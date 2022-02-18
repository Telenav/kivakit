package com.telenav.kivakit.kernel.language.monads;

import com.telenav.kivakit.kernel.interfaces.collection.Presence;
import com.telenav.kivakit.kernel.interfaces.function.BooleanFunction;
import com.telenav.kivakit.kernel.interfaces.value.Source;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.kernel.messaging.messages.Result;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

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
 *     <li>{@link #definitely(Object)} - Creates a {@link Maybe} whose value is always non-null</li>
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
 *     <li>{@link #orDefault(Object)} - Returns this value, or the default value</li>
 *     <li>{@link #orDefault(Source)} - Returns this value, or the default value</li>
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
 *     <li>{@link #apply(Function)} - Returns the result of applying the given function to this value</li>
 *     <li>{@link #apply(BiFunction, Maybe)} - Returns the result of applying the given two-argument function to this value and the given value</li>
 *     <li>{@link #flatMap(Function)} - Applies the given function to this value</li>
 *     <li>{@link #map(Function)} - Applies the given function to this value</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Conditionals</b></p>
 *
 * <ul>
 *     <li>{@link #ifPresent()} - Permits branching, by returning a curried {@link ElseFunction} with a branch for accepting a value and a branch for running code</li>
 *     <li>{@link #ifAbsent()} - Permits branching, by returning a curried {@link ElseFunction} with a branch for running code and a branch for accepting a value</li>
 *     <li>{@link #ifFalse(BooleanFunction)} - Applies the given function to this value, returning this value if it is false, or {@link #absent()} if it is true</li>
 *     <li>{@link #ifTrue(BooleanFunction)} - Applies the given function to this value, returning this value if it is true, or {@link #absent()} if it is false</li>
 *     <li>{@link #ifPresent(Consumer)} - Calls the given consumer if a value is present</li>
 *     <li>{@link #ifPresentOr(Consumer, Runnable)} - Calls the given consumer if a value is present, otherwise calls the given code</li>
 *     <li>{@link #or(Source)} - If a value is present, returns this value, otherwise returns the {@link Maybe} supplied by the given {@link Source}</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * @author jonathanl (shibo)
 * @author viniciusluisr
 * @see <a href="https://github.com/viniciusluisr/improved-optional">improved-optional</a>
 */
public class Maybe<Value> extends BaseRepeater implements Presence
{
    /**
     * A {@link Maybe} object with no value
     */
    private static final Maybe<?> VALUE_ABSENT = new Maybe<>();

    /**
     * @return Maybe value for null
     */
    @SuppressWarnings("unchecked")
    public static <Value> Maybe<Value> absent()
    {
        return (Maybe<Value>) VALUE_ABSENT;
    }

    /**
     * @return Maybe for the given non-null value
     */
    public static <Value> Maybe<Value> definitely(Value value)
    {
        return new Maybe<>(ensureNotNull(value));
    }

    /**
     * @return Maybe for the given (null or non-null) value
     */
    public static <Value> Maybe<Value> maybe(Value value)
    {
        return value == null ? absent() : new Maybe<>(ensureNotNull(value));
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
     * Applies the given function to this value and the given value, if both values are present, returning a new value.
     *
     * @param function The combining function
     * @param that The value to apply with this one
     * @return The combination of this value and the given value, if both values are non-null, otherwise, returns {@link
     * #absent()}.
     */
    public Maybe<Value> apply(BiFunction<Value, Value, Value> function, Maybe<Value> that)
    {
        if (isPresent() && that.isPresent())
        {
            return newMaybe(function.apply(value, that.value));
        }
        return newAbsent();
    }

    /**
     * Applies the given function to this value and the given value, if both values are present, returning a new value.
     *
     * @param function The combining function
     * @param that The value to apply with this one
     * @return The combination of this value and the given value, if both values are non-null, otherwise, returns {@link
     * #absent()}.
     */
    public Maybe<Value> apply(BiFunction<Value, Value, Value> function, Value that)
    {
        if (isPresent() && that != null)
        {
            return newMaybe(function.apply(value, that));
        }
        return newAbsent();
    }

    /**
     * Applies the given function to this value, if it is present
     *
     * @param function The function to apply
     * @return The value produced by the given function when applied to this value
     */
    public Maybe<Value> apply(Function<Value, Value> function)
    {
        if (isPresent())
        {
            return newMaybe(function.apply(value));
        }
        return newAbsent();
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
     * Maps this value to another type if a value is present, otherwise returns {@link #absent()}.
     *
     * @param mapper The mapping function
     * @return The mapped value or {@link #absent()}
     */
    @SuppressWarnings("unchecked")
    public <Mapped> Maybe<Mapped> flatMap(Function<? super Value, ? extends Maybe<? extends Mapped>> mapper)
    {
        return isAbsent()
                ? newAbsent()
                : ensureNotNull((Maybe<Mapped>) mapper.apply(value));
    }

    /**
     * Returns any value that might be present, or null if there is none
     */
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
     * Returns an {@link ElseFunction} allowing for branching
     */
    public ElseFunction<Runnable, ElseFunction<Consumer<Value>, Void>> ifAbsent()
    {
        return curry(ifAbsentFunction());
    }

    /**
     * If a value is present and the predicate is false when applied to the value, returns this, otherwise returns
     * {@link #absent()}
     *
     * @param predicate The predicate to test the value
     * @return This value or {@link #absent()}
     */
    public Maybe<Value> ifFalse(BooleanFunction<Value> predicate)
    {
        return isPresent() && ensureNotNull(predicate).test(value)
                ? newAbsent()
                : this;
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
            consumer.accept(value);
        }
        return this;
    }

    /**
     * Returns an {@link ElseFunction} allowing for branching
     */
    public ElseFunction<Consumer<Value>, ElseFunction<Runnable, Void>> ifPresent()
    {
        return curry(ifPresentFunction());
    }

    /**
     * If a value is present, calls the given consumer, otherwise runs the given block of code
     *
     * @param consumer The consumer to call
     * @param runnable The code to run
     * @return This value for chaining
     */
    public Maybe<Value> ifPresentOr(Consumer<Value> consumer, Runnable runnable)
    {
        if (isPresent())
        {
            consumer.accept(value);
        }
        else
        {
            runnable.run();
        }

        return this;
    }

    /**
     * If this value is not null and the given predicate evaluates to true for the value, returns this value, otherwise
     * returns the {@link #absent()} value.
     *
     * @param predicate The predicate to test any non-null value
     * @return This value or a null value
     */
    public Maybe<Value> ifTrue(BooleanFunction<Value> predicate)
    {
        return value != null && ensureNotNull(predicate).isTrue(value)
                ? this
                : newAbsent();
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
     * Returns true if there is a value present
     */
    public boolean isPresent()
    {
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
     * Maps this value to another type if a value is present, otherwise returns {@link #absent()}.
     *
     * @param mapper The mapping function
     * @return The mapped value or {@link #absent()}
     */
    public <Output> Maybe<Output> map(Function<? super Value, ? extends Output> mapper)
    {
        return isAbsent()
                ? newAbsent()
                : newMaybe(ensureNotNull(mapper).apply(value));
    }

    /**
     * If there is a value present, returns this, otherwise returns the maybe provided by the given {@link Source}
     *
     * @param source A source of a {@link Maybe} if there is no value present
     * @return This value or the value produced by the given source
     */
    @SuppressWarnings("unchecked")
    public Maybe<Value> or(Source<? extends Maybe<? extends Value>> source)
    {
        if (isPresent())
        {
            return this;
        }
        else
        {
            return (Maybe<Value>) newMaybe(ensureNotNull(source).get());
        }
    }

    /**
     * If there is a value present, returns it, otherwise returns the given default value
     *
     * @param defaultValue The default value to return if there is no value
     * @return The value
     */
    public Value orDefault(Value defaultValue)
    {
        return isPresent() ? value : defaultValue;
    }

    /**
     * If there is a value present, returns it, otherwise returns the given default value
     *
     * @param defaultValue The default value to return if there is no value
     * @return The value
     */
    public Value orDefault(Source<Value> defaultValue)
    {
        return isPresent() ? value : defaultValue.get();
    }

    /**
     * Broadcasts a problem and returns null if there is no value, otherwise returns the value
     *
     * @return A value or null
     */
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
            new Problem(message, arguments).throwAsIllegalStateException();
        }
        return value;
    }

    public String toString()
    {
        return "[Maybe value = " + value + "]";
    }

    /**
     * Overridden in {@link Result} to return the right subclass
     */
    @SuppressWarnings("unchecked")
    protected <T> Maybe<T> newAbsent()
    {
        return (Maybe<T>) VALUE_ABSENT;
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

    /**
     * Takes a two argument function:
     *
     * <pre>f(x, y) -> result</pre>
     * <p>
     * and returns a function of a function:
     *
     * <pre>f(x, f'(y)) -> result</pre>
     *
     * @return The composed function
     */
    private <X, Y, Output> ElseFunction<X, ElseFunction<Y, Output>> curry(BiFunction<X, Y, Output> function)
    {
        return (final X x) -> (final Y y) -> function.apply(x, y);
    }

    private BiFunction<Runnable, Consumer<Value>, Void> ifAbsentFunction()
    {
        return (notPresent, present) ->
        {
            if (isAbsent())
            {
                present.accept(value);
            }
            else
            {
                notPresent.run();
            }
            return null;
        };
    }

    private BiFunction<Consumer<Value>, Runnable, Void> ifPresentFunction()
    {
        return (present, notPresent) ->
        {
            if (isPresent())
            {
                present.accept(value);
            }
            else
            {
                notPresent.run();
            }
            return null;
        };
    }
}
