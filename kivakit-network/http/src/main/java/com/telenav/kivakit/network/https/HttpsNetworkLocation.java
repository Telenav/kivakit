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

package com.telenav.kivakit.network.https;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.network.core.NetworkPath;
import com.telenav.kivakit.network.http.HttpGetResource;
import com.telenav.kivakit.network.http.HttpNetworkLocation;
import com.telenav.kivakit.network.http.HttpPostResource;
import com.telenav.kivakit.network.http.internal.lexakai.DiagramHttps;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.net.URI;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
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
@UmlRelation(label = "creates", referent = HttpsGetResource.class)
@UmlRelation(label = "creates", referent = HttpsPostResource.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class HttpsNetworkLocation extends HttpNetworkLocation
{
    public static HttpsNetworkLocation parseSecureHttpNetworkLocation(Listener listener, String path)
    {
        return new HttpsNetworkLocationConverter(listener).convert(path);
    }

    public static HttpsNetworkLocation secureHttpNetworkLocation(String path)
    {
        return parseSecureHttpNetworkLocation(throwingListener(), path);
    }

    public static HttpsNetworkLocation secureHttpNetworkLocation(NetworkPath path)
    {
        return secureHttpNetworkLocation(path.toString());
    }

    public HttpsNetworkLocation(NetworkPath path)
    {
        super(path);
        ensure(HTTPS.equals(path.port().protocol()));
    }

    public HttpsNetworkLocation(URI uri)
    {
        this(parseHttpNetworkLocation(throwingListener(), uri.toString()).networkPath());
    }

    @Override
    public HttpGetResource get()
    {
        return new HttpsGetResource(this);
    }

    @Override
    public HttpPostResource post()
    {
        return new HttpsPostResource(this);
    }
}
