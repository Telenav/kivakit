package com.telenav.kivakit.core.function;

import com.telenav.kivakit.core.code.UncheckedVoidCode;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.test.Tested;
import com.telenav.kivakit.interfaces.function.BooleanFunction;
import com.telenav.kivakit.interfaces.monads.Presence;
import com.telenav.kivakit.interfaces.value.Source;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * A substitute for {@link Optional} that adds functionality and integrates with {@link Repeater}
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Construction</b></p>
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
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Terminal Operations</b></p>
 *
 * <ul>
 *     <li>{@link #isPresent()} - Returns true if a value is present</li>
 *     <li>{@link #isAbsent()} - Returns true if no value is present</li>
 *     <li>{@link #get()} - Any value that might be present, or null if no value is present</li>
 *     <li>{@link #has()} - Returns true if a value is present</li>
 *     <li>{@link #orMaybe(Object)} - Returns this value, or the default maybe value</li>
 *     <li>{@link #orDefaultTo(Object)} - Returns this value, or the default value</li>
 *     <li>{@link #orDefaultTo(Source)} - Returns this value, or the default value</li>
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
 *     <li>{@link #apply(Function)} - Applies the given function to this value</li>
 *     <li>{@link #then(Function)} - Returns the result of applying the given function to this value</li>
 *     <li>{@link #then(BiFunction, Maybe)} - Returns the result of applying the given two-argument function to this value and the given value</li>
 *     <li>{@link #map(Function)} - Applies the given function to this value</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Conditionals</b></p>
 *
 * <ul>
 *     <li>{@link #absentIf(BooleanFunction)} - Applies the given function to this value, returning this value if it is true, or {@link #absent()} if it is false</li>
 *     <li>{@link #presentIf(BooleanFunction)} - Applies the given function to this value, returning this value if it is true, or {@link #absent()} if it is false</li>
 *     <li>{@link #ifPresent(Consumer)} - Calls the given consumer if a value is present</li>
 *     <li>{@link #ifPresentOr(Consumer, UncheckedVoidCode)} - Calls the given consumer if a value is present, otherwise calls the given code</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * @author jonathanl (shibo)
 * @author viniciusluisr
 * @see <a href="https://github.com/viniciusluisr/improved-optional">improved-optional</a>
 */
public class Maybe<Value> extends BaseRepeater implements
        Presence,
        TryTrait
{
    /**
     * @return Maybe value for null
     */
    @Tested
    public static <Value> Maybe<Value> absent()
    {
        return new Maybe<>();
    }

    /**
     * @return Maybe for the given (null or non-null) value
     */
    @Tested
    public static <Value> Maybe<Value> maybe(Value value)
    {
        return value == null ? absent() : new Maybe<>(ensureNotNull(value));
    }

    /**
     * @return Maybe for the given non-null value
     */
    @Tested
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
    @Tested
    public Maybe<Value> absentIf(BooleanFunction<Value> predicate)
    {
        return tryCatch(() -> value != null && ensureNotNull(predicate).isTrue(value)
                ? newAbsent()
                : this);
    }

    /**
     * <p>
     * If a value is present, uses the given function to map the value from Value to Maybe&lt;Output&gt;. If no value is
     * present, returns {@link #absent()}. The effect is that of allowing functions to be chained together in a nested
     * structure where each nested function introduces a new lambda variable that is visible to inner functions. For
     * example:
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
     * Notice that the first sentence of the description for this method is exactly the same as that provided for {@link
     * #map(Function)}. The difference between the two methods is that the function passed to this method produces
     * Maybe&lt;Output&gt; directly (which allows function nesting), while the function passed to {@link #map(Function)}
     * just maps the value to the Output type, which is then wrapped in a {@link Maybe}.
     * </p>
     *
     * @param function A function mapping from Value to Maybe&lt;Output&gt;
     * @param <Output> The type being mapped to
     * @return The mapped value as a {@link Maybe} object, or {@link #absent()} if no value was present
     */
    @SuppressWarnings("unchecked")
    @Tested
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
    @Tested
    public Stream<Value> asStream()
    {
        return isPresent() ? Stream.of(value) : Stream.empty();
    }

    public boolean equals(Object object)
    {
        if (object instanceof Maybe)
        {
            var that = (Maybe<?>) object;
            return Objects.equals(value, that.value);
        }
        return false;
    }

    /**
     * Returns any value that might be present, or null if there is none
     */
    @Tested
    public Value get()
    {
        return value;
    }

    /**
     * Returns true if a value is present
     */
    @Tested
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
    @Tested
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
    @Tested
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
    @Tested
    public boolean isAbsent()
    {
        return value == null;
    }

    /**
     * Returns true if there is a value present
     */
    @Tested
    public boolean isPresent()
    {
        return value != null;
    }

    /**
     * Returns true if this object is valid
     */
    @Tested
    public boolean isValid()
    {
        return true;
    }

    /**
     * If a value is present, uses the given function to map the value from Value to Output. The mapped value is then
     * wrapped in a Maybe&lt;Output&gt; object and returned. If no value is present, returns {@link #absent()}. The
     * effect is that of simply mapping this optional value to a new type.
     *
     * <p><b>Important Note</b></p>
     *
     * <p>
     * Notice that the first sentence of the description for this method is exactly the same as that provided for {@link
     * #apply(Function)}. The difference between the two methods is that the function passed to {@link #apply(Function)}
     * produces Maybe&lt;Output&gt; directly (which allows function nesting), while the function passed to this method
     * just maps the value to the Output type, which is then wrapped in a {@link Maybe}.
     * </p>
     *
     * @param mapper A function mapping from Value to Output
     * @param <Output> The type that Value is being mapped to
     * @return The mapped value or {@link #absent()}
     */
    @Tested
    public <Output> Maybe<Output> map(Function<? super Value, ? extends Output> mapper)
    {
        return tryCatchDefault(() -> isPresent()
                ? newMaybe(ensureNotNull(mapper).apply(value))
                : newAbsent(), newAbsent());
    }

    /**
     * If there is a value present, returns it, otherwise returns the given default value
     *
     * @param defaultValue The default value to return if there is no value
     * @return The value
     */
    @Tested
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
    @Tested
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
    @Tested
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
    @Tested
    public Maybe<Value> orMaybe(Value value)
    {
        return tryCatch(() -> isPresent()
                ? this
                : newMaybe(value));
    }

    /**
     * Broadcasts a problem and returns null if there is no value, otherwise returns the value
     *
     * @return A value or null
     */
    @Tested
    public Value orProblem(String message, Object... arguments)
    {
        if (isAbsent())
        {
            problem(message, arguments);
            return null;
        }

        return value;
    }

    /**
     * Throws an exception if there is no value, otherwise returns the value
     *
     * @return The value
     */
    @Tested
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
    @Tested
    public Value orThrow(String message, Object... arguments)
    {
        if (isAbsent())
        {
            new Problem(message, arguments).throwAsIllegalStateException();
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
    @Tested
    public Maybe<Value> presentIf(BooleanFunction<Value> predicate)
    {
        return tryCatch(() -> value != null && ensureNotNull(predicate).isTrue(value)
                ? this
                : newAbsent());
    }

    /**
     * If a value is present, and the given value is also present, applies the given bi-function to produce a new value
     * of the same type.
     *
     * @param function The combining function, f(a, b)
     * @param value The value to pass to the bi-function along with this value
     * @return The combination of this value and the given value, if both values are non-null, otherwise, returns {@link
     * #absent()}.
     */
    @Tested
    public Maybe<Value> then(BiFunction<Value, Value, Value> function, Maybe<Value> value)
    {
        if (isPresent() && value.isPresent())
        {
            return tryCatch(() -> newMaybe(function.apply(this.value, value.value)));
        }

        return newAbsent();
    }

    /**
     * If a value is present and the given value is not null, applies the given bi-function to produce a new value of
     * the same type.
     *
     * @param function The combining function
     * @param that The value to apply with this one
     * @return The combination of this value and the given value, if both values are non-null, otherwise, returns {@link
     * #absent()}.
     */
    @Tested
    public Maybe<Value> then(BiFunction<Value, Value, Value> function, Value that)
    {
        if (isPresent() && that != null)
        {
            return tryCatch(() -> newMaybe(function.apply(value, that)));
        }

        return newAbsent();
    }

    /**
     * If a value is present, applies the given function to produce a new value of the same type.
     *
     * @param function The function to apply
     * @return The value produced by the given function when applied to this value
     */
    @Tested
    public Maybe<Value> then(Function<Value, Value> function)
    {
        return map(function);
    }

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

    /**
     * Sets the given result value
     */
    protected Maybe<Value> set(Value value)
    {
        this.value = value;

        return this;
    }
}
