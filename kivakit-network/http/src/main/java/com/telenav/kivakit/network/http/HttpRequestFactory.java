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

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

/**
 * Allows customization of an {@link HttpRequest} through the {@link HttpRequest.Builder} interface as well as through
 * the built {@link HttpRequest}.
 *
 * @author jonathanl (shibo)
 */
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

    default void header(HttpRequest request, String name, String value)
    {
        request.headers().map().put(name, List.of(value));
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
