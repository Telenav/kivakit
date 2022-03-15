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

package com.telenav.kivakit.core.function;

import com.telenav.kivakit.core.code.UncheckedVoidCode;
import com.telenav.kivakit.core.function.arities.PentaFunction;
import com.telenav.kivakit.core.function.arities.TetraFunction;
import com.telenav.kivakit.core.function.arities.TriFunction;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.core.lexakai.DiagramMessaging;
import com.telenav.kivakit.core.test.Tested;
import com.telenav.kivakit.interfaces.code.Code;
import com.telenav.kivakit.interfaces.function.BooleanFunction;
import com.telenav.kivakit.interfaces.string.StringMapper;
import com.telenav.kivakit.interfaces.value.Source;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Represents the result of an operation, capturing any failure {@link #messages()}. If there are no failure messages,
 * the operation was successful and {@link #get()} provides the value of the operation. Note that a {@link Result} is a
 * subclass of {@link Maybe}. The list of methods inherited from {@link Maybe} are duplicated here for convenience.
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Construction</b></p>
 *
 * <p>
 * A {@link Result} can be constructed in several ways:
 *
 * <ol>
 *     <li>{@link #absent()} - A successful {@link Result} with no value</li>
 *     <li>{@link #success(Maybe)} - Creates a {@link Result} with the given value</li>
 *     <li>{@link #success(Object)} - Creates a {@link Result} with the given value</li>
 *     <li>{@link #failure(String, Object...)} - Creates a {@link Result} with the given failure message</li>
 *     <li>{@link #failure(Throwable, String, Object...)} - Creates a {@link Result} with the given failure message</li>
 * </ol>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Validation</b></p>
 *
 * <ul>
 *     <li>{@link #succeeded()} - True if this result represents success</li>
 *     <li>{@link #failed()} - True if this result represents a failure</li>
 *     <li>{@link #isValid()} - True if this result is valid. A valid result must have either a value, or one or more messages, but it cannot have both.</li>
 *     <li>{@link #messages()} - Any captured error messages</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Terminal Operations</b></p>
 *
 * <ul>
 *     <li>{@link #isPresent()} - Returns true if a value is present</li>
 *     <li>{@link #isAbsent()} - Returns true if no value is present</li>
 *     <li>{@link #get()} - Any value that might be present, or null if no value is present</li>
 *     <li>{@link #has()} - Returns true if a value is present</li>
 *     <li>{@link #orMaybe(Object)} - Returns this value, or the default value</li>
 *     <li>{@link #orMaybe(Source)} - Returns this value, or the default value</li>
 *     <li>{@link #orProblem(String, Object[])} - Returns this value or broadcasts a problem if this value is not present</li>
 *     <li>{@link #orThrow(String, Object...)} - Returns this value or throws an exception</li>
 *     <li>{@link #orThrow()}- Returns this value or throws an exception</li>
 *     <li>{@link #asStream()} - Converts this value to a stream with zero or one element(s)</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Functions</b></p>
 *
 * <ul>
 *     <li>{@link #then(Function)} - Returns the result of applying the given function to this value</li>
 *     <li>{@link #then(BiFunction, Object)} - Returns the result of applying the given two-argument function to this value and the given argument</li>
 *     <li>{@link #then(TriFunction, Object, Object)} - Returns the result of applying the given three-argument function to this value and the given arguments</li>
 *     <li>{@link #then(TetraFunction, Object, Object, Object)} - Returns the result of applying the given four-argument function to this value and the given arguments</li>
 *     <li>{@link #then(PentaFunction, Object, Object, Object, Object)} - Returns the result of applying the given five-argument function to this value and the given arguments</li>
 *     <li>{@link #apply(Function)} - Applies the given function to this value</li>
 *     <li>{@link #map(Function)} - Applies the given function to this value</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Conditionals</b></p>
 *
 * <ul>
 *     <li>{@link #presentIf(BooleanFunction)} - Applies the given function to this value, returning this value if it is true, or {@link #absent()} if it is false</li>
 *     <li>{@link #ifPresent(Consumer)} - Calls the given consumer if a value is present</li>
 *     <li>{@link #ifPresentOr(Consumer, UncheckedVoidCode)} - Calls the given consumer if a value is present, otherwise calls the given code</li>
 *     <li>{@link #or(Code)} - If a value is present, returns this value, otherwise returns the {@link Maybe} supplied by the given {@link Code}</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * @author jonathanl (shibo)
 * @see Maybe
 * @see Consumer
 * @see Broadcaster
 * @see BiFunction
 * @see Function
 * @see Code
 * @see Source
 */
@UmlClassDiagram(diagram = DiagramMessaging.class)
@UmlRelation(label = "failure reason", referent = Message.class)
public class Result<Value> extends Maybe<Value> implements RepeaterMixin
{
    /**
     * A {@link Result} with no value
     */
    private static final Result<?> RESULT_ABSENT = new Result<>();

    /**
     * Returns a {@link Result} whose value is absent
     */
    @SuppressWarnings("unchecked")
    public static <Value> Result<Value> absent()
    {
        return (Result<Value>) RESULT_ABSENT;
    }

    /**
     * Returns a result with the given value
     */
    public static <T> Result<T> capture(Broadcaster value)
    {
        return new Result<>(value);
    }

    /**
     * Returns a result with the given {@link Problem}
     */
    public static <T> Result<T> failure(Throwable cause, String message, Object... arguments)
    {
        return failure(new Problem(cause, message, arguments));
    }

    /**
     * Returns a result with the given {@link Problem}
     */
    public static <T> Result<T> failure(String message, Object... arguments)
    {
        return failure(new Problem(message, arguments));
    }

    /**
     * Returns a result with the given {@link Problem}
     */
    public static <T> Result<T> failure(Result<T> result, String message, Object... arguments)
    {
        return failure(new Problem(result + " => " + message, arguments));
    }

    /**
     * Returns a result with the given message
     */
    public static <T> Result<T> failure(Message message)
    {
        var result = new Result<T>();
        result.receive(message);
        return result;
    }

    /**
     * Returns a result with the given value
     */
    public static <T> Result<T> present(T value)
    {
        return unsupported("Call Result.success(), not Result.present()");
    }

    /**
     * Returns the result of executing the given {@link Code}. Captures any value, or any failure messages broadcast by
     * the given broadcaster during the call, and returns a {@link Result}.
     *
     * @return The {@link Result} of the call
     */
    @Tested
    public static <T> Result<T> run(Broadcaster broadcaster, Code<T> code)
    {
        // Create an empty result that captures messages from this object,
        var result = Result.<T>capture(broadcaster);

        try
        {
            // call the code and store any result,
            result.set(code.run());
        }
        catch (Exception e)
        {
            // and if the code throws an exception, store that as a problem.
            result.problem(e, "Operation failed with exception");
        }

        // Return the result of the method call.
        return result;
    }

    /**
     * Returns a {@link Result} for a value that may or may not be present
     */
    public static <Value> Result<Value> success(Maybe<Value> value)
    {
        return new Result<>(value);
    }

    /**
     * Returns a result with the given value
     */
    public static <T> Result<T> success(T value)
    {
        return new Result<>(value);
    }

    /** Any broadcaster to listen to */
    private Broadcaster broadcaster;

    /** Any messages this result has captured */
    private MessageList messages;

    protected Result()
    {
    }

    protected Result(Maybe<Value> maybe)
    {
        super(maybe);
    }

    protected Result(Value value)
    {
        super(value);
    }

    protected Result(Broadcaster broadcaster)
    {
        messages = new MessageList();
        messages.listenTo(broadcaster);
        this.broadcaster = broadcaster;
    }

    /**
     * <p><i>Down-casting override</i></p>
     * <p>
     * {@inheritDoc}
     */
    @Tested
    public <Mapped> Result<Mapped> apply(Function<? super Value, ? extends Maybe<? extends Mapped>> function)
    {
        return (Result<Mapped>) super.apply(function);
    }

    /**
     * If a value is present, converts it to a string and then applies the given {@link StringMapper} class to convert
     * it to a value. The {@link StringMapper} must have a public constructor that takes a {@link Listener}. String
     * converters in the <i>kivakit-converter</i> mini-framework are such {@link StringMapper}s.
     *
     * @param mapperType The {@link StringMapper} class
     * @return A {@link Maybe} object with the mapped value, or {@link #absent()} if the mapping failed
     */
    @Tested
    public <Output, Mapper extends StringMapper<? extends Output>> Maybe<Output> convert(Class<Mapper> mapperType)
    {
        var outer = this;
        return tryCatchDefault(() ->
        {
            if (isPresent())
            {
                var mapper = Classes.newInstance(mapperType, Listener.class, outer);
                return newMaybe(ensureNotNull(mapper).map(get().toString()));
            }
            else
            {
                return newAbsent();
            }
        }, newAbsent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Result<?>)
        {
            var that = (Result<?>) object;
            return super.equals(that) && succeeded() == that.succeeded();
        }
        return false;
    }

    /**
     * Returns true if this result represents a failure
     */
    @Tested
    public boolean failed()
    {
        return messages != null && messages().failed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), succeeded());
    }

    /**
     * <p><i>Down-casting override</i></p>
     * <p>
     * {@inheritDoc}
     */
    @Tested
    public Result<Value> ifPresent(Consumer<Value> consumer)
    {
        return (Result<Value>) super.ifPresent(consumer);
    }

    /**
     * <p><i>Down-casting override</i></p>
     * <p>
     * {@inheritDoc}
     */
    @Tested
    public Result<Value> ifPresentOr(Consumer<Value> consumer, UncheckedVoidCode runnable)
    {
        return (Result<Value>) super.ifPresentOr(consumer, runnable);
    }

    /**
     * @return True if this result is valid. A result is invalid if it has a value present, but also has failure
     * messages. Note that a result can have neither a value nor any failure messages (see {@link #absent()}).
     */
    @Override
    public boolean isValid()
    {
        var invalid = isPresent() && failed();

        return !invalid;
    }

    /**
     * <p><i>Down-casting override</i></p>
     * <p>
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Tested
    public <Output> Result<Output> map(Function<? super Value, ? extends Output> mapper)
    {
        return (Result<Output>) super.map(mapper);
    }

    /**
     * Returns any messages captured in this result
     */
    @Tested
    public MessageList messages()
    {
        // If we were not constructed with a Broadcaster,
        if (messages == null)
        {
            // return an empty message list.
            return MessageList.empty();
        }

        return messages;
    }

    /**
     * If this result failed, runs the given {@link Code}, capturing any messages and result of the call. If the code
     * succeeds, returns the result, otherwise returns this result.
     *
     * @return The {@link Result} of the call
     */
    @Tested
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
     * <p><i>Down-casting override</i></p>
     * <p>
     * {@inheritDoc}
     */
    @Override
    @Tested
    public Result<Value> orMaybe(final Value value)
    {
        return (Result<Value>) super.orMaybe(value);
    }

    /**
     * <p><i>Down-casting override</i></p>
     * <p>
     * {@inheritDoc}
     */
    @Override
    @Tested
    public Result<Value> orMaybe(final Source<Value> source)
    {
        return (Result<Value>) super.orMaybe(source);
    }

    /**
     * Broadcasts a problem if no value is present
     *
     * @return Any value that might be present
     */
    @Tested
    public Value orProblem(String message, Object... arguments)
    {
        if (isAbsent())
        {
            problem(message, arguments);
        }

        return get();
    }

    /**
     * <p><i>Down-casting override</i></p>
     * <p>
     * {@inheritDoc}
     */
    @Tested
    public Result<Value> presentIf(BooleanFunction<Value> predicate)
    {
        return (Result<Value>) super.presentIf(predicate);
    }

    /**
     * Returns true if this result represents success
     */
    @Tested
    public boolean succeeded()
    {
        return messages == null || messages().succeeded();
    }

    /**
     * <p><i>Down-casting override</i></p>
     * <p>
     * {@inheritDoc}
     */
    @Tested
    public <Argument2, R> Result<R> then(BiFunction<Value, Argument2, R> function, Argument2 argument2)
    {
        return (Result<R>) super.then(function, argument2);
    }

    /**
     * <p><i>Down-casting override</i></p>
     * <p>
     * {@inheritDoc}
     */
    @Override
    @Tested
    public <Argument2, Argument3, ResultType> Result<ResultType> then(
            final TriFunction<Value, Argument2, Argument3, ResultType> function, final Argument2 argument2,
            final Argument3 argument3)
    {
        return (Result<ResultType>) super.then(function, argument2, argument3);
    }

    /**
     * <p><i>Down-casting override</i></p>
     * <p>
     * {@inheritDoc}
     */
    @Override
    @Tested
    public <Argument2, Argument3, Argument4, ResultType> Result<ResultType> then(
            final TetraFunction<Value, Argument2, Argument3, Argument4, ResultType> function, final Argument2 argument2,
            final Argument3 argument3, final Argument4 argument4)
    {
        return (Result<ResultType>) super.then(function, argument2, argument3, argument4);
    }

    /**
     * <p><i>Down-casting override</i></p>
     * <p>
     * {@inheritDoc}
     */
    @Override
    @Tested
    public <Argument2, Argument3, Argument4, Argument5, ResultType> Result<ResultType> then(
            final PentaFunction<Value, Argument2, Argument3, Argument4, Argument5, ResultType> function,
            final Argument2 argument2,
            final Argument3 argument3, final Argument4 argument4, final Argument5 argument5)
    {
        return (Result<ResultType>) super.then(function, argument2, argument3, argument4, argument5);
    }

    /**
     * <p><i>Down-casting override</i></p>
     * <p>
     * {@inheritDoc}
     */
    @Tested
    public Result<Value> then(Function<Value, Value> function)
    {
        return (Result<Value>) super.then(function);
    }

    @Override
    public String toString()
    {
        return succeeded() ? "Succeeded: " + get() : "Failed:\n\n" + messages().bulleted(4);
    }

    /**
     * <p><i>Down-casting override</i></p>
     * <p>
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <T> Result<T> newAbsent()
    {
        return (Result<T>) RESULT_ABSENT;
    }

    /**
     * <p><i>Down-casting override</i></p>
     * <p>
     * {@inheritDoc}
     */
    @Override
    protected <T> Result<T> newMaybe(T value)
    {
        return new Result<>(value);
    }
}
