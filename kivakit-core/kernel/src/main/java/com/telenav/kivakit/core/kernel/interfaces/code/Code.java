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

package com.telenav.kivakit.core.kernel.interfaces.code;

import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Listener;

/**
 * Code that can be executed, returning a value. Methods are provided that provide easy exception handling. For example
 * the code below executes the doIt() method and logs a warning and returns the default value <i>false</i> if the code
 * throws an exception. This design allows the code to handle the checked exception in a succinct and readable way.
 * <pre>
 * boolean doIt() throws NumberFormatException
 * {
 *     [...]
 * }
 *
 *    [...]
 *
 * return Code.of(() -&gt; doIt()).ifThrows(false, "Unable to do it");
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
public interface Code<Value>
{
    Logger LOGGER = LoggerFactory.newLogger();

    static <T> Code<T> of(final Code<T> code)
    {
        return code;
    }

    static <T> Code<T> of(final Runnable code)
    {
        return of(() ->
        {
            code.run();
            return null;
        });
    }

    /**
     * @return The value returned by the code, or a default value if an exception is thrown
     */
    default Value or(final Value defaultValue)
    {
        try
        {
            return run();
        }
        catch (final Exception ignored)
        {
            return defaultValue;
        }
    }

    /**
     * @param defaultValue A default value to return if the code throws an exception
     * @param message A warning message to give to the listener if an exception is thrown
     * @param arguments Arguments to interpolate into the message
     * @return The value returned by the code, or the given default value if an exception is thrown
     */
    default Value or(final Value defaultValue, final String message, final Object... arguments)
    {
        return or(defaultValue, LOGGER, message, arguments);
    }

    /**
     * @param defaultValue A default value to return if the code throws an exception
     * @param listener A listener to broadcast a warning message to if an exception is thrown
     * @param message A warning message to give to the listener if an exception is thrown
     * @param arguments Arguments to interpolate into the message
     * @return The value returned by the code, or the given default value if an exception is thrown
     */
    default Value or(final Value defaultValue, final Listener listener, final String message, final Object... arguments)
    {
        try
        {
            return run();
        }
        catch (final Exception e)
        {
            listener.warning(e, message, arguments);
            return defaultValue;
        }
    }

    default Value orNull()
    {
        return or(null);
    }

    default void quietly()
    {
        or(null);
    }

    /**
     * @return Executes the code
     */
    Value run() throws Exception;
}
