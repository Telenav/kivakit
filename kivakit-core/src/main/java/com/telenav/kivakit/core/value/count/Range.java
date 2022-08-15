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

import com.telenav.kivakit.core.language.primitive.Longs;
import com.telenav.kivakit.core.internal.lexakai.DiagramCount;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.testing.Tested;
import com.telenav.kivakit.interfaces.code.FilteredLoopBody;
import com.telenav.kivakit.interfaces.code.LoopBody;
import com.telenav.kivakit.interfaces.collection.NextValue;
import com.telenav.kivakit.interfaces.numeric.IntegerNumeric;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.kivakit.interfaces.numeric.QuantumComparable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Random;
import java.util.function.Consumer;

import static com.telenav.kivakit.core.value.count.Range.UpperBound.EXCLUSIVE;
import static com.telenav.kivakit.core.value.count.Range.UpperBound.INCLUSIVE;

/**
 * Represents a range of value objects from a minimum to a maximum.
 *
 * <p>Creation and Properties</p>
 *
 * <p>
 * Ranges can be created in two ways, as {@link #rangeExclusive(IntegerNumeric, IntegerNumeric)} ranges which do not
 * include their maximum value, and as {@link #rangeInclusive(IntegerNumeric, IntegerNumeric)} ranges which do.
 * </p>
 *
 * <ul>
 *     <li>{@link #rangeInclusive(IntegerNumeric, IntegerNumeric)}</li>
 *     <li>{@link #rangeExclusive(IntegerNumeric, IntegerNumeric)}</li>
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
 *     <li>{@link #forEach(LoopBody)}</li>
 *     <li>{@link #forCount(Count, FilteredLoopBody)} </li>
 *     <li>{@link #loop(Runnable)}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #constrain(Value)}</li>
 *     <li>{@link #contains(Value)}</li>
 *     <li>{@link #randomValue(Random)}</li>
 * </ul>
 *
 * @param <Value> A value that is {@link IntegerNumeric}, which includes {@link Minimizable}, {@link Maximizable},
 * {@link Comparable}, and {@link NextValue}.
 * @author jonathanl (shibo)
 * @see LoopBody
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCount.class)
public class Range<Value extends IntegerNumeric<Value>> implements
        Comparable<Countable>,
        QuantumComparable<Countable>,
        Countable
{
    /**
     * Constructs a range that excludes the given maximum value.
     */
    @Tested
    public static <Value extends IntegerNumeric<Value>>
    Range<Value> rangeExclusive(Value minimum,
                                Value exclusiveMaximum)
    {
        return new Range<>(minimum, exclusiveMaximum, EXCLUSIVE);
    }

    /**
     * Constructs a range that includes the given maximum value.
     */
    @Tested
    public static <Value extends IntegerNumeric<Value>>
    Range<Value> rangeInclusive(Value minimum,
                                Value inclusiveMaximum)
    {
        return new Range<>(minimum, inclusiveMaximum, INCLUSIVE);
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

    protected Range(Value minimum, Value maximum, UpperBound upperBound)
    {
        this.minimum = minimum;
        this.maximum = maximum;
        this.upperBound = upperBound;
    }

    @Override
    public int compareTo(Countable that)
    {
        return Long.compare(quantum(), that.quantum());
    }

    /**
     * Forces the given value into this range
     */
    @Tested
    public Value constrain(Value value)
    {
        var constrainedToMinimum = minimum().maximum(value);
        return inclusiveMaximum().minimum(constrainedToMinimum);
    }

    /**
     * True if this range contains the given value
     */
    @Tested
    public boolean contains(Value value)
    {
        return value.isGreaterThanOrEqualTo(minimum()) &&
                value.isLessThanOrEqualTo(inclusiveMaximum());
    }

    /**
     * Returns the exclusive maximum for this range, even if it was constructed as inclusive (for example, inclusive
     * range of 0 to 9 is the same as an exclusive range of 0-10).
     */
    @Tested
    public Value exclusiveMaximum()
    {
        return isExclusive()
                ? maximum
                : maximum.incremented();
    }

    /**
     * Executes the loop starting at the minimum value, calling the {@link FilteredLoopBody} until the given number of
     * values are accepted.
     *
     * @param body The loop body to invoke
     */
    @Tested
    public void forCount(Count count, FilteredLoopBody<Value> body)
    {
        body.forCount(minimum(), exclusiveMaximum(), count.asLong());
    }

    /**
     * Calls the given {@link LoopBody} with each value from the minimum to the maximum (inclusive or exclusive,
     * depending on construction of the range)
     *
     * @param body The loop body to invoke
     */
    @Tested
    public void forEach(LoopBody<Value> body)
    {
        body.forEachInclusive(minimum(), inclusiveMaximum());
    }

    /**
     * Calls the given {@link LoopBody} with each value from the minimum to the maximum (inclusive or exclusive,
     * depending on construction of the range)
     *
     * @param body The loop body to invoke
     */
    @Tested
    public void forEachInt(Consumer<Integer> body)
    {
        forEach(at -> body.accept(at.asInt()));
    }

    /**
     * Returns the inclusive maximum for this range, even if it was constructed as exclusive (for example, an exclusive
     * range of 0-10 is the same as an inclusive range of 0 to 9).
     */
    @Tested
    public Value inclusiveMaximum()
    {
        return isInclusive()
                ? maximum
                : maximum.decremented();
    }

    /**
     * @return True if this is an exclusive range
     */
    @Tested
    public boolean isExclusive()
    {
        return upperBound == EXCLUSIVE;
    }

    /**
     * @return True if this is an inclusive range
     */
    @Tested
    public boolean isInclusive()
    {
        return upperBound == INCLUSIVE;
    }

    /**
     * Executes the given code body once for each value in this range
     */
    @Tested
    public void loop(Runnable body)
    {
        forEach(ignored -> body.run());
    }

    /**
     * Returns the minimum value in this range.
     */
    @Tested
    public Value minimum()
    {
        return minimum;
    }

    /**
     * Returns a random value in this range
     */
    @Tested
    public Value randomValue(Random random)
    {
        return minimum.newInstance(Longs.random(random, minimum().asLong(), exclusiveMaximum().asLong()));
    }

    /**
     * Returns the size of this range in values
     */
    @Override
    @Tested
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
    @Tested
    public UpperBound upperBound()
    {
        return upperBound;
    }
}
