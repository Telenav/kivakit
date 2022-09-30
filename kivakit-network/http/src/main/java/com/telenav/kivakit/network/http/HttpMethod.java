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
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public enum HttpMethod
{
    GET,
    POST,
    DELETE,
    HEAD,
    PUT,
    CONNECT,
    OPTIONS,
    TRACE,
    PATCH,
    NONE;

    /**
     * Returns the {@link HttpMethod} for the given text
     *
     * @param listener The listener to notify with any problems
     * @param text The text to parse
     */
    public static HttpMethod parseHttpMethod(Listener listener, String text)
    {
        try
        {
            return valueOf(text.toUpperCase());
        }
        catch (Exception e)
        {
            listener.problem("Invalid HTTP method: $", text);
            return null;
        }
    }
}
