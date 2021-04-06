////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.http;

import com.telenav.kivakit.core.network.http.project.lexakai.diagrams.DiagramHttp;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.kivakit.core.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.core.network.core.BaseNetworkResource;
import com.telenav.kivakit.core.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.core.network.core.NetworkLocation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * A network resource accessible via HTTP.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("deprecation")
@UmlClassDiagram(diagram = DiagramHttp.class)
public abstract class BaseHttpResource extends BaseNetworkResource
{
    @UmlAggregation
    private final NetworkAccessConstraints constraints;

    @UmlAggregation
    private final NetworkLocation networkLocation;

    // Response content encoding
    private String encoding;

    // Entity opened, to be able to close it
    private HttpEntity entityMirror;

    private final VariableMap<String> responseHeader = new VariableMap<>();

    private int statusCode;

    /**
     * Constructs a resource accessible via HTTP
     */
    protected BaseHttpResource(final NetworkLocation networkLocation, final NetworkAccessConstraints constraints)
    {
        super(networkLocation);
        this.networkLocation = networkLocation;
        this.constraints = constraints;
    }

    public void consume()
    {
        if (entityMirror != null)
        {
            EntityUtils.consumeQuietly(entityMirror);
        }
    }

    public String contentType()
    {
        return header("Content-CheckType");
    }

    public String encoding()
    {
        return encoding;
    }

    public String header(final String header)
    {
        final var client = newClient();
        final var head = new HttpHead(asUri());
        try
        {
            final HttpResponse response = client.execute(head);
            final var value = response.getFirstHeader(header).getValue();
            EntityUtils.consume(response.getEntity());
            return value;
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isRemote()
    {
        return true;
    }

    @Override
    public NetworkLocation location()
    {
        return networkLocation;
    }

    /**
     * Opens this resource
     *
     * @return Input stream to read
     */
    @Override
    public InputStream onOpenForReading()
    {
        try
        {
            final var client = newClient();

            // Set the timeouts
            final var httpParameters = client.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    (int) constraints.timeout().asMilliseconds());
            HttpConnectionParams.setSoTimeout(httpParameters, (int) constraints.timeout().asMilliseconds());

            if (constraints instanceof HttpAccessConstraints)
            {
                final var httpConstraints = (HttpAccessConstraints) constraints;
                final var credentials = httpConstraints.httpBasicCredentials();
                if (credentials != null)
                {
                    client.getCredentialsProvider().setCredentials(
                            new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), new UsernamePasswordCredentials(
                                    credentials.userName().toString(), credentials.password().toString()));
                }
            }
            final var httpRequest = newRequest();
            final HttpResponse response = client.execute(httpRequest);
            statusCode = response.getStatusLine().getStatusCode();

            if (responseHeader != null)
            {
                for (final var header : response.getAllHeaders())
                {
                    responseHeader.put(header.getName(), header.getValue());
                }
            }

            final var entity = response.getEntity();
            if (entity != null)
            {
                final var contentEncoding = entity.getContentEncoding();
                if (contentEncoding != null)
                {
                    encoding = contentEncoding.getValue();
                }
                entityMirror = entity;
                return entity.getContent();
            }
        }
        catch (final Exception e)
        {
            throw new IllegalStateException("Can't open input stream", e);
        }
        return null;
    }

    public String readString()
    {
        return reader().string();
    }

    public VariableMap<String> responseHeader()
    {
        return responseHeader;
    }

    @UmlRelation(label = "yields")
    public HttpStatus status()
    {
        return new HttpStatus(statusCode);
    }

    @Override
    public String toString()
    {
        return networkLocation.toString();
    }

    protected DefaultHttpClient newClient()
    {
        return new DefaultHttpClient();
    }

    protected abstract HttpUriRequest newRequest();
}
