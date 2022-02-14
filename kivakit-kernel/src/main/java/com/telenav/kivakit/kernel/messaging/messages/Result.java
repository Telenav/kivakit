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
import com.telenav.kivakit.kernel.interfaces.function.BooleanFunction;
import com.telenav.kivakit.kernel.interfaces.value.Source;
import com.telenav.kivakit.kernel.language.monads.ElseFunction;
import com.telenav.kivakit.kernel.language.monads.Maybe;
import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.messaging.Broadcaster;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.listeners.MessageList;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessaging;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.telenav.kivakit.kernel.language.monads.Maybe.maybe;

/**
 * Represents the result of an operation, containing any failure messages in {@link MessageList}. If there are no
 * failure messages, the operation was successful and {@link #value()} provides the value of the operation.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessaging.class)
@UmlRelation(label = "failure reason", referent = Message.class)
public class Result<Value> extends MessageList
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

    private Maybe<Value> value;

    public Result()
    {
    }

    public Result(Broadcaster broadcaster)
    {
        listenTo(broadcaster);

        this.broadcaster = broadcaster;
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public Maybe<Value> apply(Maybe<Value> that, BiFunction<Value, Value, Value> function)
    {
        return value.apply(function, that);
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public Maybe<Value> apply(Function<Value, Value> function)
    {
        return value.apply(function);
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public Stream<Value> asStream()
    {
        return value.asStream();
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public boolean equals(Object object)
    {
        if (object instanceof Result)
        {
            var that = (Result<?>) object;
            return Objects.equals(value, that.value);
        }
        return false;
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public <Mapped> Maybe<Mapped> flatMap(Function<? super Value, ? extends Maybe<? extends Mapped>> mapper)
    {
        return value.flatMap(mapper);
    }

    /**
     * @return The value for this result, or null if there is none
     */
    @KivaKitIncludeProperty
    public Value get()
    {
        return value.get();
    }

    /**
     * If this result succeeded, returns the value from {@link #get()}. If it failed, broadcasts a {@link Problem}
     * message and returns null.
     *
     * @return The value of this result or null if this result failed
     */
    public Maybe<Value> getOrProblem(String message, Object... arguments)
    {
        if (failed())
        {
            problem(message, arguments);
            return Maybe.empty();
        }
        else
        {
            return value();
        }
    }

    /**
     * If this result succeeded, returns the value from {@link #get()}. If it failed, throws an {@link
     * IllegalStateException}.
     *
     * @return The value of this result
     * @throws IllegalStateException Details of this failed result
     */
    public Maybe<Value> getOrThrow()
    {
        if (failed())
        {
            super.ifFailedThrow();
            return null;
        }
        else
        {
            return value();
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
     * <i>Delegates to {@link Maybe}</i>
     */
    @Override
    public int hashCode()
    {
        return Hash.code(value);
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public Maybe<Value> ifFalse(BooleanFunction<Value> predicate)
    {
        return value.ifFalse(predicate);
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public Maybe<Value> ifNonEmptyOr(Consumer<Value> consumer, Runnable runnable)
    {
        return value.ifPresentOr(consumer, runnable);
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public ElseFunction<Runnable, ElseFunction<Consumer<Value>, Void>> ifNull()
    {
        return value.ifEmpty();
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public Maybe<Value> ifPresent(Consumer<Value> consumer)
    {
        return value.ifPresent(consumer);
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public ElseFunction<Consumer<Value>, ElseFunction<Runnable, Void>> ifPresent()
    {
        return value.ifPresent();
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public Maybe<Value> ifTrue(BooleanFunction<Value> predicate)
    {
        return value.ifTrue(predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty()
    {
        return value.isEmpty();
    }

    /**
     * @return True if this result has a value and succeeded, or does not have a value and failed. False otherwise.
     */
    public boolean isValid()
    {
        return has() ^ failed();
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public <Output> Maybe<Output> map(Function<? super Value, ? extends Output> mapper)
    {
        return value.map(mapper);
    }

    /**
     * If this result failed, runs the given {@link Code}, capturing any messages and result of the call. If the code
     * succeeds, returns the result, otherwise returns this result.
     *
     * @return The {@link Result} of the call
     */
    public Result<Value> or(Code<Value> code)
    {
        // If this result failed,
        if (failed())
        {
            // don't listen to the broadcaster further,
            broadcaster.removeListener(this);

            // create a new result to listens to it,
            var result = new Result<Value>(broadcaster);

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
     * <i>Delegates to {@link Maybe}</i>
     */
    public Maybe<Value> or(Source<? extends Maybe<? extends Value>> supplier)
    {
        return value.or(supplier);
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public Value orElse(Value defaultValue)
    {
        return value.orDefault(defaultValue);
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public Value orElse(Source<Value> defaultValue)
    {
        return value.orDefault(defaultValue);
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public Value orElseProblem(String message, Object... arguments)
    {
        return value.orProblem(message, arguments);
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public Value orElseThrow()
    {
        return value.orThrow();
    }

    /**
     * <i>Delegates to {@link Maybe}</i>
     */
    public Value orElseThrow(String message, Object... arguments)
    {
        return value.orThrow(message, arguments);
    }

    /**
     * Sets the given result value
     */
    public Result<Value> set(Value result)
    {
        return set(maybe(result));
    }

    /**
     * Sets the given result value
     */
    public Result<Value> set(Maybe<Value> result)
    {
        value = result;

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

    /**
     * Returns this result's value as a {@link Maybe}.
     *
     * @return The {@link Maybe} object containing any value for this result
     */
    public Maybe<Value> value()
    {
        return value;
    }
}
