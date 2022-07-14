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
import com.telenav.kivakit.core.io.StringInputStream;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.network.core.BaseNetworkResource;
import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.http.lexakai.DiagramHttp;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

import static java.net.http.HttpRequest.BodyPublishers.noBody;
import static java.net.http.HttpResponse.BodyHandlers.ofString;

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
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramHttp.class)
@LexakaiJavadoc(complete = true)
public abstract class BaseHttpResource extends BaseNetworkResource implements HttpRequestFactory
{
    @UmlAggregation
    private final NetworkAccessConstraints constraints;

    private String contentEncoding;

    @UmlAggregation
    private final NetworkLocation networkLocation;

    private HttpResponse<String> response;

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
        var head = HttpRequest.newBuilder()
                .uri(asUri())
                .method("HEAD", noBody())
                .build();
        try
        {
            var response = client.send(head, ofString());
            return response.headers().firstValue(fieldName).orElse("");
        }
        catch (Exception e)
        {
            problem(e, "Could not get header field '$'", fieldName);
            return null;
        }
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
            send(newRequest());

            if (status().isOkay())
            {
                contentEncoding = response.headers().firstValue("Content-Encoding").orElse(null);
                var body = response.body();
                return new StringInputStream(response.body());
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
        send(newRequest());
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
        send(newRequest());
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
    protected HttpClient newClient()
    {
        var builder = HttpClient.newBuilder()
                .connectTimeout(constraints.timeout().asJavaDuration());

        // add any credentials
        if (constraints instanceof HttpAccessConstraints)
        {
            var httpConstraints = (HttpAccessConstraints) constraints;
            var credentials = httpConstraints.httpBasicCredentials();
            if (credentials != null)
            {
                builder = builder.authenticator(new Authenticator()
                {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(credentials.userName().name(), credentials.password().toString().toCharArray());
                    }
                });
            }
        }

        return builder.build();
    }

    protected HttpRequest newRequest()
    {
        return build(asUri());
    }

    /**
     * Executes the given request, reads the status code and header map
     *
     * @param httpRequest The HTTP request to execute
     */
    private void send(HttpRequest httpRequest)
    {
        if (response == null)
        {
            try
            {
                response = newClient().send(httpRequest, ofString());
                statusCode = response.statusCode();
                if (responseHeader != null)
                {
                    var map = response.headers().map();
                    for (var name : map.keySet())
                    {
                        responseHeader.put(name, map.get(name).get(0));
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
