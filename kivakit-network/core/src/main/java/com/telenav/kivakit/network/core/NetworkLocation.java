////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.network.core;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.language.reflection.property.IncludeProperty;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.broadcasters.GlobalRepeater;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.kivakit.network.core.internal.lexakai.DiagramNetworkLocation;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.Hash.hashMany;
import static com.telenav.kivakit.core.language.Objects.isEqual;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.network.core.NetworkPath.parseNetworkPath;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * A network location with a {@link Port}, {@link NetworkPath}, {@link NetworkAccessConstraints} and optional
 * {@link QueryParameters}.
 *
 * <p><b>Factory Methods</b></p>
 *
 * <ul>
 *     <li>{@link #networkLocation(Listener, URI)}</li>
 * </ul>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parseNetworkLocation(Listener, String)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #constraints()}</li>
 *     <li>{@link #constraints(NetworkAccessConstraints)}</li>
 *     <li>{@link #host()}</li>
 *     <li>{@link #networkPath()}</li>
 *     <li>{@link #port()}</li>
 *     <li>{@link #protocol()}</li>
 *     <li>{@link #queryParameters()}</li>
 *     <li>{@link #reference()}</li>
 * </ul>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
 *     <li>{@link #compareTo(NetworkLocation)}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asUri()}</li>
 *     <li>{@link #asUrl()}</li>
 *     <li>{@link #asString(Format)}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #withPath(NetworkPath)}</li>
 *     <li>{@link #withInterpolatedVariables(VariableMap)}</li>
 *     <li>{@link #withQueryParameters(QueryParameters)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramNetworkLocation.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class NetworkLocation implements
    StringFormattable,
    Comparable<NetworkLocation>,
    GlobalRepeater
{
    /**
     * Returns a network location for a {@link URI}
     *
     * @param listener The listener to call with any problems
     * @param uri The URI
     * @return The network location
     */
    public static NetworkLocation networkLocation(Listener listener, URI uri)
    {
        try
        {
            var location = new NetworkLocation(NetworkPath.networkPath(listener, uri));
            location.queryParameters(new QueryParameters(uri.getQuery()));
            var url = uri.toURL();
            location.reference(url.getRef());
            return location;
        }
        catch (Exception e)
        {
            listener.problem("Could not convert URI to NetworkLocation: $", uri);
            return null;
        }
    }

    /**
     * Parses the given text into a network location
     *
     * @param listener The listener to call with any problems
     * @param text The text to parse
     * @return The network location
     */
    public static NetworkLocation parseNetworkLocation(Listener listener, String text)
    {
        try
        {
            return networkLocation(listener, new URI(text));
        }
        catch (URISyntaxException e)
        {
            listener.problem(e, "Bad network location ${debug}", text);
            return null;
        }
    }

    /** The constraints for accessing this network location */
    @UmlAggregation
    private NetworkAccessConstraints constraints;

    /** The path to this location */
    @UmlAggregation
    @FormatProperty
    private NetworkPath networkPath;

    /** The host and port for this location */
    @UmlAggregation
    @FormatProperty
    private final Port port;

    /** Any query parameters for this location */
    @UmlAggregation(label = "optional")
    private QueryParameters queryParameters;

    /** Any hyperlink reference */
    private String reference;

    public NetworkLocation(NetworkLocation that)
    {
        port = that.port;
        networkPath = that.networkPath;
        constraints = that.constraints;
        queryParameters = that.queryParameters;
        reference = that.reference;
    }

    public NetworkLocation(NetworkPath networkPath)
    {
        checkPath(networkPath);
        this.networkPath = networkPath;
        port = networkPath.port();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asString(@NotNull Format format)
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * Returns this location as a {@link URI}
     */
    public URI asUri()
    {
        try
        {
            var username = constraints == null || constraints.userName() == null ? null : constraints.userName().name();
            var portNumber = port().portNumber();
            if (protocol().defaultPort() == portNumber)
            {
                portNumber = -1;
            }
            // Decode path, so we avoid double-encoding if the path is already encoded
            var path = URLDecoder.decode(networkPath().asStringPath().toString(), UTF_8);
            return new URI(protocol().name(), username, host().name(), portNumber, "/" + path,
                queryParameters == null ? null : queryParameters.toString(), reference);
        }
        catch (URISyntaxException e)
        {
            throw new IllegalStateException("Cannot convert " + this + " to URI", e);
        }
    }

    /**
     * Returns this location as a {@link URL}
     */
    public URL asUrl()
    {
        try
        {
            var file = networkPath.asStringPath().toString();
            if (queryParameters != null)
            {
                file += "?" + queryParameters;
            }
            if (reference != null)
            {
                file += "#" + reference;
            }
            var portNumber = port().portNumber();
            if (protocol().defaultPort() == portNumber)
            {
                portNumber = -1;
            }
            return new URL(protocol().name(), host().address().getHostAddress(), portNumber, "/" + file);
        }
        catch (MalformedURLException e)
        {
            throw new IllegalStateException("Cannot convert " + this + " to URL", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(NetworkLocation that)
    {
        return asUri().compareTo(that.asUri());
    }

    /**
     * Returns the constraints for accessing this network location
     */
    public NetworkAccessConstraints constraints()
    {
        return constraints;
    }

    /**
     * Sets the constraints for accessing this network location
     */
    public void constraints(NetworkAccessConstraints constraints)
    {
        this.constraints = constraints;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof NetworkLocation that)
        {
            return port.equals(that.port) && networkPath.equals(that.networkPath)
                && isEqual(queryParameters, that.queryParameters)
                && isEqual(reference, that.reference);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return hashMany(port, networkPath, queryParameters, reference);
    }

    /**
     * Returns the host for this network location
     */
    @IncludeProperty
    public Host host()
    {
        return port().host();
    }

    /**
     * Returns true if this location is a child of the given location
     */
    public boolean isChildOf(NetworkLocation that)
    {
        // NOTE: This is not really sufficient, but works for our cases for now
        return toString().startsWith(that.toString());
    }

    /**
     * Returns the path portion of this network location
     */
    @IncludeProperty
    public NetworkPath networkPath()
    {
        return networkPath;
    }

    /**
     * Returns the host and port for this location
     */
    @IncludeProperty
    public Port port()
    {
        return port;
    }

    /**
     * Returns the protocol required to access this network location
     */
    @IncludeProperty
    public Protocol protocol()
    {
        return port.protocol();
    }

    /**
     * Returns any query parameters for this network location
     */
    @IncludeProperty
    public QueryParameters queryParameters()
    {
        return queryParameters;
    }

    /**
     * Sets any query parameters for this network location
     */
    public void queryParameters(QueryParameters queryParameters)
    {
        this.queryParameters = queryParameters;
    }

    /**
     * Returns any ref value for this network location
     */
    public String reference()
    {
        return reference;
    }

    /**
     * Sets any ref value for this network location
     */
    public void reference(String fragment)
    {
        reference = fragment;
    }

    @Override
    public String toString()
    {
        var builder = new StringBuilder();
        builder.append(protocol());
        builder.append("://");
        if (constraints != null && constraints.userName() != null)
        {
            builder.append(constraints.userName());
            if (constraints.password() != null)
            {
                builder.append(':');
                builder.append(constraints.password());
            }
            builder.append('@');
        }
        builder.append(port);
        builder.append(networkPath.asStringPath().withRoot("/"));
        if (queryParameters != null && !queryParameters.isEmpty())
        {
            builder.append('?');
            builder.append(queryParameters);
        }
        if (reference != null)
        {
            builder.append('#');
            builder.append(reference);
        }
        return builder.toString();
    }

    /**
     * Returns this network location with any variable markers expanded using the given variable map
     *
     * @param variables The variables to interpolate
     * @return The new network location
     */
    @UmlExcludeMember
    public NetworkLocation withInterpolatedVariables(VariableMap<String> variables)
    {
        // Interpolate variables in path
        var interpolatedPath = format(networkPath().toString(), variables);

        // Create location with the given path
        var location = withPath(parseNetworkPath(this, interpolatedPath));

        // If there are any query parameters,
        if (queryParameters() != null)
        {
            // interpolate variables into query parameter string
            var interpolatedQueryParameters = format(queryParameters().toString(), variables);

            // and create a new location with the interpolated value
            location = location.withQueryParameters(new QueryParameters(interpolatedQueryParameters));
        }
        return location;
    }

    /**
     * Returns this network location with the given path
     */
    public NetworkLocation withPath(NetworkPath path)
    {
        var location = new NetworkLocation(this);
        location.networkPath = path;
        return location;
    }

    /**
     * Returns this network location with the given query parameters
     */
    public NetworkLocation withQueryParameters(QueryParameters queryParameters)
    {
        var location = new NetworkLocation(this);
        location.queryParameters = queryParameters;
        return location;
    }

    private void checkPath(NetworkPath path)
    {
        if (path != null)
        {
            var pathString = path.toString();
            if (pathString.indexOf('&') >= 0 || pathString.indexOf('?') >= 0 || pathString.indexOf('#') >= 0)
            {
                throw new IllegalArgumentException(
                    "NetworkLocation path cannot contain query parameters or references:  " + pathString);
            }
        }
    }
}
