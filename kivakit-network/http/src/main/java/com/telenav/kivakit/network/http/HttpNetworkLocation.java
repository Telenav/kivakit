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

package com.telenav.kivakit.network.http;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.core.NetworkPath;
import com.telenav.kivakit.network.core.QueryParameters;
import com.telenav.kivakit.network.http.internal.lexakai.DiagramHttp;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.Resourceful;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.net.URI;
import java.net.http.HttpRequest;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.network.core.NetworkAccessConstraints.defaultNetworkAccessConstraints;

/**
 * An HTTP network location.
 *
 * <p><b>Access</b></p>
 *
 * <p>
 * HTTP network locations can be accessed as resources with {@link #resource()}. The can also be accessed with the HTTP
 * access methods GET, PUT and POST:
 * </p>
 *
 * <ul>
 *     <li>{@link #content(Listener)}</li>
 *     <li>{@link #get()} - A {@link HttpGetResource} with default content type and access constraints</li>
 *     <li>{@link #get(String)} - A {@link HttpGetResource} with the given content type</li>
 *     <li>{@link #get(NetworkAccessConstraints)} - A {@link HttpGetResource}  with the given access constraints</li>
 *     <li>{@link #get(NetworkAccessConstraints, String)} - A {@link HttpGetResource} with the given content type and access constraints</li>
 *     <li>{@link #post()} - A {@link HttpPostResource} with default content type and access constraints</li>
 *     <li>{@link #post(String)} - A {@link HttpPostResource} with the given content to post</li>
 *     <li>{@link #post(String, String)} - A {@link HttpPostResource} with the given content and content to post</li>
 *     <li>{@link #post(NetworkAccessConstraints)} - A {@link HttpPostResource} with the given constraints</li>
 *     <li>{@link #post(NetworkAccessConstraints, String, String)} - A {@link HttpPostResource} with the given constraints, content type and content to post</li>
 *     <li>{@link #put(String, String)} - A {@link HttpPutResource} with the given content type and content to put</li>
 *     <li>{@link #put(NetworkAccessConstraints, String, String)} - A {@link HttpPutResource} with the given access constraints, content type and content to put</li>
 *     <li>{@link #resource()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #withInterpolatedVariables(VariableMap)}</li>
 *     <li>{@link #withPath(NetworkPath)}</li>
 *     <li>{@link #withQueryParameters(QueryParameters)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramHttp.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class HttpNetworkLocation extends NetworkLocation implements Resourceful
{
    public static HttpNetworkLocation httpNetworkLocation(String path)
    {
        return parseHttpNetworkLocation(throwingListener(), path);
    }

    public static HttpNetworkLocation httpNetworkLocation(NetworkPath path)
    {
        return httpNetworkLocation(path.toString());
    }

    public static HttpNetworkLocation parseHttpNetworkLocation(Listener listener, String path)
    {
        return new HttpNetworkLocationConverter(listener).convert(path);
    }

    public HttpNetworkLocation(NetworkLocation that)
    {
        super(that);
        ensure(that.port().isHttp());
    }

    public HttpNetworkLocation(URI uri)
    {
        this(parseHttpNetworkLocation(throwingListener(), uri.toString()));
    }

    public HttpNetworkLocation(NetworkPath path)
    {
        super(path);
        ensure(path.port().isHttp());
    }

    public HttpNetworkLocation child(String child)
    {
        return withPath(networkPath().withChild(child));
    }

    /**
     * Returns any text content at this network location
     *
     * @param listener The listener to call with any problems
     * @return The context
     */
    public String content(Listener listener)
    {
        return listener.listenTo(get()).asString();
    }

    /**
     * Returns an {@link HttpGetResource}
     */
    public HttpGetResource get()
    {
        return get(defaultNetworkAccessConstraints());
    }

    /**
     * Returns an {@link HttpGetResource} with the given access constraints
     */
    @UmlRelation(label = "creates")
    public HttpGetResource get(NetworkAccessConstraints constraints)
    {
        return get(constraints, null);
    }

    /**
     * Returns an {@link HttpGetResource} with the given access constraints and content type
     */
    public HttpGetResource get(NetworkAccessConstraints constraints, String contentType)
    {
        return new HttpGetResource(this, constraints)
        {
            @Override
            public void onInitialize(HttpRequest request)
            {
                if (contentType != null)
                {
                    request.headers().map().put("Accept", list(contentType));
                }
            }
        };
    }

    /**
     * Returns an {@link HttpGetResource} with the given content type
     */
    public HttpGetResource get(String contentType)
    {
        return get(defaultNetworkAccessConstraints(), contentType);
    }

    /**
     * Returns the parent network location
     */
    public HttpNetworkLocation parent()
    {
        return withPath(networkPath().parent());
    }

    /**
     * Returns an {@link HttpPostResource}
     */
    @UmlRelation(label = "creates")
    public HttpPostResource post()
    {
        return post(defaultNetworkAccessConstraints());
    }

    /**
     * Returns an {@link HttpPostResource} with the given access constraints
     */
    public HttpPostResource post(NetworkAccessConstraints constraints)
    {
        return new HttpPostResource(this, constraints);
    }

    /**
     * Returns an {@link HttpPostResource} with the given access constraints that will post the given content
     */
    public HttpPostResource post(NetworkAccessConstraints constraints, String content)
    {
        return post(constraints, null, content);
    }

    /**
     * Returns an {@link HttpPostResource} with the given access constraints that will post the given content of the
     * given content type
     */
    public HttpPostResource post(NetworkAccessConstraints constraints,
                                 String contentType,
                                 String content)
    {
        return new HttpPostResource(this, constraints)
        {
            @Override
            public void onInitialize(HttpRequest.Builder builder)
            {
                builder.POST(HttpRequest.BodyPublishers.ofString(content));
            }

            @Override
            public void onInitialize(HttpRequest request)
            {
                if (contentType != null)
                {
                    header(request, "Content-CheckType", contentType);
                }
            }
        };
    }

    /**
     * Returns an {@link HttpPostResource} that will post the given content
     */
    public HttpPostResource post(String content)
    {
        return post(defaultNetworkAccessConstraints(), content);
    }

    /**
     * Returns an {@link HttpPostResource} that will post the given content of the given content type
     */
    public HttpPostResource post(String contentType, String content)
    {
        return post(defaultNetworkAccessConstraints(), contentType, content);
    }

    public HttpPutResource put(NetworkAccessConstraints constraints,
                               String contentType,
                               String content)
    {
        return new HttpPutResource(this, constraints)
        {
            @Override
            public void onInitialize(HttpRequest.Builder builder)
            {
                builder.PUT(HttpRequest.BodyPublishers.ofString(content));
            }

            @Override
            public void onInitialize(HttpRequest request)
            {
                header(request, "Content-CheckType", contentType);
            }
        };
    }

    @UmlRelation(label = "creates")
    public HttpPutResource put(String contentType, String content)
    {
        return put(defaultNetworkAccessConstraints(), contentType, content);
    }

    /**
     * Returns the text resource at this location
     */
    @Override
    public Resource resource()
    {
        return get();
    }

    @Override
    public HttpNetworkLocation withInterpolatedVariables(VariableMap<String> variables)
    {
        return new HttpNetworkLocation(super.withInterpolatedVariables(variables));
    }

    @Override
    public HttpNetworkLocation withPath(NetworkPath path)
    {
        return new HttpNetworkLocation(super.withPath(path));
    }

    @Override
    public HttpNetworkLocation withQueryParameters(QueryParameters queryParameters)
    {
        return new HttpNetworkLocation(super.withQueryParameters(queryParameters));
    }
}
