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

package com.telenav.kivakit.core.value.count;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.iteration.BaseIterator;
import com.telenav.kivakit.core.internal.lexakai.DiagramCount;
import com.telenav.kivakit.core.language.primitive.Longs;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.interfaces.code.Callback;
import com.telenav.kivakit.interfaces.collection.Contains;
import com.telenav.kivakit.interfaces.collection.NextIterator;
import com.telenav.kivakit.interfaces.function.Mapper;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.kivakit.interfaces.numeric.Numeric;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.value.count.Range.UpperBound.EXCLUSIVE;
import static com.telenav.kivakit.core.value.count.Range.UpperBound.INCLUSIVE;

/**
 * Represents a range of value objects from a minimum to a maximum.
 *
 * <p>Creation and Properties</p>
 *
 * <p>
 * Ranges can be created in two ways, as {@link #rangeExclusive(Numeric, Numeric, Function)} ranges which do not include
 * their maximum value, and as {@link #rangeInclusive(Numeric, Numeric, Function)} ranges which do.
 * </p>
 *
 * <ul>
 *     <li>{@link #rangeInclusive(Numeric, Numeric, Function)}</li>
 *     <li>{@link #rangeExclusive(Numeric, Numeric, Function)}</li>
 *     <li>{@link #upperBound()}</li>
 *     <li>{@link #isInclusive()}</li>
 *     <li>{@link #isExclusive()}</li>
 *     <li>{@link #minimum()}</li>
 *     <li>{@link #inclusiveMaximum()}</li>
 *     <li>{@link #exclusiveMaximum()}</li>
 * </ul>
 *
 * <p><b>Looping</b></p>
 *
 * <p>
 * The values in a range can be processed in a loop with these methods.
 * </p>
 *
 * <ul>
 *     <li>{@link #loop(Runnable)}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #constrained(Value)}</li>
 *     <li>{@link #containsInclusive(Value)}</li>
 *     <li>{@link #randomValue(Random)}</li>
 * </ul>
 *
 * @param <Value> A value that is {@link Numeric}, which includes {@link Minimizable}, {@link Maximizable},
 * {@link Comparable}, and {@link NextIterator}.
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCount.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Range<Value extends Numeric<Value>> implements
        Contains<Value>,
        Comparable<Countable>,
        Countable,
        Iterable<Value>,
        Mapper<Long, Value>
{
    /**
     * Constructs a range that excludes the given maximum value.
     */
    public static <Value extends Numeric<Value>>
    Range<Value> rangeExclusive(Value minimum,
                                Value exclusiveMaximum,
                                Function<Long, Value> newInstance)
    {
        return new Range<>(minimum, exclusiveMaximum, EXCLUSIVE, newInstance);
    }

    /**
     * Constructs a range that includes the given maximum value.
     */
    public static Range<Count> rangeExclusive(Count minimum,
                                              Count inclusiveMaximum)
    {
        return new Range<>(minimum, inclusiveMaximum, EXCLUSIVE, Count::count);
    }

    /**
     * Constructs a range that includes the given maximum value.
     */
    public static Range<Count> rangeInclusive(Count minimum,
                                              Count inclusiveMaximum)
    {
        return new Range<>(minimum, inclusiveMaximum, INCLUSIVE, Count::count);
    }

    /**
     * Constructs a range that includes the given maximum value.
     */
    public static <Value extends Numeric<Value>>
    Range<Value> rangeInclusive(Value minimum,
                                Value inclusiveMaximum,
                                Function<Long, Value> newInstance)
    {
        return new Range<>(minimum, inclusiveMaximum, INCLUSIVE, newInstance);
    }

    /**
     * The type of upper bound
     */
    public enum UpperBound
    {
        /** The range's maximum value is included in the range */
        INCLUSIVE,

        /** The range's maximum value is not included in the range */
        EXCLUSIVE
    }

    /** The maximum value of the range (which may be included in the range or not, depending on the <i>upperBound</i> */
    private final Value maximum;

    /** The minimum value of the range */
    private final Value minimum;

    /** The type of upper bound that this range has, either inclusive or exclusive of the end point */
    private final UpperBound upperBound;

    /** The function to create new value instances */
    private final Function<Long, Value> newInstance;

    protected Range(Value minimum,
                    Value maximum,
                    UpperBound upperBound,
                    Function<Long, Value> newInstance)
    {
        this.minimum = minimum;
        this.maximum = maximum;
        this.upperBound = upperBound;
        this.newInstance = newInstance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Countable that)
    {
        return Long.compare(count().longValue(), that.count().longValue());
    }

    /**
     * Forces the given value into this range
     */
    public Value constrained(Value value)
    {
        return inclusiveMaximum().minimize(minimum().maximize(value));
    }

    /**
     * True if this range contains the given value, inclusive or exclusive depending on the value of
     * {@link #upperBound()}
     */
    @Override
    public boolean contains(Value value)
    {
        return isInclusive()
                ? containsInclusive(value)
                : containsExclusive(value);
    }

    /**
     * True if this range contains the given value, exclusive
     */
    public boolean containsExclusive(Value value)
    {
        return value.isGreaterThanOrEqualTo(minimum()) &&
                value.isLessThan(exclusiveMaximum());
    }

    /**
     * True if this range contains the given value, inclusive
     */
    public boolean containsInclusive(Value value)
    {
        return value.isGreaterThanOrEqualTo(minimum()) &&
                value.isLessThanOrEqualTo(inclusiveMaximum());
    }

    /**
     * The number of elements in this range
     */
    @Override
    public Count count()
    {
        return Count.count(size());
    }

    /**
     * Returns the exclusive maximum for this range, even if it was constructed as inclusive (for example, an inclusive
     * range of 0-9 is the same as an exclusive range of 0 to 10).
     */
    public Value exclusiveMaximum()
    {
        return isInclusive()
                ? maximum.incremented()
                : maximum;
    }

    /**
     * Calls the callback with each int in this range
     *
     * @param callback The callback to call
     */
    public void forEachInt(Callback<Integer> callback)
    {
        for (var at = minimum().asInt(); at < exclusiveMaximum().asInt(); at++)
        {
            callback.call(at);
        }
    }

    /**
     * Returns the inclusive maximum for this range, even if it was constructed as exclusive (for example, an exclusive
     * range of 0-10 is the same as an inclusive range of 0 to 9).
     */
    public Value inclusiveMaximum()
    {
        return isInclusive()
                ? maximum
                : maximum.decremented();
    }

    /**
     * Returns true if this is an exclusive range
     */
    public boolean isExclusive()
    {
        return upperBound == EXCLUSIVE;
    }

    /**
     * Returns true if this is an inclusive range
     */
    public boolean isInclusive()
    {
        return upperBound == INCLUSIVE;
    }

    @NotNull
    @Override
    public Iterator<Value> iterator()
    {
        return new BaseIterator<>()
        {
            long at = minimum.longValue();

            @Override
            protected Value onNext()
            {
                if (at < exclusiveMaximum().longValue())
                {
                    at++;
                    return map(at);
                }
                return null;
            }
        };
    }

    /**
     * Executes the given code body once for each value in this range
     */
    public void loop(Runnable body)
    {
        forEach(ignored -> body.run());
    }

    /**
     * Returns the minimum value in this range.
     */
    public Value minimum()
    {
        return minimum;
    }

    @Override
    public Value map(Long value)
    {
        return newInstance.apply(value);
    }

    /**
     * Returns a random value in this range
     */
    public Value randomValue(Random random)
    {
        return minimum.map(Longs.random(random, minimum().asLong(), exclusiveMaximum().asLong()));
    }

    /**
     * Returns the size of this range in values
     */
    public int size()
    {
        return exclusiveMaximum().minus(minimum()).asInt();
    }

    @Override
    public String toString()
    {
        return Formatter.format("[$ to $, $]", minimum(),
                isInclusive()
                        ? inclusiveMaximum()
                        : exclusiveMaximum(),
                isInclusive()
                        ? "inclusive"
                        : "exclusive");
    }

    /**
     * Returns the kind of {@link UpperBound} that was used to construct this range.
     */
    public UpperBound upperBound()
    {
        return upperBound;
    }
}
