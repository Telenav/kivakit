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

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.interfaces.model.Initializer;
import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.core.NetworkPath;
import com.telenav.kivakit.network.core.QueryParameters;
import com.telenav.kivakit.network.http.project.lexakai.diagrams.DiagramHttp;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.Resourceful;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;

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
 *     <li>{@link #get()} - A {@link HttpGetResource} with default content type and access constraints</li>
 *     <li>{@link #get(String)} - A {@link HttpGetResource} with the given content type</li>
 *     <li>{@link #get(NetworkAccessConstraints)} - A {@link HttpGetResource}  with the given access contraints</li>
 *     <li>{@link #get(NetworkAccessConstraints, String)} - A {@link HttpGetResource} with the given content type and access constraints</li>
 *     <li>{@link #post()} - A {@link HttpPostResource} with default content type and access constraints</li>
 *     <li>{@link #post(String)} - A {@link HttpPostResource} with the given value to post</li>
 *     <li>{@link #post(String, String)} - A {@link HttpPostResource} with the given content and value to post</li>
 *     <li>{@link #post(NetworkAccessConstraints)} - A {@link HttpPostResource} with the given constraints</li>
 *     <li>{@link #post(NetworkAccessConstraints, String, String)} - A {@link HttpPostResource} with the given constrains, content type and value to post</li>
 *     <li>{@link #put(String, String)} - A {@link HttpPutResource} with the given content type and value to put</li>
 *     <li>{@link #put(NetworkAccessConstraints, String, String)} - A {@link HttpPutResource} with the given access constraints, content type and value to put</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHttp.class)
@LexakaiJavadoc(complete = true)
public class HttpNetworkLocation extends NetworkLocation implements Resourceful
{
    /**
     * Converts to and from {@link HttpNetworkLocation}
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<HttpNetworkLocation>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected HttpNetworkLocation onToValue(final String value)
        {
            try
            {
                // NOTE: This code is very similar to the code in HttpNetworkLocationConverter
                final var uri = new URI(value);
                final var url = uri.toURL();
                final var location = new HttpNetworkLocation(NetworkPath.networkPath(uri));
                location.queryParameters(QueryParameters.parse(url.getQuery()));
                location.reference(url.getRef());
                return location;
            }
            catch (final URISyntaxException | MalformedURLException e)
            {
                problem(e, "Bad network location ${debug}", value);
                return null;
            }
        }
    }

    public HttpNetworkLocation(final NetworkLocation that)
    {
        super(that);
        ensure(that.port().isHttp());
    }

    public HttpNetworkLocation(final NetworkPath path)
    {
        super(path);
        ensure(path.port().isHttp());
    }

    public HttpNetworkLocation child(final String child)
    {
        return withPath(networkPath().withChild(child));
    }

    public String content()
    {
        return get().content();
    }

    public HttpGetResource get()
    {
        return get(NetworkAccessConstraints.DEFAULT);
    }

    @UmlRelation(label = "creates")
    public HttpGetResource get(final NetworkAccessConstraints constraints)
    {
        return new HttpGetResource(this, constraints);
    }

    public HttpGetResource get(final NetworkAccessConstraints constraints, final Initializer<HttpGet> initializer)
    {
        return new HttpGetResource(this, constraints)
        {
            @Override
            protected void onInitialize(final HttpGet get)
            {
                initializer.initialize(get);
            }
        };
    }

    public HttpGetResource get(final NetworkAccessConstraints constraints, final String contentType)
    {
        return get(constraints, get -> get.setHeader("Accept", contentType));
    }

    public HttpGetResource get(final String contentType)
    {
        return get(NetworkAccessConstraints.DEFAULT, contentType);
    }

    public HttpNetworkLocation parent()
    {
        return withPath(networkPath().parent());
    }

    @UmlRelation(label = "creates")
    public HttpPostResource post()
    {
        return post(NetworkAccessConstraints.DEFAULT);
    }

    public HttpPostResource post(final NetworkAccessConstraints constraints)
    {
        return new HttpPostResource(this, constraints);
    }

    public HttpPostResource post(final NetworkAccessConstraints constraints, final String content)
    {
        return post(constraints, null, content);
    }

    public HttpPostResource post(final NetworkAccessConstraints constraints, final String contentType,
                                 final String value)
    {
        return new HttpPostResource(this, constraints)
        {
            @Override
            protected void onInitialize(final HttpPost post)
            {
                try
                {
                    if (contentType != null)
                    {
                        post.setHeader("Content-CheckType", contentType);
                    }
                    post.setEntity(new StringEntity(value));
                }
                catch (final UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }
        };
    }

    public HttpPostResource post(final String value)
    {
        return post(NetworkAccessConstraints.DEFAULT, value);
    }

    public HttpPostResource post(final String contentType, final String value)
    {
        return post(NetworkAccessConstraints.DEFAULT, contentType, value);
    }

    public HttpPutResource put(final NetworkAccessConstraints constraints, final String contentType,
                               final String content)
    {
        return new HttpPutResource(this, constraints)
        {
            @Override
            protected void onInitialize(final HttpPut put)
            {
                try
                {
                    put.setHeader("Content-CheckType", contentType);
                    put.setEntity(new StringEntity(content));
                }
                catch (final UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }
        };
    }

    @UmlRelation(label = "creates")
    public HttpPutResource put(final String contentType, final String value)
    {
        return put(NetworkAccessConstraints.DEFAULT, contentType, value);
    }

    @Override
    public Resource resource()
    {
        return get();
    }

    @Override
    public HttpNetworkLocation withInterpolatedVariables(final VariableMap<String> variables)
    {
        return new HttpNetworkLocation(super.withInterpolatedVariables(variables));
    }

    @Override
    public HttpNetworkLocation withPath(final NetworkPath path)
    {
        return new HttpNetworkLocation(super.withPath(path));
    }

    @Override
    public HttpNetworkLocation withQueryParameters(final QueryParameters queryParameters)
    {
        return new HttpNetworkLocation(super.withQueryParameters(queryParameters));
    }
}
