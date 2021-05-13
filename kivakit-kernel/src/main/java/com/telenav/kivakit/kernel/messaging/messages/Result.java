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

package com.telenav.kivakit.kernel.messaging.messages;

import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessaging;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.function.BiFunction;

import static com.telenav.kivakit.kernel.messaging.messages.MessageFormatter.Format.WITH_EXCEPTION;

/**
 * A return value that contains an object OR a {@link Message} indicating that something went wrong. Success messages
 * should not be returned with this object as the {@link #failed()} method only tests for the existence of any message.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessaging.class)
@UmlRelation(label = "failure reason", referent = Message.class)
public class Result<T>
{
    public static <T> Result<T> failed(final Message message)
    {
        return new Result<T>().message(message);
    }

    public static <T> Result<T> failed(final Throwable cause, final String message, final Object... arguments)
    {
        return new Result<T>().problem(cause, message, arguments);
    }

    public static <T> Result<T> failed(final String message, final Object... arguments)
    {
        return new Result<T>().problem(message, arguments);
    }

    public static <T> Result<T> failed(final Result<?> result, final String message, final Object... arguments)
    {
        return new Result<T>().problem(result + " => " + message, arguments);
    }

    public static <T> Result<T> succeeded(final T value)
    {
        return new Result<T>().set(value);
    }

    private T object;

    private Message message;

    /**
     * @return True if this result represents failure. Call {@link #why()} to find out the reason.
     */
    @KivaKitIncludeProperty
    public boolean failed()
    {
        return message != null;
    }

    /**
     * @return If {@link #succeeded()} returns true, this method returns the result object
     */
    @KivaKitIncludeProperty
    public T get()
    {
        return object;
    }

    /**
     * @return True if this result has a value
     */
    public boolean has()
    {
        return object != null;
    }

    /**
     * Assigns the given message as a reason for failure
     */
    public Result<T> problem(final String message, final Object... arguments)
    {
        return new Result<T>().message(new Problem(message, arguments));
    }

    /**
     * Assigns the given message as a reason for failure
     */
    public Result<T> problem(final Throwable cause, final String message, final Object... arguments)
    {
        return new Result<T>().message(new Problem(cause, message, arguments));
    }

    /**
     * Sets the given result value
     */
    public Result<T> set(final T result)
    {
        object = result;
        return this;
    }

    /**
     * @return The result is successful
     */
    public boolean succeeded()
    {
        return !failed();
    }

    @Override
    public String toString()
    {
        return succeeded() ? "Succeeded: " + object : message.formatted(WITH_EXCEPTION);
    }

    /**
     * @return If {@link #failed()} is true, this method returns a message that explains why the failure occurred
     */
    @KivaKitIncludeProperty
    public Message why()
    {
        return message;
    }

    public Result<T> with(final Result<T> that, final BiFunction<T, T, T> combiner)
    {
        if (object != null && that.object != null)
        {
            final var value = combiner.apply(object, that.object);
            return new Result<T>().set(value);
        }
        return this;
    }

    private Result<T> message(final Message message)
    {
        this.message = message;
        return this;
    }
}
