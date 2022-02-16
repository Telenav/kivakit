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
 * Represents a count of something.
 *
 * <p>
 * Counts have useful properties that primitive values like <i>long</i> and <i>int</i> do not have:
 *
 * <ol>
 *     <li>Counts are guaranteed to be positive values, so it is not possible to have a {@link Count} of -1</li>
 *     <li>Counts have a variety of useful and convenient methods for:
 *     <ul>
 *         <li>Conversion</li>
 *         <li>Comparison</li>
 *         <li>Mathematics</li>
 *         <li>Looping</li>
 *         <li>Iteration</li>
 *         <li>Array allocation</li>
 *     </ul>
 *     </li>
 *     <li>Counts implement the {@link Quantizable} interface, which makes them interoperable with methods that consume {@link Quantizable}s.</li>
 *     <li>Counts provide a more readable, comma-separated String representation by default</li>
 * </ol>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Efficiency</b></p>
 *
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
 *     is a primitive value for efficiency.
 *     </li>
 * </ul>
 *
 * <p>
 * Although {@link Count} objects are convenient and make method signatures clear and type-safe, as always, the
 * best approach is to simply use {@link Count} objects until a clear inefficiency shows up in a profiler like YourKit.</li>
 * </p>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *    <li>{@link #parseCount(Listener, String)} - Parses the given string (with or without commas) as a count, calling the {@link Listener} with any problems that occur</li>
 *    <li>{@link #count(long)} - Returns the given long as a {@link Count}</li>
 *    <li>{@link #count(double)} - Casts the given double to a long and returns that as a {@link Count}</li>
 *    <li>{@link #count(Object[])} - Returns the {@link Count} of slots in the array</li>
 *    <li>{@link #count(Iterable)} - Returns the {@link Count} of items provided by the given {@link Iterable}</li>
 *    <li>{@link #count(Iterator)} - Returns the {@link Count} of items provided the given {@link Iterator}</li>
 *    <li>{@link #count(Collection)} - Returns the {@link Count} of items in the given {@link Collection}</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #asInt()} - This count cast to an <i>int</i> value</li>
 *     <li>{@link #asLong()} - This count as a <i>long</i></li>
 *     <li>{@link #get()} - This count as a <i>long</i></li>
 *     <li>{@link #count()} - This count</li>
 *     <li>{@link #asBitCount()} - This count as a {@link BitCount}</li>
 *     <li>{@link #asEstimate()} - This count as an {@link Estimate}</li>
 *     <li>{@link #asMaximum()} - This count as a {@link Maximum}</li>
 *     <li>{@link #asMinimum()} - This count as a {@link Minimum}</li>
 *     <li>{@link #quantum()} - This count as a quantum <i>long</i> value ({@link Quantizable#quantum()})</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>String Representations</b></p>
 *
 * <ul>
 *     <li>{@link #asString(StringFormat)} - This count formatted in the given format</li>
 *     <li>{@link #asCommaSeparatedString()} - This count as a comma-separated string, like 65,536</li>
 *     <li>{@link #asSimpleString()} - This count as a simple string, like 65536</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
 *     <li>{@link #compareTo(Count)} - {@link Comparable#compareTo(Object)} implementation</li>
 *     <li>{@link #isLessThan(Quantizable)} - True if this count is less than the given quantum</li>
 *     <li>{@link #isGreaterThan(Quantizable)} - True if this count is greater than the given quantum</li>
 *     <li>{@link #isLessThanOrEqualTo(Quantizable)} - True if this count is less than or equal to the given quantum</li>
 *     <li>{@link #isGreaterThanOrEqualTo(Quantizable) - True if this count is greater than or equal to the given quantum}</li>
 *     <li>{@link #isZero()} - True if this count is zero</li>
 *     <li>{@link #isNonZero()} - True if this count is not zero</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Minima and Maxima</b></p>
 *
 * <ul>
 *     <li>{@link #MAXIMUM} - The largest count possible</li>
 *     <li>{@link #MAXIMUM_BYTE_VALUE} - {@link Byte#MAX_VALUE}</li>
 *     <li>{@link #MAXIMUM_CHARACTER_VALUE} - {@link Character#MAX_VALUE}</li>
 *     <li>{@link #MAXIMUM_SHORT_VALUE} - {@link Short#MAX_VALUE}</li>
 *     <li>{@link #MAXIMUM_INTEGER_VALUE} - {@link Integer#MAX_VALUE}</li>
 *     <li>{@link #MAXIMUM_LONG_VALUE} - {@link Long#MAX_VALUE}</li>
 *     <li>{@link #isMaximum()} - True if this count is {@link #MAXIMUM}</li>
 *     <li>{@link #isMinimum()} - True if this count is zero</li>
 *     <li>{@link #asMaximum()} - Converts this count to a {@link Maximum}</li>
 *     <li>{@link #asMinimum()} - Converts this count to a {@link Minimum}</li>
 *     <li>{@link #maximum(Count)} - Returns the maximum of this count and the given count</li>
 *     <li>{@link #minimum(Count)} - Returns the minimum of this count and the given count</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Arithmetic</b></p>
 *
 * <ul>
 *     <li>{@link #decremented()} - This count minus one</li>
 *     <li>{@link #incremented()} - This count plus one</li>
 *     <li>{@link #plus(Count)} - This count plus the given count</li>
 *     <li>{@link #plus(long)} - This count plus the given value</li>
 *     <li>{@link #plusOne()} - This count plus one</li>
 *     <li>{@link #minus(Count)} - This count minus the given count</li>
 *     <li>{@link #minus(long)} - This count minus the given value</li>
 *     <li>{@link #minusOne()} - This count minus one </li>
 *     <li>{@link #dividedBy(Count)} - This count divided by the given count, using integer division without rounding</li>
 *     <li>{@link #dividedBy(long)} - This count divided by the given value, using integer division without rounding</li>
 *     <li>{@link #times(Count)} - This count times the given count</li>
 *     <li>{@link #times(long)} - This count times the given value</li>
 *     <li>{@link #times(double)} - This count times the given value, cast to a long value</li>
 *     <li>{@link #times(Percent)} - This count times the given percentage</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Mathematics</b></p>
 *
 * <ul>
 *     <li>{@link #percent(Percent)} - The given percentage of this count</li>
 *     <li>{@link #percentOf(Count)} - This count as a percentage of the given count</li>
 *     <li>{@link #dividesEvenlyBy(Count)} - True if there is no remainder when dividing this count by the given count</li>
 *     <li>{@link #ceiling(int)} - The maximum value of this count taking on the given number of digits</li>
 *     <li>{@link #floor(int)} - The minimum value of this count taking on the given number of digits</li>
 *     <li>{@link #nextPrime()} - The next prime value from a limited table of primes, useful in allocating linear hashmaps</li>
 *     <li>{@link #bitsToRepresent()} - The number of bits required to represent this count</li>
 *     <li>{@link #roundUpToPowerOfTwo()} - The next power of two above this count</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Looping</b></p>
 *
 * <ul>
 *     <li>{@link #loop(Runnable)} - Runs the given code block {@link #count()} times</li>
 *     <li>{@link #loop(Loopable)} - Runs the given code block {@link #count()} times, passing the iteration number to the code</li>
 *     <li>{@link #loop(int, Loopable)} - Static method that runs the given code block {@link #count()} times</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Iteration</b></p>
 *
 * <ul>
 *     <li>{@link #forEachByte(Consumer)} - Passes to the given consumer, each byte from 0 to the smaller of this count or {@link #MAXIMUM_BYTE_VALUE}, exclusive</li>
 *     <li>{@link #forEachInteger(Consumer)} - Passes to the given consumer, each byte from 0 to the smaller of this count or {@link #MAXIMUM_INTEGER_VALUE}, exclusive</li>
 *     <li>{@link #forEachLong(Consumer)} - Passes each long from 0 to {@link #asLong()} to the given consumer, exclusive</li>
 *     <li>{@link #forEachShort(Consumer)} - Passes to the given consumer, each byte from 0 to the smaller of this count or {@link #MAXIMUM_SHORT_VALUE}, exclusive</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Array Allocation</b></p>
 *
 * <ul>
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
 * <p>
 * {@link Count} objects implement the {@link #hashCode()} / {@link #equals(Object)} contract and are {@link Comparable}.
 *
 * <p><br/><hr/><br/></p>
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

    public static final Count MAXIMUM_CHARACTER_VALUE = new Count(Character.MAX_VALUE);

    public static final Count MAXIMUM_LONG_VALUE = new Count(Long.MAX_VALUE);

    public static final Count MAXIMUM_INTEGER_VALUE = new Count(Integer.MAX_VALUE);

    public static final Count MAXIMUM_SHORT_VALUE = new Count(Short.MAX_VALUE);

    public static final Count MAXIMUM_BYTE_VALUE = new Count(Byte.MAX_VALUE);

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

    public static Count count(Collection<?> collection)
    {
        return count(collection.size());
    }

    public static Count count(Iterable<?> iterable)
    {
        Ensure.ensure(!(iterable instanceof Count));
        return count(iterable.iterator());
    }

    public static Count count(Iterable<?> iterable, Maximum maximum)
    {
        return count(iterable.iterator(), maximum);
    }

    public static Count count(Iterator<?> iterator)
    {
        var count = 0L;
        while (iterator.hasNext())
        {
            iterator.next();
            count++;
        }
        return count(count);
    }

    public static Count count(Iterator<?> iterator, Maximum maximum)
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

    public static Count count(long value)
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

    public static <T> Count count(T[] values)
    {
        return count(values.length);
    }

    public static void loop(int times, Loopable code)
    {
        for (var iteration = 0; iteration < times; iteration++)
        {
            code.iteration(times);
        }
    }

    public static Count parseCount(Listener listener, String value)
    {
        var count = Longs.parseFast(value, -1);
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
        public Converter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected Count onToValue(String value)
        {
            return parseCount(this, value);
        }
    }

    private long count;

    protected Count(long count)
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

    public String asCommaSeparatedString()
    {
        return String.format("%,d", count);
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

    public String asSimpleString()
    {
        return Long.toString(count);
    }

    @Override
    public String asString(StringFormat format)
    {
        switch (format.identifier())
        {
            case PROGRAMMATIC_IDENTIFIER:
                return asSimpleString();

            default:
                return toString();
        }
    }

    public BitCount bitsToRepresent()
    {
        return Longs.bitsToRepresent(count);
    }

    public Count ceiling(int digits)
    {
        return onNewInstance((get() + Ints.powerOfTen(digits)) / Ints.powerOfTen(digits) * Ints.powerOfTen(digits));
    }

    @Override
    public int compareTo(Count that)
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

    public Count dividedBy(Count divisor)
    {
        return dividedBy(divisor.get());
    }

    public Count dividedBy(long divisor)
    {
        return onNewInstance(count / divisor);
    }

    public boolean dividesEvenlyBy(Count value)
    {
        return get() % value.get() == 0;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Count)
        {
            var that = (Count) object;
            return count == that.count;
        }
        return false;
    }

    public Count floor(int digits)
    {
        return onNewInstance(get() / Ints.powerOfTen(digits) * Ints.powerOfTen(digits));
    }

    public void forEachByte(Consumer<Byte> consumer)
    {
        for (byte i = 0; i < minimum(MAXIMUM_BYTE_VALUE).asInt(); i++)
        {
            consumer.accept(i);
        }
    }

    public void forEachInteger(Consumer<Integer> consumer)
    {
        for (var i = 0; i < minimum(MAXIMUM_INTEGER_VALUE).asInt(); i++)
        {
            consumer.accept(i);
        }
    }

    public void forEachLong(Consumer<Long> consumer)
    {
        for (long i = 0; i < get(); i++)
        {
            consumer.accept(i);
        }
    }

    public void forEachShort(Consumer<Short> consumer)
    {
        for (short i = 0; i < minimum(MAXIMUM_SHORT_VALUE).asInt(); i++)
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

    public boolean isGreaterThan(Count that)
    {
        return count > that.count;
    }

    public boolean isGreaterThanOrEqualTo(Count that)
    {
        return count >= that.count;
    }

    public boolean isLessThan(Count that)
    {
        return count < that.count;
    }

    public boolean isLessThanOrEqualTo(Count that)
    {
        return count <= that.count;
    }

    public boolean isMaximum()
    {
        return count == MAXIMUM.get();
    }

    public boolean isMinimum()
    {
        return count == 0;
    }

    public void loop(Loopable code)
    {
        loop(asInt(), code);
    }

    public void loop(Runnable code)
    {
        loop(asInt(), iteration -> code.run());
    }

    @Override
    public Count maximum(Count that)
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
    public Count minimum(Count that)
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

    public Count minus(Count count)
    {
        return minus(count.get());
    }

    public Count minus(long count)
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

    public Count percent(Percent percentage)
    {
        return onNewInstance((long) (count * percentage.asUnitValue()));
    }

    public Percent percentOf(Count total)
    {
        if (total.isZero())
        {
            return Percent._0;
        }
        return Percent.of(count * 100.0 / total.get());
    }

    public Count plus(Count count)
    {
        return plus(count.get());
    }

    public Count plus(long count)
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

    public Count times(Count count)
    {
        return times(count.get());
    }

    public Count times(double multiplier)
    {
        return onNewInstance((long) (get() * multiplier));
    }

    public Count times(long count)
    {
        var product = this.count * count;
        if (product < 0)
        {
            return MAXIMUM;
        }
        return onNewInstance(product);
    }

    public Count times(Percent percentage)
    {
        return times(percentage.asUnitValue());
    }

    @Override
    public String toString()
    {
        return asCommaSeparatedString();
    }

    protected Count onNewInstance(long value)
    {
        return count(value);
    }
}
