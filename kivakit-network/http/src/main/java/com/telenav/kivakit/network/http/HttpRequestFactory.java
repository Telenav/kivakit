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

import com.telenav.kivakit.annotations.code.ApiQuality;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Allows customization of an {@link HttpRequest} through the {@link HttpRequest.Builder} interface as well as through
 * the built {@link HttpRequest}.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_DEFAULT_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public interface HttpRequestFactory
{
    /**
     * Builds a request, customizing it with the {@link #onInitialize(HttpRequest.Builder)} and
     * {@link #onInitialize(HttpRequest)} methods
     *
     * @param uri The URI for the request
     * @return The request
     */
    default HttpRequest build(URI uri)
    {
        var builder = HttpRequest.newBuilder(uri);
        onInitialize(builder);
        var request = builder.build();
        onInitialize(request);
        return request;
    }

    /**
     * Sets the given header value in the request
     *
     * @param request The HTTP request
     * @param key The header key
     * @param value The header value
     */
    default void header(HttpRequest request, String key, String value)
    {
        request.headers().map().put(key, List.of(value));
    }

    /**
     * Allows customization of the given {@link HttpRequest.Builder}
     *
     * @param builder The builder
     */
    default void onInitialize(HttpRequest.Builder builder)
    {
    }

    /**
     * Allows initialization of the request, such as populating header values
     *
     * @param request The request
     */
    default void onInitialize(HttpRequest request)
    {
    }
}
