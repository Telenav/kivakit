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

import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.language.objects.Objects;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.network.core.project.lexakai.diagrams.DiagramNetworkLocation;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * A network location with a {@link Port}, {@link NetworkPath}, {@link NetworkAccessConstraints} and optional {@link
 * QueryParameters}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramNetworkLocation.class)
@LexakaiJavadoc(complete = true)
public class NetworkLocation implements Stringable, Comparable<NetworkLocation>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * Converts to and from a {@link NetworkLocation}
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<NetworkLocation>
    {
        public Converter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected NetworkLocation onToValue(String value)
        {
            try
            {
                var uri = new URI(value);
                var location = new NetworkLocation(NetworkPath.networkPath(this, uri));
                location.queryParameters(new QueryParameters(uri.getQuery()));
                var url = uri.toURL();
                location.reference(url.getRef());
                return location;
            }
            catch (URISyntaxException | MalformedURLException e)
            {
                problem(e, "Bad network location ${debug}", value);
                return null;
            }
        }
    }

    @UmlAggregation
    private NetworkAccessConstraints constraints;

    @UmlAggregation
    private NetworkPath networkPath;

    @UmlAggregation
    private final Port port;

    @UmlAggregation(label = "optional")
    private QueryParameters queryParameters;

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

    @Override
    public String asString(Format format)
    {
        return new ObjectFormatter(this).toString();
    }

    public URI asUri()
    {
        try
        {
            var username = constraints == null || constraints.userName() == null ? null : constraints.userName().name();
            var portNumber = port().number();
            if (protocol().defaultPort() == portNumber)
            {
                portNumber = -1;
            }
            // Decode path so we avoid double-encoding if the path is already encoded
            var path = URLDecoder.decode(networkPath().asStringPath().toString(), StandardCharsets.UTF_8);
            return new URI(protocol().name(), username, host().name(), portNumber, "/" + path,
                    queryParameters == null ? null : queryParameters.toString(), reference);
        }
        catch (URISyntaxException e)
        {
            throw new IllegalStateException("Cannot convert " + this + " to URI", e);
        }
    }

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
            var portNumber = port().number();
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

    @Override
    public int compareTo(NetworkLocation that)
    {
        return asUri().compareTo(that.asUri());
    }

    public NetworkAccessConstraints constraints()
    {
        return constraints;
    }

    public void constraints(NetworkAccessConstraints constraints)
    {
        this.constraints = constraints;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof NetworkLocation)
        {
            var that = (NetworkLocation) object;
            return port.equals(that.port) && networkPath.equals(that.networkPath)
                    && Objects.equal(queryParameters, that.queryParameters)
                    && Objects.equal(reference, that.reference);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(port, networkPath, queryParameters, reference);
    }

    @KivaKitIncludeProperty
    public Host host()
    {
        return port().host();
    }

    public boolean isChildOf(NetworkLocation that)
    {
        // NOTE: This is not really sufficient, but works for our cases for now
        return toString().startsWith(that.toString());
    }

    @KivaKitIncludeProperty
    public NetworkPath networkPath()
    {
        return networkPath;
    }

    @KivaKitIncludeProperty
    public Port port()
    {
        return port;
    }

    @KivaKitIncludeProperty
    public Protocol protocol()
    {
        return port.protocol();
    }

    @KivaKitIncludeProperty
    public QueryParameters queryParameters()
    {
        return queryParameters;
    }

    public void queryParameters(QueryParameters queryParameters)
    {
        this.queryParameters = queryParameters;
    }

    public String reference()
    {
        return reference;
    }

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

    @UmlExcludeMember
    public NetworkLocation withInterpolatedVariables(VariableMap<String> variables)
    {
        // Interpolate variables in path
        var formatter = new MessageFormatter();
        var interpolatedPath = formatter.format(networkPath().toString(), variables);

        // Create location with the given path
        var location = withPath(NetworkPath.parseNetworkPath(LOGGER, interpolatedPath));

        // If there are any query parameters,
        if (queryParameters() != null)
        {
            // interpolate variables into query parameter string
            var interpolatedQueryParameters = formatter.format(queryParameters().toString(), variables);

            // and create a new location with the interpolated value
            location = location.withQueryParameters(new QueryParameters(interpolatedQueryParameters));
        }
        return location;
    }

    public NetworkLocation withPath(NetworkPath path)
    {
        var location = new NetworkLocation(this);
        location.networkPath = path;
        return location;
    }

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
