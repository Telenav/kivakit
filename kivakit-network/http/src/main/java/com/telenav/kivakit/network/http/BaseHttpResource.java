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

import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.network.core.BaseNetworkResource;
import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.http.project.lexakai.diagrams.DiagramHttp;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
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
 * <p><b>Content</b></p>
 *
 * <p>
 * The content of an HTTP resource can be accessed using any of the methods inherited by all resources. An HTTP resource
 * also has:
 * </p>
 *
 * <ul>
 *     <li>{@link #retrieveContentType()} - A MIME content type</li>
 *     <li>{@link #asString()} - The resource content as a string</li>
 *     <li>{@link #encoding()} - A content encoding</li>
 *     <li>{@link #retrieveHeaderField(String)} - An optional header</li>
 *     <li>{@link #status()} - A status code once the resource has been accessed</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("deprecation")
@UmlClassDiagram(diagram = DiagramHttp.class)
@LexakaiJavadoc(complete = true)
public abstract class BaseHttpResource extends BaseNetworkResource
{
    @UmlAggregation
    private final NetworkAccessConstraints constraints;

    @UmlAggregation
    private final NetworkLocation networkLocation;

    private String contentEncoding;

    private final VariableMap<String> responseHeader = new VariableMap<>();

    private int statusCode;

    private HttpResponse response;

    /**
     * Constructs a resource accessible via HTTP
     */
    protected BaseHttpResource(final NetworkLocation networkLocation, final NetworkAccessConstraints constraints)
    {
        super(networkLocation);
        this.networkLocation = networkLocation;
        this.constraints = constraints;
    }

    /**
     * @return The content of this HTTP resource as a string
     */
    public String asString()
    {
        return reader().string();
    }

    /**
     * @return The content encoding once the resource has been opened for reading
     */
    public String encoding()
    {
        return contentEncoding;
    }

    /**
     * @return Always true
     */
    @Override
    public boolean isRemote()
    {
        return true;
    }

    /**
     * @return The network location of this HTTP resource
     */
    @Override
    public NetworkLocation location()
    {
        return networkLocation;
    }

    /**
     * Opens this resource for reading
     *
     * @return Input stream to read
     */
    @Override
    public InputStream onOpenForReading()
    {
        try
        {
            executeRequest(newRequest());

            final var entity = response.getEntity();
            if (entity != null)
            {
                final var contentEncoding = entity.getContentEncoding();
                if (contentEncoding != null)
                {
                    this.contentEncoding = contentEncoding.getValue();
                }
                return entity.getContent();
            }
        }
        catch (final Exception e)
        {
            illegalState(e, "Can't open input stream");
        }
        return null;
    }

    /**
     * @return Response header variables
     */
    public VariableMap<String> responseHeader()
    {
        return responseHeader;
    }

    /**
     * @return The content type, as determined by a Content-CheckType HTTP HEAD request
     */
    public String retrieveContentType()
    {
        return retrieveHeaderField("Content-CheckType");
    }

    /**
     * @return The value for the given HTTP header field, as determined by an HTTP HEAD request
     */
    public String retrieveHeaderField(final String fieldName)
    {
        final var client = newClient();
        final var head = new HttpHead(asUri());
        try
        {
            final HttpResponse response = client.execute(head);
            final var value = response.getFirstHeader(fieldName).getValue();
            EntityUtils.consume(response.getEntity());
            return value;
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Executes the request for this resource and returns the status code
     *
     * @return The status code for this resource
     */
    @UmlRelation(label = "yields")
    public HttpStatus status()
    {
        executeRequest(newRequest());
        return new HttpStatus(statusCode);
    }

    @Override
    public String toString()
    {
        return networkLocation.toString();
    }

    /**
     * @return A configured HTTP client
     */
    protected DefaultHttpClient newClient()
    {
        var client = new DefaultHttpClient();

        // Set timeouts
        final var httpParameters = client.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                (int) constraints.timeout().asMilliseconds());
        HttpConnectionParams.setSoTimeout(httpParameters, (int) constraints.timeout().asMilliseconds());

        // add any credentials
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

        return client;
    }

    /**
     * @return The subclass' request object
     */
    protected abstract HttpUriRequest newRequest();

    /**
     * Executes the given request, reads the status code and header map
     *
     * @param httpRequest The HTTP request to execute
     */
    private void executeRequest(final HttpUriRequest httpRequest)
    {
        if (response == null)
        {
            try
            {
                response = newClient().execute(httpRequest);
                statusCode = response.getStatusLine().getStatusCode();
                if (responseHeader != null)
                {
                    for (final var header : response.getAllHeaders())
                    {
                        responseHeader.put(header.getName(), header.getValue());
                    }
                }
            }
            catch (Exception e)
            {
                problem(e, "Unable to execute HTTP request to: $", httpRequest);
            }
        }
    }
}
