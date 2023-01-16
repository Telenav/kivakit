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
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.messages.MessageException;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
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
    NULL_HTTP_METHOD;

    /**
     * Returns the {@link HttpMethod} for the given text
     *
     * @param text The text to parse
     * @return The HTTP method
     * @throws MessageException Thrown if the method cannot be parsed
     */
    public static HttpMethod httpMethod(String text)
    {
        return parseHttpMethod(throwingListener(), text);
    }

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
