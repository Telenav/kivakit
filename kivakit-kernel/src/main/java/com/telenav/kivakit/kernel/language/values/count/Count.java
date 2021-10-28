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

package com.telenav.kivakit.kernel.language.values.count;

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.interfaces.code.Loopable;
import com.telenav.kivakit.kernel.interfaces.numeric.Countable;
import com.telenav.kivakit.kernel.interfaces.numeric.Maximizable;
import com.telenav.kivakit.kernel.interfaces.numeric.Minimizable;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.kernel.language.math.Primes;
import com.telenav.kivakit.kernel.language.primitives.Ints;
import com.telenav.kivakit.kernel.language.primitives.Longs;
import com.telenav.kivakit.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

import static com.telenav.kivakit.kernel.language.strings.conversion.StringFormat.PROGRAMMATIC_IDENTIFIER;

/**
 * Represents a count of something. Counts have useful properties that primitive values like long and int do not have.
 * Count objects are guaranteed to be positive values, so it is not possible to have a {@link Count} of -1. Counts also
 * have a variety of useful methods for comparison, conversion, mathematical operations and other conveniences. By
 * implementing the {@link Quantizable} interface, they inherit operations and are interoperable with all other objects
 * that consume {@link Quantizable}s.
 * <p>
 * {@link Count} objects are cheaper than they might seem for two reasons:
 * <ul>
 *     <li>(1) low count constants and powers of two can be accessed as constant objects (like {@link #_1024} or
 *          {@link #_15}) and values up to 65,536 are cached so that no new object is created if you call {@link #count(long)}
 *     </li>
 *
 *     <li>(2) allocation of count objects higher than 65,536 are cheap in Java due to the design of generational garbage
 *     collectors. This said, there will be occasions where {@link Count} objects are not desirable (for example, inside a
 *     doubly nested loop) and sometimes they may improve a public API method or constructor, while the internal representation
 *     is a primitive value for efficiency. As always, the best approach is to use objects until a problem shows up
 *     in a profiler like YourKit.
 *     </li>
 * </ul>
 * <p>
 * Conversion
 * <ul>
 *     <li>{@link #asInt()}</li>
 *     <li>{@link #get()}</li>
 *     <li>{@link #asBitCount()}</li>
 *     <li>{@link #asEstimate()}</li>
 *     <li>{@link #asMaximum()}</li>
 *     <li>{@link #asMinimum()}</li>
 *     <li>{@link #quantum()}</li>
 * </ul>
 * <p>
 * Comparison
 * <ul>
 *     <li>{@link #isLessThan(Quantizable)}</li>
 *     <li>{@link #isGreaterThan(Quantizable)}</li>
 *     <li>{@link #isLessThanOrEqualTo(Quantizable)}</li>
 *     <li>{@link #isGreaterThanOrEqualTo(Quantizable)}</li>
 *     <li>{@link #asMaximum()}</li>
 *     <li>{@link #asMinimum()}</li>
 *     <li>{@link #quantum()}</li>
 *     <li>{@link #maximum(Count)}</li>
 *     <li>{@link #minimum(Count)}</li>
 * </ul>
 * <p>
 * Mathematical
 * <ul>
 *     <li>{@link #decremented()}</li>
 *     <li>{@link #incremented()}</li>
 *     <li>{@link #plus(Count)}</li>
 *     <li>{@link #plus(long)}</li>
 *     <li>{@link #minus(Count)}</li>
 *     <li>{@link #minus(long)}</li>
 *     <li>{@link #dividedBy(Count)}</li>
 *     <li>{@link #dividedBy(long)}</li>
 *     <li>{@link #times(Count)}</li>
 *     <li>{@link #times(long)}</li>
 *     <li>{@link #dividesEvenlyBy(Count)}</li>
 *     <li>{@link #isZero()}</li>
 *     <li>{@link #isNonZero()}</li>
 *     <li>{@link #percent(Percent)} - The given percentage of this count</li>
 *     <li>{@link #percentOf(Count)} - This count as a percentage of the given count</li>
 * </ul>
 * <p>
 * Utility
 * <ul>
 *     <li>{@link #bitsToRepresent()} - The number of bits required to represent this count</li>
 *     <li>{@link #roundUpToPowerOfTwo()} - The next power of two above this count</li>
 *     <li>{@link #toCommaSeparatedString()}</li>
 *     <li>{@link #loop(Runnable)} - Runs the given code block {@link #count()} times</li>
 *     <li>{@link #loop(Loopable)} - Runs the given code block {@link #count()} times, passing the iteration number to the code</li>
 *     <li>{@link #loop(int, Loopable)} - Static method that runs the given code block {@link #count()} times</li>
 *     <li>{@link #newByteArray()} - Allocates a byte array of {@link #count()} elements</li>
 *     <li>{@link #newCharArray()} - Allocates a char array of {@link #count()} elements</li>
 *     <li>{@link #newDoubleArray()} - Allocates a double array of {@link #count()} elements</li>
 *     <li>{@link #newFloatArray()} - Allocates a float array of {@link #count()} elements</li>
 *     <li>{@link #newIntArray()} - Allocates an int array of {@link #count()} elements</li>
 *     <li>{@link #newLongArray()} - Allocates a long array of {@link #count()} elements</li>
 *     <li>{@link #newObjectArray()} - Allocates an Object array of {@link #count()} elements</li>
 *     <li>{@link #newShortArray()} - Allocates a short array of {@link #count()} elements</li>
 *     <li>{@link #newStringArray()} - Allocates a String array of {@link #count()} elements</li>
 * </ul>
 * {@link Count} objects implement the {@link #hashCode()} / {@link #equals(Object)} contract and are {@link Comparable}.
 *
 * @author jonathanl (shibo)
 * @see Quantizable
 * @see Countable
 * @see Comparable
 * @see Estimate
 * @see Maximum
 * @see Minimum
 */
@SuppressWarnings("SwitchStatementWithTooFewBranches")
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class Count implements
        Countable,
        Comparable<Count>,
        Quantizable,
        Maximizable<Count>,
        Minimizable<Count>,
        AsString,
        Serializable
{
    public static final Count _0 = new Count(0);

    public static final Count _1 = new Count(1);

    public static final Count _2 = new Count(2);

    public static final Count _3 = new Count(3);

    public static final Count _4 = new Count(4);

    public static final Count _5 = new Count(5);

    public static final Count _6 = new Count(6);

    public static final Count _7 = new Count(7);

    public static final Count _8 = new Count(8);

    public static final Count _9 = new Count(9);

    public static final Count _10 = new Count(10);

    public static final Count _11 = new Count(11);

    public static final Count _12 = new Count(12);

    public static final Count _13 = new Count(13);

    public static final Count _14 = new Count(14);

    public static final Count _15 = new Count(15);

    public static final Count _16 = new Count(16);

    public static final Count _17 = new Count(17);

    public static final Count _18 = new Count(18);

    public static final Count _19 = new Count(19);

    public static final Count _20 = new Count(20);

    public static final Count _21 = new Count(21);

    public static final Count _22 = new Count(22);

    public static final Count _23 = new Count(23);

    public static final Count _24 = new Count(24);

    public static final Count _25 = new Count(25);

    public static final Count _26 = new Count(26);

    public static final Count _27 = new Count(27);

    public static final Count _28 = new Count(28);

    public static final Count _29 = new Count(29);

    public static final Count _30 = new Count(30);

    public static final Count _31 = new Count(31);

    public static final Count _32 = new Count(32);

    public static final Count _64 = new Count(64);

    public static final Count _128 = new Count(128);

    public static final Count _256 = new Count(256);

    public static final Count _512 = new Count(512);

    public static final Count _1024 = new Count(1024);

    public static final Count _2048 = new Count(2048);

    public static final Count _4096 = new Count(4096);

    public static final Count _8192 = new Count(8192);

    public static final Count _16384 = new Count(16384);

    public static final Count _32768 = new Count(32678);

    public static final Count _65536 = new Count(65536);

    public static final Count _131_072 = new Count(131_072);

    public static final Count _262_144 = new Count(262_144);

    public static final Count _524_288 = new Count(524_288);

    public static final Count _1_048_576 = new Count(1048576);

    public static final Count _100 = new Count(100);

    public static final Count _1_000 = new Count(1_000);

    public static final Count _10_000 = new Count(10_000);

    public static final Count _100_000 = new Count(100_000);

    public static final Count _1_000_000 = new Count(1_000_000);

    public static final Count _10_000_000 = new Count(10_000_000);

    public static final Count _100_000_000 = new Count(100_000_000);

    public static final Count _1_000_000_000 = new Count(1_000_000_000);

    public static final Count MAXIMUM = new Count(Long.MAX_VALUE);

    public static final Count MAXIMUM_INTEGER = new Count(Integer.MAX_VALUE);

    private static final int CACHE_SIZE = 8_192;

    private static final Count[] CACHED;

    private static final Count[] CACHED_POWERS_OF_TWO;

    static
    {
        CACHED = new Count[CACHE_SIZE];
        for (var i = 0; i < CACHED.length; i++)
        {
            CACHED[i] = new Count(i);
        }

        CACHED_POWERS_OF_TWO = new Count[63];
        for (var i = 0; i < CACHED_POWERS_OF_TWO.length; i++)
        {
            CACHED_POWERS_OF_TWO[i] = new Count(1L << i);
        }
    }

    public static Count count(final Collection<?> collection)
    {
        return count(collection.size());
    }

    public static Count count(final Iterable<?> iterable)
    {
        Ensure.ensure(!(iterable instanceof Count));
        return count(iterable.iterator());
    }

    public static Count count(final Iterable<?> iterable, final Count maximum)
    {
        return count(iterable.iterator(), maximum);
    }

    public static Count count(final Iterator<?> iterator)
    {
        var count = 0L;
        while (iterator.hasNext())
        {
            iterator.next();
            count++;
        }
        return count(count);
    }

    public static Count count(final Iterator<?> iterator, final Count maximum)
    {
        var count = 0L;
        while (iterator.hasNext())
        {
            iterator.next();
            count++;
            if (count > maximum.get())
            {
                return count(count);
            }
        }
        return count(count);
    }

    public static Count count(double value)
    {
        return count((long) value);
    }

    public static Count count(final long value)
    {
        // If we have a cached value,
        if (value < CACHE_SIZE)
        {
            // return it
            return CACHED[(int) value];
        }

        // If our value is a power of two,
        if ((value & value - 1) == 0)
        {
            // return the cached value
            return CACHED_POWERS_OF_TWO[Long.numberOfTrailingZeros(value)];
        }

        // If the value isn't < CACHE_SIZE (65,536) and it's not a power of two we have to allocate
        return new Count(value);
    }

    public static <T> Count count(final T[] values)
    {
        return count(values.length);
    }

    public static void loop(final int times, final Loopable code)
    {
        for (var iteration = 0; iteration < times; iteration++)
        {
            code.iteration(times);
        }
    }

    public static Count parse(final String value)
    {
        final var count = Longs.parse(value, -1);
        return count < 0 ? null : count(count);
    }

    /**
     * Converts to and from a {@link Count}
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<Count>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected Count onToValue(final String value)
        {
            return parse(value);
        }
    }

    private long count;

    protected Count(final long count)
    {
        if (count < 0)
        {
            Ensure.fail("Count of $ is negative", count);
        }

        this.count = count;
    }

    protected Count()
    {
    }

    public BitCount asBitCount()
    {
        return BitCount.bitCount(asLong());
    }

    public Count asCount()
    {
        return count(get());
    }

    public Estimate asEstimate()
    {
        return Estimate.estimate(count);
    }

    public int asInt()
    {
        if (count > Integer.MAX_VALUE)
        {
            return Integer.MAX_VALUE;
        }
        return (int) count;
    }

    public long asLong()
    {
        return count;
    }

    public Maximum asMaximum()
    {
        return Maximum.maximum(count);
    }

    public Minimum asMinimum()
    {
        return Minimum.minimum(count);
    }

    @Override
    public String asString(final StringFormat format)
    {
        switch (format.identifier())
        {
            case PROGRAMMATIC_IDENTIFIER:
                return toSimpleString();

            default:
                return toString();
        }
    }

    public BitCount bitsToRepresent()
    {
        return Longs.bitsToRepresent(count);
    }

    public Count ceiling(final int digits)
    {
        return onNewInstance((get() + Ints.powerOfTen(digits)) / Ints.powerOfTen(digits) * Ints.powerOfTen(digits));
    }

    @Override
    public int compareTo(final Count that)
    {
        return Long.compare(count, that.count);
    }

    @Override
    public Count count()
    {
        return this;
    }

    public Count decremented()
    {
        return minusOne();
    }

    public Count dividedBy(final Count divisor)
    {
        return dividedBy(divisor.get());
    }

    public Count dividedBy(final long divisor)
    {
        return onNewInstance(count / divisor);
    }

    public boolean dividesEvenlyBy(final Count value)
    {
        return get() % value.get() == 0;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Count)
        {
            final var that = (Count) object;
            return count == that.count;
        }
        return false;
    }

    public Count floor(final int digits)
    {
        return onNewInstance(get() / Ints.powerOfTen(digits) * Ints.powerOfTen(digits));
    }

    public void forEachByte(final Consumer<Byte> consumer)
    {
        for (byte i = 0; i < asInt(); i++)
        {
            consumer.accept(i);
        }
    }

    public void forEachInteger(final Consumer<Integer> consumer)
    {
        for (var i = 0; i < asInt(); i++)
        {
            consumer.accept(i);
        }
    }

    public void forEachLong(final Consumer<Long> consumer)
    {
        for (long i = 0; i < get(); i++)
        {
            consumer.accept(i);
        }
    }

    public void forEachShort(final Consumer<Short> consumer)
    {
        for (short i = 0; i < asInt(); i++)
        {
            consumer.accept(i);
        }
    }

    public long get()
    {
        return count;
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(count);
    }

    public Count incremented()
    {
        return plusOne();
    }

    public boolean isEvenlyDividedBy(final Count that)
    {
        return get() % that.get() == 0;
    }

    public boolean isGreaterThan(final Count that)
    {
        return count > that.count;
    }

    public boolean isGreaterThanOrEqualTo(final Count that)
    {
        return count >= that.count;
    }

    public boolean isLessThan(final Count that)
    {
        return count < that.count;
    }

    public boolean isLessThanOrEqualTo(final Count that)
    {
        return count <= that.count;
    }

    public boolean isMaximum()
    {
        return count == MAXIMUM.get();
    }

    public void loop(final Loopable code)
    {
        loop(asInt(), code);
    }

    public void loop(final Runnable code)
    {
        loop(asInt(), iteration -> code.run());
    }

    @Override
    public Count maximum(final Count that)
    {
        if (count > that.count)
        {
            return this;
        }
        else
        {
            return that;
        }
    }

    @Override
    public Count minimum(final Count that)
    {
        if (count < that.count)
        {
            return this;
        }
        else
        {
            return that;
        }
    }

    public Count minus(final Count count)
    {
        return minus(count.get());
    }

    public Count minus(final long count)
    {
        return onNewInstance(this.count - count);
    }

    public Count minusOne()
    {
        return minus(_1);
    }

    public byte[] newByteArray()
    {
        return new byte[asInt()];
    }

    public char[] newCharArray()
    {
        return new char[asInt()];
    }

    public double[] newDoubleArray()
    {
        return new double[asInt()];
    }

    public float[] newFloatArray()
    {
        return new float[asInt()];
    }

    public int[] newIntArray()
    {
        return new int[asInt()];
    }

    public long[] newLongArray()
    {
        return new long[asInt()];
    }

    @SuppressWarnings({ "unchecked" })
    public <T> T[] newObjectArray()
    {
        return (T[]) new Object[asInt()];
    }

    public short[] newShortArray()
    {
        return new short[asInt()];
    }

    public String[] newStringArray()
    {
        return new String[asInt()];
    }

    public Count nextPrime()
    {
        return onNewInstance(Primes.allocationSize(asInt()));
    }

    public Count percent(final Percent percentage)
    {
        return onNewInstance((long) (count * percentage.asUnitValue()));
    }

    public Percent percentOf(final Count total)
    {
        if (total.isZero())
        {
            return Percent._0;
        }
        return Percent.of(count * 100.0 / total.get());
    }

    public Count plus(final Count count)
    {
        return plus(count.get());
    }

    public Count plus(final long count)
    {
        return onNewInstance(this.count + count);
    }

    public Count plusOne()
    {
        return plus(_1);
    }

    @Override
    public long quantum()
    {
        return count;
    }

    public Count roundUpToPowerOfTwo()
    {
        var rounded = 1L;
        while (rounded < count)
        {
            rounded <<= 1;
        }
        return onNewInstance(rounded);
    }

    public Count times(final Count count)
    {
        return times(count.get());
    }

    public Count times(final double multiplier)
    {
        return onNewInstance((long) (get() * multiplier));
    }

    public Count times(final long count)
    {
        final var product = this.count * count;
        if (product < 0)
        {
            return MAXIMUM;
        }
        return onNewInstance(product);
    }

    public Count times(final Percent percentage)
    {
        return times(percentage.asUnitValue());
    }

    public String toCommaSeparatedString()
    {
        return String.format("%,d", count);
    }

    public String toSimpleString()
    {
        return Long.toString(count);
    }

    @Override
    public String toString()
    {
        return toCommaSeparatedString();
    }

    protected Count onNewInstance(final long value)
    {
        return count(value);
    }
}
