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

import java.net.URI;
import java.net.http.HttpRequest;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Allows customization of an {@link HttpRequest} through the {@link HttpRequest.Builder} interface as well as through
 * the built {@link HttpRequest}.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public interface HttpRequestFactory
{
    /**
     * Builds a request, allowing customization by overriding {@link #onInitialize(HttpRequest.Builder)}
     *
     * @param uri The URI for the request
     * @return The request
     */
    default HttpRequest build(URI uri)
    {
        var builder = HttpRequest.newBuilder(uri);
        builder = onInitialize(builder);
        return builder.build();
    }

    /**
     * Allows initialization of the given {@link HttpRequest.Builder}
     *
     * @param builder The builder
     */
    default HttpRequest.Builder onInitialize(HttpRequest.Builder builder)
    {
        return builder;
    }
}
