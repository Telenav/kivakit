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

import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.network.core.BaseNetworkResource;
import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.http.project.lexakai.DiagramHttp;
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
import java.util.Objects;

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
 *     <li>{@link #httpHeadRequestContentType()} - A MIME content type</li>
 *     <li>{@link #asString()} - The resource content as a string</li>
 *     <li>{@link #encoding()} - A content encoding</li>
 *     <li>{@link #httpHeadRequestHeaderField(String)} - An optional header</li>
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

    private String contentEncoding;

    @UmlAggregation
    private final NetworkLocation networkLocation;

    private HttpResponse response;

    private final VariableMap<String> responseHeader = new VariableMap<>();

    private int statusCode;

    /**
     * Constructs a resource accessible via HTTP
     */
    protected BaseHttpResource(NetworkLocation networkLocation, NetworkAccessConstraints constraints)
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
        return reader().asString();
    }

    /**
     * Executes the request for this resource and returns the content type from the response header
     *
     * @return The content type of this resource
     */
    public String contentType()
    {
        return responseHeader().get("Content-Type");
    }

    /**
     * @return The content encoding once the resource has been opened for reading
     */
    public String encoding()
    {
        return contentEncoding;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof BaseHttpResource)
        {
            BaseHttpResource that = (BaseHttpResource) object;
            return networkLocation.equals(that.networkLocation);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(networkLocation);
    }

    /**
     * @return The content type, as determined by a Content-CheckType HTTP HEAD request
     */
    public String httpHeadRequestContentType()
    {
        return httpHeadRequestHeaderField("Content-CheckType");
    }

    /**
     * @return The value for the given HTTP header field, as determined by an HTTP HEAD request
     */
    public String httpHeadRequestHeaderField(String fieldName)
    {
        var client = newClient();
        var head = new HttpHead(asUri());
        try
        {
            HttpResponse response = client.execute(head);
            var value = response.getFirstHeader(fieldName).getValue();
            EntityUtils.consume(response.getEntity());
            return value;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
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

            if (status().isOkay())
            {
                var entity = response.getEntity();
                if (entity != null)
                {
                    var contentEncoding = entity.getContentEncoding();
                    if (contentEncoding != null)
                    {
                        this.contentEncoding = contentEncoding.getValue();
                    }
                    return entity.getContent();
                }
                else
                {
                    throw new Problem("No entity found for: $", this).asException();
                }
            }
            else
            {
                throw new Problem("Request failed (HTTP status code $): $", status(), this).asException();
            }
        }
        catch (Exception e)
        {
            throw new Problem(e, "Cannot open: $", this).asException();
        }
    }

    /**
     * @return Response header variables
     */
    public VariableMap<String> responseHeader()
    {
        executeRequest(newRequest());
        return responseHeader;
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
        var httpParameters = client.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                (int) constraints.timeout().asMilliseconds());
        HttpConnectionParams.setSoTimeout(httpParameters, (int) constraints.timeout().asMilliseconds());

        // add any credentials
        if (constraints instanceof HttpAccessConstraints)
        {
            var httpConstraints = (HttpAccessConstraints) constraints;
            var credentials = httpConstraints.httpBasicCredentials();
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
    private void executeRequest(HttpUriRequest httpRequest)
    {
        if (response == null)
        {
            try
            {
                response = newClient().execute(httpRequest);
                statusCode = response.getStatusLine().getStatusCode();
                if (responseHeader != null)
                {
                    for (var header : response.getAllHeaders())
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
