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

package com.telenav.kivakit.network.http.secure;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.network.core.NetworkPath;
import com.telenav.kivakit.network.core.QueryParameters;
import com.telenav.kivakit.network.http.HttpGetResource;
import com.telenav.kivakit.network.http.HttpNetworkLocation;
import com.telenav.kivakit.network.http.HttpPostResource;
import com.telenav.kivakit.network.http.internal.lexakai.DiagramHttps;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.network.core.Protocol.HTTPS;

/**
 * A secure network location using the HTTPS protocol.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHttps.class)
@UmlRelation(label = "creates", referent = SecureHttpGetResource.class)
@UmlRelation(label = "creates", referent = SecureHttpPostResource.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class SecureHttpNetworkLocation extends HttpNetworkLocation
{
    public static SecureHttpNetworkLocation parseSecureHttpNetworkLocation(Listener listener, String path)
    {
        return new Converter(listener).convert(path);
    }

    public static SecureHttpNetworkLocation secureHttpNetworkLocation(String path)
    {
        return parseSecureHttpNetworkLocation(throwingListener(), path);
    }

    public static SecureHttpNetworkLocation secureHttpNetworkLocation(NetworkPath path)
    {
        return secureHttpNetworkLocation(path.toString());
    }

    /**
     * Converts to and from {@link SecureHttpNetworkLocation}
     *
     * @author jonathanl (shibo)
     */
    @CodeQuality(stability = STABLE,
                 testing = UNTESTED,
                 documentation = DOCUMENTATION_COMPLETE)
    public static class Converter extends BaseStringConverter<SecureHttpNetworkLocation>
    {
        public Converter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected SecureHttpNetworkLocation onToValue(String value)
        {
            try
            {
                // NOTE: This code is very similar to the code in HttpNetworkLocationConverter
                var uri = new URI(value);
                var url = uri.toURL();
                var location = new SecureHttpNetworkLocation(NetworkPath.networkPath(this, uri));
                var query = url.getQuery();
                if (query != null)
                {
                    location.queryParameters(QueryParameters.parseQueryParameters(this, query));
                }
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

    public SecureHttpNetworkLocation(NetworkPath path)
    {
        super(path);
        ensure(HTTPS.equals(path.port().protocol()));
    }

    public SecureHttpNetworkLocation(URI uri)
    {
        this(parseHttpNetworkLocation(throwingListener(), uri.toString()).networkPath());
    }

    @Override
    public HttpGetResource get()
    {
        return new SecureHttpGetResource(this);
    }

    @Override
    public HttpPostResource post()
    {
        return new SecureHttpPostResource(this);
    }
}
