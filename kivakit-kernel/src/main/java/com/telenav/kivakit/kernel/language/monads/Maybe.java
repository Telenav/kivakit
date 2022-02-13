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
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * A substitute for {@link Optional} that adds functionality and integrates with {@link Repeater}
 *
 * <p><b>Creating</b></p>
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
 *
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

    public boolean equals(Object object)
    {
        if (object instanceof Maybe)
        {
            var that = (Maybe<?>) object;
            return Objects.equals(value, that.value);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public <Mapped> Maybe<Mapped> flatMap(Function<? super Value, ? extends Maybe<? extends Mapped>> mapper)
    {
        return isEmpty()
                ? empty()
                : ensureNotNull((Maybe<Mapped>) mapper.apply(value));
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(value);
    }

    public Maybe<Value> ifFalse(BooleanFunction<Value> predicate)
    {
        return value != null && ensureNotNull(predicate).test(value)
                ? empty()
                : this;
    }

    public Maybe<Value> ifNonEmptyOr(Consumer<Value> consumer, Runnable runnable)
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
     * If this value is not null, calls the given consumer with the value
     *
     * @param consumer The consumer for any non-null value
     * @return This {@link Maybe} for chaining
     */
    public Maybe<Value> ifNonNull(Consumer<Value> consumer)
    {
        if (isPresent())
        {
            consumer.accept(value);
        }
        return this;
    }

    /**
     * If this value is not null, calls the given consumer with the value. If it is null, calls the source to get a
     * value, then calls the consumer if that value is not null.
     *
     * @param consumer The consumer for any non-null value
     * @param source An alternate value source if this value is null
     * @return This value for chaining
     */
    public Maybe<Value> ifNonNull(Consumer<Value> consumer, Source<Value> source)
    {
        if (isPresent())
        {
            consumer.accept(value);
        }
        else
        {
            var value = source.get();
            if (value != null)
            {
                consumer.accept(value);
            }
        }

        return this;
    }

    public ElseFunction<Consumer<Value>, ElseFunction<Runnable, Void>> ifNonNull()
    {
        return curry(ifPresentFunction());
    }

    public ElseFunction<Runnable, ElseFunction<Consumer<Value>, Void>> ifNull()
    {
        return curry(ifEmptyFunction());
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

    public <Output> Maybe<Output> map(Function<? super Value, ? extends Output> mapper)
    {
        return isEmpty()
                ? empty()
                : maybe(ensureNotNull(mapper).apply(value));
    }

    @SuppressWarnings("unchecked")
    public Maybe<Value> or(Supplier<? extends Maybe<? extends Value>> supplier)
    {
        if (isPresent())
        {
            return this;
        }
        else
        {
            return (Maybe<Value>) ensureNotNull(ensureNotNull(supplier).get());
        }
    }

    public Value orElse(Value defaultValue)
    {
        return isPresent() ? value : defaultValue;
    }

    public Value orElse(Source<Value> defaultValue)
    {
        return isPresent() ? value : defaultValue.get();
    }

    public Value orElseProblem(String message, Object... arguments)
    {
        if (isEmpty())
        {
            problem(message, arguments);
            return null;
        }

        return value;
    }

    public Value orElseThrow()
    {
        return orElseThrow("No value present");
    }

    public Value orElseThrow(String message, Object... arguments)
    {
        if (isEmpty())
        {
            new Problem(message, arguments).throwAsIllegalStateException();
        }
        return value;
    }

    /**
     * If this value is not null, returns the value as a {@link Stream}, otherwise returns an empty {@link Stream}.
     *
     * @return This value as a {@link Stream}
     */
    public Stream<Value> stream()
    {
        return isPresent() ? Stream.of(value) : Stream.empty();
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
