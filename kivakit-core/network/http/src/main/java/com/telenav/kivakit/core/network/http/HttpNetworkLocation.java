////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.http;

import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.core.network.core.NetworkLocation;
import com.telenav.kivakit.core.network.core.NetworkPath;
import com.telenav.kivakit.core.network.core.QueryParameters;
import com.telenav.kivakit.core.network.http.project.lexakai.diagrams.DiagramHttp;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.Resourced;
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

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

@UmlClassDiagram(diagram = DiagramHttp.class)
public class HttpNetworkLocation extends NetworkLocation implements Resourced
{
    public static class Converter extends BaseStringConverter<HttpNetworkLocation>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected HttpNetworkLocation onConvertToObject(final String value)
        {
            try
            {
                // NOTE: This code is very similar to the code in HttpNetworkLocationConverter
                final var uri = new URI(value);
                final var url = uri.toURL();
                final var location = new HttpNetworkLocation(NetworkPath.networkPath(uri));
                location.queryParameters(new QueryParameters(url.getQuery()));
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

    public HttpGetResource get()
    {
        return get(NetworkAccessConstraints.DEFAULT);
    }

    @UmlRelation(label = "creates")
    public HttpGetResource get(final NetworkAccessConstraints constraints)
    {
        return new HttpGetResource(this, constraints);
    }

    public HttpGetResource get(final NetworkAccessConstraints constraints, final String contentType)
    {
        return new HttpGetResource(this, constraints)
        {
            @Override
            protected void onInitialize(final HttpGet get)
            {
                get.setHeader("Accept", contentType);
            }
        };
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

    public String readString()
    {
        return get().readString();
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
