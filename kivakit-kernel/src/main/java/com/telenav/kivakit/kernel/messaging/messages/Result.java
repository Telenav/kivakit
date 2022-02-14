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

package com.telenav.kivakit.kernel.messaging.messages;

import com.telenav.kivakit.kernel.interfaces.code.Code;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.messaging.Broadcaster;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.listeners.MessageList;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessaging;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.function.BiFunction;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;

/**
 * Represents the result of an operation, containing any failure messages in {@link MessageList}. If there are no
 * failure messages, the result is successful and {@link #get()} provides the value of the operation.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessaging.class)
@UmlRelation(label = "failure reason", referent = Message.class)
public class Result<T> extends MessageList
{
    public static <T> Result<T> failed(Message message)
    {
        var result = new Result<T>();
        result.receive(message);
        return result;
    }

    public static <T> Result<T> failed(Throwable cause, String message, Object... arguments)
    {
        return failed(new Problem(cause, message, arguments));
    }

    public static <T> Result<T> failed(String message, Object... arguments)
    {
        return failed(new Problem(message, arguments));
    }

    public static <T> Result<T> failed(Result<?> result, String message, Object... arguments)
    {
        return failed(new Problem(result + " => " + message, arguments));
    }

    public static <T> Result<T> succeeded(T value)
    {
        return new Result<T>().set(value);
    }

    private Broadcaster broadcaster;

    private T value;

    public Result()
    {
    }

    public Result(Broadcaster broadcaster)
    {
        listenTo(broadcaster);
        this.broadcaster = broadcaster;
    }

    /**
     * @return If {@link #succeeded()} returns true, this method returns the result object
     */
    @KivaKitIncludeProperty
    public T get()
    {
        return value;
    }

    /**
     * If this result succeeded, returns the value from {@link #get()}. If it failed, broadcasts a {@link Problem}
     * message and returns null.
     *
     * @return The value of this result or null if this result failed
     */
    public T getOrProblem(String message, Object... arguments)
    {
        if (failed())
        {
            problem(message, arguments);
            return null;
        }
        else
        {
            return get();
        }
    }

    /**
     * If this result succeeded, returns the value from {@link #get()}. If it failed, throws an {@link
     * IllegalStateException}.
     *
     * @return The value of this result
     * @throws IllegalStateException Details of this failed result
     */
    public T getOrThrow()
    {
        if (failed())
        {
            super.ifFailedThrow();
            return null;
        }
        else
        {
            return get();
        }
    }

    /**
     * @return True if this result has a value
     */
    public boolean has()
    {
        return value != null;
    }

    /**
     * @return True if this result has a value and succeeded, or does not have a value and failed. False otherwise.
     */
    public boolean isValid()
    {
        return has() ^ failed();
    }

    /**
     * If this result failed, runs the given {@link Code}, capturing any messages and result of the call. If the code
     * succeeds, returns the result, otherwise returns this result.
     *
     * @return The {@link Result} of the call
     */
    public Result<T> or(Code<T> code)
    {
        // If this result failed,
        if (failed())
        {
            // don't listen to the broadcaster further,
            broadcaster.removeListener(this);

            // create a new result to listens to it,
            var result = new Result<T>(broadcaster);

            try
            {
                // call the code and store any result,
                result.set(code.run());
            }
            catch (Exception e)
            {
                // and if the code throws an exception, store that as a problem.
                problem(e, "Failed");
            }

            return result;
        }

        return this;
    }

    /**
     * Sets the given result value
     */
    public Result<T> set(T result)
    {
        value = result;

        ensure(isValid());

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
        return succeeded() ? "Succeeded: " + value : "Failed:\n\n" + bulleted(4);
    }

    public Result<T> with(Result<T> that, BiFunction<T, T, T> combiner)
    {
        if (value != null && that.value != null)
        {
            var value = combiner.apply(this.value, that.value);
            return new Result<T>().set(value);
        }
        return this;
    }
}
