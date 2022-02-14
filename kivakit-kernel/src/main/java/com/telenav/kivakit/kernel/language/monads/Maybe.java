package com.telenav.kivakit.kernel.language.monads;

import com.telenav.kivakit.kernel.interfaces.collection.Nullable;
import com.telenav.kivakit.kernel.interfaces.function.BooleanFunction;
import com.telenav.kivakit.kernel.interfaces.value.Source;
import com.telenav.kivakit.kernel.messaging.Repeater;
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
 * <p><b>Creating</b></p>
 *
 * <p>
 * A {@link Maybe} can be constructed in three ways:
 *
 * <ol>
 *     <li>{@link #empty()} - Creates a {@link Maybe} whose value is null</li>
 *     <li>{@link #maybe(Object)} - Creates a {@link Maybe} whose value can be null or non-null</li>
 *     <li>{@link #definitely(Object)} - Creates a {@link Maybe} whose value is always non-null</li>
 * </ol>
 *
 * <p><b>Terminal Operations</b></p>
 *
 * <ul>
 *     <li>{@link #isPresent()} - Returns true if this maybe has a non-null value</li>
 *     <li>{@link #isEmpty()} - Returns true if this maybe has a null value</li>
 *     <li>{@link #asStream()} - Converts this value to a stream with zero or one element(s)</li>
 *     <li>{@link #get()} - A value or null</li>
 *     <li>{@link #orDefault(Object)} - Returns this value, or the default value</li>
 *     <li>{@link #orDefault(Source)} - Returns this value, or the default value</li>
 *     <li>{@link #orProblem(String, Object[])} - Returns this value or broadcasts a problem if this value is not present</li>
 *     <li>{@link #orThrow(String, Object...)} - Returns this value or throws an exception</li>
 *     <li>{@link #orThrow()}- Returns this value or throws an exception</li>
 * </ul>
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
 * <p><b>Conditionals</b></p>
 *
 * <ul>
 *     <li>{@link #ifFalse(BooleanFunction)} - Applies the given function to this value, returning this value if it is false, or {@link #empty()} if it is true</li>
 *     <li>{@link #ifTrue(BooleanFunction)} - Applies the given function to this value, returning this value if it is true, or {@link #empty()} if it is false</li>
 *     <li>{@link #ifPresent(Consumer)} - Calls the given consumer if a value is present</li>
 *     <li>{@link #ifPresentOr(Consumer, Runnable)} - Calls the given consumer if a value is present, otherwise calls the given code</li>
 *     <li>{@link #or(Source)} - If a value is present, returns this value, otherwise returns the {@link Maybe} supplied by the given {@link Source}</li>
 *     <li>{@link #ifPresent()} - Permits branching, by returning a curried {@link ElseFunction} with a branch for accepting a value and a branch for running code</li>
 *     <li>{@link #ifEmpty()} - Permits branching, by returning a curried {@link ElseFunction} with a branch for running code and a branch for accepting a value</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @author viniciusluisr
 * @see <a href="https://github.com/viniciusluisr/improved-optional">improved-optional</a>
 */
public class Maybe<Value> extends BaseRepeater implements Nullable
{
    private static final Maybe<?> NULL = new Maybe<>(null);

    /**
     * @return Maybe for the given non-null value
     */
    public static <Value> Maybe<Value> definitely(Value value)
    {
        return new Maybe<>(ensureNotNull(value));
    }

    /**
     * @return Maybe value for null
     */
    @SuppressWarnings("unchecked")
    public static <Value> Maybe<Value> empty()
    {
        return (Maybe<Value>) NULL;
    }

    /**
     * @return Maybe for the given (null or non-null) value
     */
    public static <Value> Maybe<Value> maybe(Value value)
    {
        return value == null ? empty() : new Maybe<>(ensureNotNull(value));
    }

    /** The null or non-null value */
    private final Value value;

    private Maybe(Value value)
    {
        this.value = value;
    }

    /**
     * Applies the given function to this value and the given value, if both values are present, returning a new value.
     *
     * @param function The combining function
     * @param that The value to apply with this one
     * @return The combination of this value and the given value, if both values are non-null, otherwise, returns {@link
     * #empty()}.
     */
    public Maybe<Value> apply(BiFunction<Value, Value, Value> function, Maybe<Value> that)
    {
        if (isPresent() && that.isPresent())
        {
            return maybe(function.apply(value, that.value));
        }
        return empty();
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
            return maybe(function.apply(value));
        }
        return empty();
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
     * Maps this value to another type if a value is present, otherwise returns {@link #empty()}.
     *
     * @param mapper The mapping function
     * @return The mapped value or empty
     */
    @SuppressWarnings("unchecked")
    public <Mapped> Maybe<Mapped> flatMap(Function<? super Value, ? extends Maybe<? extends Mapped>> mapper)
    {
        return isEmpty()
                ? empty()
                : ensureNotNull((Maybe<Mapped>) mapper.apply(value));
    }

    public Value get()
    {
        return value;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(value);
    }

    /**
     * Returns an {@link ElseFunction} allowing for branching
     */
    public ElseFunction<Runnable, ElseFunction<Consumer<Value>, Void>> ifEmpty()
    {
        return curry(ifEmptyFunction());
    }

    /**
     * If a value is present and the predicate is false when applied to the value, returns this, otherwise returns
     * {@link #empty()}
     *
     * @param predicate The predicate to test the value
     * @return This value or {@link #empty()}
     */
    public Maybe<Value> ifFalse(BooleanFunction<Value> predicate)
    {
        return isPresent() && ensureNotNull(predicate).test(value)
                ? empty()
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
     * returns the {@link #empty()} value.
     *
     * @param predicate The predicate to test any non-null value
     * @return This value or a null value
     */
    public Maybe<Value> ifTrue(BooleanFunction<Value> predicate)
    {
        return value != null && ensureNotNull(predicate).isTrue(value)
                ? this
                : empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty()
    {
        return value == null;
    }

    /**
     * Maps this value to another type if a value is present, otherwise returns {@link #empty()}.
     *
     * @param mapper The mapping function
     * @return The mapped value or empty
     */
    public <Output> Maybe<Output> map(Function<? super Value, ? extends Output> mapper)
    {
        return isEmpty()
                ? empty()
                : maybe(ensureNotNull(mapper).apply(value));
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
            return (Maybe<Value>) ensureNotNull(ensureNotNull(source).get());
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
        if (isEmpty())
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
        if (isEmpty())
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
    private <X, Y, Result> ElseFunction<X, ElseFunction<Y, Result>> curry(BiFunction<X, Y, Result> function)
    {
        return (final X x) -> (final Y y) -> function.apply(x, y);
    }

    private BiFunction<Runnable, Consumer<Value>, Void> ifEmptyFunction()
    {
        return (notPresent, present) ->
        {
            if (isEmpty())
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
