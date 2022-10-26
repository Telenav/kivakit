package com.telenav.kivakit.core.function;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.code.UncheckedVoidCode;
import com.telenav.kivakit.core.function.arities.PentaFunction;
import com.telenav.kivakit.core.function.arities.TetraFunction;
import com.telenav.kivakit.core.function.arities.TriFunction;
import com.telenav.kivakit.core.language.trait.TryCatchTrait;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.interfaces.function.Presence;
import com.telenav.kivakit.interfaces.value.Source;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * A substitute for {@link Optional} that adds functionality and integrates with {@link Repeater}
 *
 * <p><b>Creation</b></p>
 *
 * <p>
 * A {@link Maybe} can be constructed in three ways:
 *
 * <ol>
 *     <li>{@link #absent()} - Creates a {@link Maybe} whose value is missing</li>
 *     <li>{@link #maybe(Object)} - Creates a {@link Maybe} whose value can be missing or not</li>
 *     <li>{@link #present(Object)} - Creates a {@link Maybe} whose value is always non-null</li>
 * </ol>
 *
 * <p><b>Terminal Operations</b></p>
 *
 * <ul>
 *     <li>{@link #isPresent()} - Returns true if a value is present and it is not the boolean value <i>false</i></li>
 *     <li>{@link #isAbsent()} - Returns true if no value is present</li>
 *     <li>{@link #get()} - Any value that might be present, or null if no value is present</li>
 *     <li>{@link #has()} - Returns true if a value is present</li>
 *     <li>{@link #orMaybe(Object)} - Returns this value, or the default maybe value</li>
 *     <li>{@link #orDefaultTo(Object)} - Returns this value, or the default value</li>
 *     <li>{@link #orDefaultTo(Source)} - Returns this value, or the default value</li>
 *     <li>{@link #orThrow(String, Object...)} - Returns this value or throws an exception</li>
 *     <li>{@link #orThrow()}- Returns this value or throws an exception</li>
 *     <li>{@link #asStream()} - Converts this value to a stream with zero or one element(s)</li>
 * </ul>
 *
 * <p><b>Functions</b></p>
 *
 * <ul>
 *     <li>{@link #apply(Function)} - Applies the given function to this value</li>
 *     <li>{@link #then(Function)} - Returns the result of applying the given function to this value</li>
 *     <li>{@link #map(BiFunction, Object)} - Returns the result of applying the given two-argument function to this value and the given argument</li>
 *     <li>{@link #map(TriFunction, Object, Object)} - Returns the result of applying the given three-argument function to this value and the given arguments</li>
 *     <li>{@link #map(TetraFunction, Object, Object, Object)} - Returns the result of applying the given four-argument function to this value and the given arguments</li>
 *     <li>{@link #map(PentaFunction, Object, Object, Object, Object)} - Returns the result of applying the given five-argument function to this value and the given arguments</li>
 *     <li>{@link #map(Function)} - Applies the given function to this value</li>
 *     <li>{@link #thenRetry(Count, Source)}  - Retries the given source up to the given maximum number of times or until source returns a non-null value</li>
 * </ul>
 *
 * <p><b>Conditionals</b></p>
 *
 * <ul>
 *     <li>{@link #absentIf(Function)} - Applies the given function to this value, returning this value if it is true, or {@link #absent()} if it is false</li>
 *     <li>{@link #presentIf(Function)} - Applies the given function to this value, returning this value if it is true, or {@link #absent()} if it is false</li>
 *     <li>{@link #ifPresent(Consumer)} - Calls the given consumer if a value is present</li>
 *     <li>{@link #ifPresentOr(Consumer, UncheckedVoidCode)} - Calls the given consumer if a value is present, otherwise calls the given code</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @author viniciusluisr
 * @see <a href="https://github.com/viniciusluisr/improved-optional">improved-optional</a>
 */
@SuppressWarnings("unused") @CodeQuality(stability = STABLE_EXTENSIBLE,
                                         testing = TESTING_INSUFFICIENT,
                                         documentation = DOCUMENTATION_COMPLETE)
public class Maybe<Value> implements
        Presence,
        TryCatchTrait,
        Source<Value>
{
    /**
     * Returns maybe value for null
     */
    public static <Value> Maybe<Value> absent()
    {
        return new Maybe<>();
    }

    /**
     * Returns maybe for the given (null or non-null) value
     */
    public static <Value> Maybe<Value> maybe(Value value)
    {
        return value == null ? absent() : new Maybe<>(ensureNotNull(value));
    }

    /**
     * Returns maybe for the given non-null value
     */
    public static <Value> Maybe<Value> present(Value value)
    {
        return new Maybe<>(ensureNotNull(value));
    }

    /** The null or non-null value */
    private Value value;

    protected Maybe(Value value)
    {
        this.value = value;
    }

    protected Maybe(Maybe<Value> value)
    {
        this.value = value.get();
    }

    protected Maybe()
    {
        value = null;
    }

    /**
     * If this value is not null and the given predicate evaluates to true for the value, returns {@link #absent()},
     * otherwise returns this value.
     *
     * @param predicate The predicate to test any non-null value
     * @return This value or a null value
     */
    public Maybe<Value> absentIf(Function<Value, Boolean> predicate)
    {
        return tryCatch(() -> value != null && ensureNotNull(predicate).apply(value)
                ? newAbsent()
                : this);
    }

    /**
     * <p>
     * If a value is present, uses the given function to map the value from Value to Maybe&lt;ResultType&gt;. If no
     * value is present, returns {@link #absent()}. The effect is that of allowing functions to be chained together in a
     * nested structure where each nested function introduces a new lambda variable that is visible to inner functions.
     * For example:
     * </p>
     *
     * <pre>
     * var seven = present(7);
     * var three = present(3);
     *
     * var sum = seven.apply(a ->
     *               three.apply(b ->
     *                   present(a + b)))
     *           .get();</pre>
     *
     * <p>
     * In this example, if either <i>seven</i> or <i>three</i> had no value present (or both values were absent), the
     * statement would return {@link #absent()}. Only if both values are present will the statement result in the sum of
     * the two values. <i>This allows us to stop thinking about the special case where one or both values are
     * missing.</i>
     * </p>
     *
     * <p><b>Monadic Terminology</b></p>
     *
     * <p>
     * Note that this method is sometimes referred to as <i>flatMap</i>. However, the name 'flatMap' does not accurately
     * describe what this method does, as it only describes what happens when values are collections.
     * </p>
     *
     * <p><b>Important Note</b></p>
     *
     * <p>
     * Notice that the first sentence of the description for this method is exactly the same as that provided for
     * {@link #map(Function)}. The difference between the two methods is that the function passed to this method
     * produces Maybe&lt;ResultType&gt; directly (which allows function nesting), while the function passed to
     * {@link #map(Function)} just maps the value to the ResultType type, which is then wrapped in a {@link Maybe}.
     * </p>
     *
     * @param function A function mapping from Value to Maybe&lt;ResultType&gt;
     * @param <Output> The type being mapped to
     * @return The mapped value as a {@link Maybe} object, or {@link #absent()} if no value was present
     */
    @SuppressWarnings("unchecked")
    public <Output> Maybe<Output> apply(Function<? super Value, ? extends Maybe<? extends Output>> function)
    {
        return tryCatchDefault(() -> isPresent()
                ? ensureNotNull((Maybe<Output>) function.apply(value))
                : newAbsent(), newAbsent());
    }

    /**
     * If this value is not null, returns the value as a {@link Stream}, otherwise returns an empty {@link Stream}.
     *
     * @return This value as a {@link Stream}
     */
    public Stream<Value> asStream()
    {
        return isPresent() ? Stream.of(value) : Stream.empty();
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Maybe<?> that)
        {
            return Objects.equals(value, that.value);
        }
        return false;
    }

    /**
     * Returns any value that might be present, or null if there is none
     */
    @Override
    public Value get()
    {
        return value;
    }

    /**
     * Returns true if a value is present
     */
    public boolean has()
    {
        return isPresent();
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(value);
    }

    /**
     * If this value is not null, calls the given consumer with the value
     *
     * @param consumer The consumer for any non-null value
     * @return This {@link Maybe} for chaining
     */
    public Maybe<Value> ifPresent(Consumer<Value> consumer)
    {
        if (isPresent())
        {
            tryCatch(() -> consumer.accept(value));
        }

        return this;
    }

    /**
     * If a value is present, calls the given consumer, otherwise runs the given block of code
     *
     * @param consumer The consumer to call
     * @param runnable The code to run
     * @return This value for chaining
     */
    public Maybe<Value> ifPresentOr(Consumer<Value> consumer, UncheckedVoidCode runnable)
    {
        if (isPresent())
        {
            tryCatch(() -> consumer.accept(value));
        }
        else
        {
            tryCatch(runnable);
        }

        return this;
    }

    /**
     * Returns true if there is no value present
     */
    @Override
    public boolean isAbsent()
    {
        return value == null;
    }

    /**
     * Returns true if there is a value present and it is not {@link Boolean#FALSE}. The check for false allows false
     * monads to be treated as if they were null.
     */
    @Override
    public boolean isPresent()
    {
        if (value instanceof Boolean)
        {
            return (Boolean) value;
        }
        return value != null;
    }

    /**
     * Returns true if this object is valid
     */
    public boolean isValid()
    {
        return true;
    }

    /**
     * If a value is present, uses the given function to map the value from Value to ResultType. The mapped value is
     * then wrapped in a Maybe&lt;ResultType&gt; object and returned. If no value is present, returns {@link #absent()}.
     * The effect is that of simply mapping this optional value to a new type.
     *
     * <p><b>Important Note</b></p>
     *
     * <p>
     * Notice that the first sentence of the description for this method is exactly the same as that provided for
     * {@link #apply(Function)}. The difference between the two methods is that the function passed to
     * {@link #apply(Function)} produces Maybe&lt;ResultType&gt; directly (which allows function nesting), while the
     * function passed to this method just maps the value to the ResultType type, which is then wrapped in a
     * {@link Maybe}.
     * </p>
     *
     * @param mapper A function mapping from Value to ResultType
     * @param <ResultType> The type that Value is being mapped to
     * @return The mapped value or {@link #absent()}
     */
    public <ResultType> Maybe<ResultType> map(
            Function<? super Value, ? extends ResultType> mapper)
    {
        return tryCatchDefault(() -> isPresent()
                ? newMaybe(ensureNotNull(mapper).apply(value))
                : newAbsent(), newAbsent());
    }

    /**
     * If a value is present and the given value is not null, applies the given bi-function to produce a result of a
     * different type.
     *
     * @param function The bi-function, f(Value, Argument2) -> ResultType
     * @param argument2 The second argument to pass to the bi-function, along with this value as the first argument
     * @return The combination of this value and the given argument, if both values are non-null, otherwise, returns
     * {@link #absent()}.
     */
    public <Argument2, ResultType> Maybe<ResultType> map(
            BiFunction<Value, Argument2, ResultType> function,
            Argument2 argument2)
    {
        if (isPresent() && value != null)
        {
            return tryCatch(() -> newMaybe(function.apply(value, argument2)));
        }

        return newAbsent();
    }

    /**
     * If a value is present, and the given arguments are non-null, applies the given tri-function to produce a result
     * of a different type.
     *
     * @param function The tri-function, f(Value, Argument2, Argument3) -> ResultType
     * @param argument2 The second argument to pass to the tri-function, along with this value as the first argument
     * @param argument3 The third argument to pass to the tri-function
     * @return The combination of this value and the given arguments, if all arguments are non-null, otherwise, returns
     * {@link #absent()}.
     */
    public <Argument2, Argument3, ResultType> Maybe<ResultType> map(
            TriFunction<Value, Argument2, Argument3, ResultType> function,
            Argument2 argument2,
            Argument3 argument3)
    {
        if (isPresent() && argument2 != null && argument3 != null)
        {
            return tryCatch(() -> newMaybe(function.apply(this.value, argument2, argument3)));
        }

        return newAbsent();
    }

    /**
     * If a value is present, and the given value is also present, applies the given tri-function to produce a result of
     * a different type.
     *
     * @param function The combining tetra-function, f(Value, Argument2, Argument3, Argument4) -> ResultType
     * @param argument2 The second argument to pass to the quad-function, along with this value as the first argument
     * @param argument3 The third argument to pass to the quad-function along with this value
     * @param argument4 The fourth argument to pass to the quad-function along with this value
     * @return The combination of this value and the given arguments, if all arguments are non-null, otherwise, returns
     * {@link #absent()}.
     */
    public <Argument2, Argument3, Argument4, ResultType> Maybe<ResultType> map(
            TetraFunction<Value, Argument2, Argument3, Argument4, ResultType> function,
            Argument2 argument2,
            Argument3 argument3,
            Argument4 argument4)
    {
        if (isPresent() && argument2 != null && argument3 != null && argument4 != null)
        {
            return tryCatch(() -> newMaybe(function.apply(this.value, argument2, argument3, argument4)));
        }

        return newAbsent();
    }

    /**
     * If a value is present, and the given value is also present, applies the given five-argument function to produce a
     * result of a different type.
     *
     * @param function The five-argument function, f(Value, Argument2, Argument3, Argument4, Argument5) -> ResultType
     * @param argument2 The second argument to pass to the quad-function, along with this value as the first argument
     * @param argument3 The third argument to pass to the quad-function along with this value
     * @param argument4 The fourth argument to pass to the quad-function along with this value
     * @param argument5 The fifth argument to pass to the quad-function along with this value
     * @return The combination of this value and the given arguments, if all arguments are non-null, otherwise, returns
     * {@link #absent()}.
     */
    public <Argument2, Argument3, Argument4, Argument5, ResultType> Maybe<ResultType> map(
            PentaFunction<Value, Argument2, Argument3, Argument4, Argument5, ResultType> function,
            Argument2 argument2,
            Argument3 argument3,
            Argument4 argument4,
            Argument5 argument5)
    {
        if (isPresent() && argument2 != null && argument3 != null && argument4 != null && argument5 != null)
        {
            return tryCatch(() -> newMaybe(function.apply(this.value, argument2, argument3, argument4, argument5)));
        }

        return newAbsent();
    }

    /**
     * If a value is present, retries the given value mapper up to the given maximum number of times or until the mapper
     * returns a non-null value. If no mapped value can be produced, returns {@link #absent()}.
     *
     * @param retries The number of times to retry
     * @param mapper The mapping function
     * @return This value or the value produced by the given source
     */
    public <ResultType> Maybe<ResultType> mapWithRetries(Count retries,
                                                         Function<? super Value, ? extends ResultType> mapper)
    {
        if (isPresent())
        {
            retries.loop(() ->
            {
                Maybe<ResultType> maybe = tryCatch(() ->
                {
                    var mapped = ensureNotNull(mapper).apply(value);
                    if (mapped != null)
                    {
                        return newMaybe(mapped);
                    }
                    return absent();
                });
            });
        }
        return absent();
    }

    /**
     * If there is a value present, returns it, otherwise returns the given default value
     *
     * @param defaultValue The default value to return if there is no value
     * @return The value
     */
    public Value orDefaultTo(Value defaultValue)
    {
        return tryCatch(() -> isPresent()
                ? value
                : defaultValue);
    }

    /**
     * If there is a value present, returns it, otherwise returns the given default value
     *
     * @param defaultValue The default value to return if there is no value
     * @return The value
     */
    public Value orDefaultTo(Source<Value> defaultValue)
    {
        return tryCatch(() -> isPresent()
                ? value
                : defaultValue.get());
    }

    /**
     * If there is a value present, returns this, otherwise returns the value provided by the given {@link Source}
     *
     * @param source A source of a value, if there is no value present
     * @return This value or the value produced by the given source
     */
    public Maybe<Value> orMaybe(Source<Value> source)
    {
        return tryCatch(() -> isPresent()
                ? this
                : newMaybe(ensureNotNull(source).get()));
    }

    /**
     * If there is a value present, returns this, otherwise returns the value provided by the given {@link Source}
     *
     * @param value A source of a {@link Maybe} if there is no value present
     * @return This value or the value produced by the given source
     */
    public Maybe<Value> orMaybe(Value value)
    {
        return tryCatch(() -> isPresent()
                ? this
                : newMaybe(value));
    }

    /**
     * Throws an exception if there is no value, otherwise returns the value
     *
     * @return The value
     */
    public Value orThrow()
    {
        return orThrow("No value present");
    }

    /**
     * Throws an exception if there is no value, otherwise returns the value
     *
     * @param message The exception message
     * @param arguments The message arguments
     * @return The value
     */
    public Value orThrow(String message, Object... arguments)
    {
        if (isAbsent())
        {
            new Problem(message, arguments).throwMessage();
        }

        return value;
    }

    /**
     * If this value is not null and the given predicate evaluates to true for the value, returns this value, otherwise
     * returns the {@link #absent()} value.
     *
     * @param predicate The predicate to test any non-null value
     * @return This value or a null value
     */
    public Maybe<Value> presentIf(Function<Value, Boolean> predicate)
    {
        return tryCatch(() -> value != null && ensureNotNull(predicate).apply(value)
                ? this
                : newAbsent());
    }

    /**
     * Sets the given result value
     */
    public Maybe<Value> set(Value value)
    {
        this.value = value;

        return this;
    }

    /**
     * If a value is present, and the given value is also present, applies the given five-argument function to produce a
     * new value of the same type.
     *
     * @param function The combining five-argument function, f(Value, Argument2, Argument3, Argument4, Argument5) ->
     * Value
     * @param argument2 The second argument to pass to the quad-function, along with this value as the first argument
     * @param argument3 The third argument to pass to the quad-function along with this value
     * @param argument4 The fourth argument to pass to the quad-function along with this value
     * @param argument5 The fifth argument to pass to the quad-function along with this value
     * @return The combination of this value and the given arguments, if all arguments are non-null, otherwise, returns
     * {@link #absent()}.
     */
    public <Argument2, Argument3, Argument4, Argument5> Maybe<Value> then(
            PentaFunction<Value, Argument2, Argument3, Argument4, Argument5, Value> function,
            Argument2 argument2,
            Argument3 argument3,
            Argument4 argument4,
            Argument5 argument5)
    {
        if (isPresent() && argument2 != null && argument3 != null && argument4 != null && argument5 != null)
        {
            return tryCatch(() -> newMaybe(function.apply(this.value, argument2, argument3, argument4, argument5)));
        }

        return newAbsent();
    }

    /**
     * If a value is present, and the given value is also present, applies the given tri-function to produce a new value
     * of the same type.
     *
     * @param function The combining tetra-function, f(Value, Argument2, Argument3, Argument4) -> ResultType
     * @param argument2 The second argument to pass to the quad-function, along with this value as the first argument
     * @param argument3 The third argument to pass to the quad-function along with this value
     * @param argument4 The fourth argument to pass to the quad-function along with this value
     * @return The combination of this value and the given arguments. if all arguments are non-null, otherwise, returns
     * {@link #absent()}.
     */
    public <Argument2, Argument3, Argument4> Maybe<Value> then(
            TetraFunction<Value, Argument2, Argument3, Argument4, Value> function,
            Argument2 argument2,
            Argument3 argument3,
            Argument4 argument4)
    {
        if (isPresent() && argument2 != null && argument3 != null && argument4 != null)
        {
            return tryCatch(() -> newMaybe(function.apply(this.value, argument2, argument3, argument4)));
        }

        return newAbsent();
    }

    /**
     * If a value is present, and the given arguments are non-null, applies the given tri-function to produce a result
     * of the same type.
     *
     * @param function The combining tri-function, f(Value, Argument2, Argument3) -> ResultType
     * @param argument2 The second argument to pass to the tri-function, along with this value as the first argument
     * @param argument3 The third argument to pass to the tri-function
     * @return The combination of this value and the given arguments, if all arguments are non-null, otherwise, returns
     * {@link #absent()}.
     */
    public <Argument2, Argument3> Maybe<Value> then(
            TriFunction<Value, Argument2, Argument3, Value> function,
            Argument2 argument2,
            Argument3 argument3)
    {
        if (isPresent() && argument2 != null && argument3 != null)
        {
            return tryCatch(() -> newMaybe(function.apply(this.value, argument2, argument3)));
        }

        return newAbsent();
    }

    /**
     * If a value is present and the given value is not null, applies the given bi-function to produce a result of the
     * same type.
     *
     * @param function The combining bi-function, f(Value, Argument2) -> Value
     * @param argument2 The second argument to pass to the bi-function, along with this value as the first argument
     * @return The combination of this value and the given argument, if both values are non-null, otherwise, returns
     * {@link #absent()}.
     */
    public <Argument2> Maybe<Value> then(
            BiFunction<Value, Argument2, Value> function,
            Argument2 argument2)
    {
        if (isPresent() && value != null)
        {
            return tryCatch(() -> newMaybe(function.apply(value, argument2)));
        }

        return newAbsent();
    }

    /**
     * If a value is present, applies the given function to produce a new value of the same type.
     *
     * @param function The function to apply
     * @return The value produced by the given function when applied to this value
     */
    public Maybe<Value> then(Function<Value, Value> function)
    {
        return map(function);
    }

    @Override
    public String toString()
    {
        return "[Maybe value = " + value + "]";
    }

    /**
     * Overridden in {@link Result} to return the right subclass
     */
    protected <T> Maybe<T> newAbsent()
    {
        return new Maybe<>();
    }

    /**
     * Overridden in {@link Result} to return the right subclass
     */
    protected <T> Maybe<T> newMaybe(T value)
    {
        return new Maybe<>(value);
    }
}
