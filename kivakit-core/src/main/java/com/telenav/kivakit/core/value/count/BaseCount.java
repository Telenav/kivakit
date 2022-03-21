package com.telenav.kivakit.core.value.count;

import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.core.language.primitive.Longs;
import com.telenav.kivakit.core.language.primitive.Primes;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.interfaces.code.LoopBody;
import com.telenav.kivakit.interfaces.code.FilteredLoopBody;
import com.telenav.kivakit.interfaces.code.FilteredLoopBody.FilterAction;
import com.telenav.kivakit.interfaces.numeric.IntegerNumeric;
import com.telenav.kivakit.interfaces.numeric.Quantizable;
import com.telenav.kivakit.interfaces.value.Source;

import java.util.function.Consumer;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.value.count.BitCount.bitCount;
import static com.telenav.kivakit.core.value.count.Estimate.estimate;

/**
 * Base class for classes that represent some kind of cardinal number. Each method in this class works for all subclass
 * types. Instances of the subclass are created with {@link #newInstance(long)}. If a subclass wishes to store
 * information in its own field, it can override {@link #asLong()}.
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
 *     <li>(1) low count constants, and powers of two and ten can be accessed as constant objects in subclasses
 *     and values up to 65,536 are cached by subclass.
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
 *     <li>{@link #asString(Format)} - This count formatted in the given format</li>
 *     <li>{@link #asCommaSeparatedString()} - This count as a comma-separated string, like 65,536</li>
 *     <li>{@link #asSimpleString()} - This count as a simple string, like 65536</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
 *     <li>{@link #compareTo(SubClass)} - {@link Comparable#compareTo(Object)} implementation</li>
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
 *     <li>{@link #isMaximum()} - True if this count is {@link #maximum()}</li>
 *     <li>{@link #isMinimum()} - True if this count is zero</li>
 *     <li>{@link #asMaximum()} - Converts this count to a {@link Maximum}</li>
 *     <li>{@link #asMinimum()} - Converts this count to a {@link Minimum}</li>
 *     <li>{@link #maximum(SubClass)} - Returns the maximum of this count and the given count</li>
 *     <li>{@link #minimum(SubClass)} - Returns the minimum of this count and the given count</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Arithmetic</b></p>
 *
 * <ul>
 *     <li>{@link #decremented()} - This count minus one</li>
 *     <li>{@link #incremented()} - This count plus one</li>
 *     <li>{@link #plus(Quantizable)} - This count plus the given count</li>
 *     <li>{@link #plus(long)} - This count plus the given value</li>
 *     <li>{@link #minus(Quantizable)} - This count minus the given count</li>
 *     <li>{@link #minus(long)} - This count minus the given value</li>
 *     <li>{@link #dividedBy(Quantizable)} - This count divided by the given count, using integer division without rounding</li>
 *     <li>{@link #dividedBy(long)} - This count divided by the given value, using integer division without rounding</li>
 *     <li>{@link #times(Quantizable)} - This count times the given count</li>
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
 *     <li>{@link #percentOf(Quantizable)} - This count as a percentage of the given count</li>
 *     <li>{@link #dividesEvenlyBy(Quantizable)} - True if there is no remainder when dividing this count by the given count</li>
 *     <li>{@link #powerOfTenCeiling(int)} - The maximum value of this count taking on the given number of digits</li>
 *     <li>{@link #powerOfTenFloor(int)} - The minimum value of this count taking on the given number of digits</li>
 *     <li>{@link #nextPrime()} - The next prime value from a limited table of primes, useful in allocating linear hashmaps</li>
 *     <li>{@link #bitsToRepresent()} - The number of bits required to represent this count</li>
 *     <li>{@link #powerOfTwoCeiling()} - The next power of two above this count</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Looping</b></p>
 *
 * <ul>
 *     <li>{@link #loop(Runnable)} - Runs the given code block {@link #count()} times</li>
 *     <li>{@link #loop(LoopBody)} - Runs the given code block {@link #count()} times, passing the iteration number to the code</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Iteration</b></p>
 *
 * <ul>
 *     <li>{@link #forEachByte(Consumer)} - Passes to the given consumer, each byte from 0 to the smaller of this count or {@link Byte#MAX_VALUE}, exclusive</li>
 *     <li>{@link #forEachInteger(Consumer)} - Passes to the given consumer, each byte from 0 to the smaller of this count or {@link Integer#MAX_VALUE}, exclusive</li>
 *     <li>{@link #forEachLong(Consumer)} - Passes each long from 0 to {@link #asLong()} to the given consumer, exclusive</li>
 *     <li>{@link #forEachShort(Consumer)} - Passes to the given consumer, each byte from 0 to the smaller of this count or {@link Short#MAX_VALUE}, exclusive</li>
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
 * @param <SubClass> The subclass type
 * @author jonathanl (shibo)
 * @see Quantizable
 * @see Countable
 * @see Comparable
 * @see Estimate
 * @see Maximum
 * @see Minimum
 */
public abstract class BaseCount<SubClass extends BaseCount<SubClass>> implements
        IntegerNumeric<SubClass>,
        Countable,
        Source<Long>
{
    /** The underlying primitive cardinal number */
    private final long count;

    public BaseCount()
    {
        this.count = 0;
    }

    protected BaseCount(long count)
    {
        ensure(count >= 0, "Count of " + count + " is negative");

        this.count = count;
    }

    public BitCount asBitCount()
    {
        return bitCount(asLong());
    }

    public Count asCount()
    {
        return Count.count(asLong());
    }

    public Estimate asEstimate()
    {
        return estimate(asLong());
    }

    public int asInt()
    {
        var value = asLong();
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }

    public long asLong()
    {
        return count;
    }

    public Maximum asMaximum()
    {
        return Maximum.maximum(asLong());
    }

    public Minimum asMinimum()
    {
        return Minimum.minimum(asLong());
    }

    public BitCount bitsToRepresent()
    {
        return BitCount.bitsToRepresent(asLong());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(SubClass that)
    {
        return Long.compare(asLong(), that.asLong());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Count count()
    {
        return Count.count(asLong());
    }

    public SubClass decremented()
    {
        return offset(-1);
    }

    @Override
    public SubClass dividedBy(SubClass divisor)
    {
        return dividedBy(divisor.asLong());
    }

    public SubClass dividedBy(Quantizable divisor)
    {
        return dividedBy(divisor.quantum());
    }

    public SubClass dividedBy(long divisor)
    {
        return inRange(asLong() / divisor);
    }

    public boolean dividesEvenlyBy(Quantizable value)
    {
        return asLong() % value.quantum() == 0;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof BaseCount)
        {
            var that = (BaseCount<?>) object;
            return asLong() == that.asLong();
        }
        return false;
    }

    public void forEach(Consumer<SubClass> consumer)
    {
        for (var value = minimum(); value.isLessThan(this); value = value.next())
        {
            consumer.accept(value);
        }
    }

    public void forEachByte(Consumer<Byte> consumer)
    {
        for (byte i = 0; i < maximum().asInt(); i++)
        {
            consumer.accept(i);
        }
    }

    public void forEachInteger(Consumer<Integer> consumer)
    {
        for (var i = 0; i < maximum().asInt(); i++)
        {
            consumer.accept(i);
        }
    }

    public void forEachLong(Consumer<Long> consumer)
    {
        for (long i = 0; i < asLong(); i++)
        {
            consumer.accept(i);
        }
    }

    public void forEachShort(Consumer<Short> consumer)
    {
        for (short i = 0; i < maximum().asInt(); i++)
        {
            consumer.accept(i);
        }
    }

    /**
     * Implements {@link Source#get()} to return this count as a Long
     */
    @Override
    public Long get()
    {
        return asLong();
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(asLong());
    }

    public SubClass inRange(long value)
    {
        if (value > maximum().asLong())
        {
            return null;
        }
        if (value < 0)
        {
            return null;
        }
        return newInstance(value);
    }

    public SubClass incremented()
    {
        return offset(1);
    }

    public boolean isBetweenExclusive(BaseCount<?> minimum, BaseCount<?> exclusiveMaximum)
    {
        return Longs.isBetweenExclusive(asLong(), minimum.asLong(), exclusiveMaximum.asLong());
    }

    public boolean isBetweenInclusive(BaseCount<?> minimum, BaseCount<?> inclusiveMaximum)
    {
        return Longs.isBetweenInclusive(asLong(), minimum.asLong(), inclusiveMaximum.asLong());
    }

    public boolean isGreaterThan(Quantizable that)
    {
        return asLong() > that.quantum();
    }

    public boolean isGreaterThanOrEqualTo(Quantizable that)
    {
        return asLong() >= that.quantum();
    }

    public boolean isLessThan(Quantizable that)
    {
        return asLong() < that.quantum();
    }

    public boolean isLessThanOrEqualTo(Quantizable that)
    {
        return asLong() <= that.quantum();
    }

    public boolean isMaximum()
    {
        return asLong() == maximum().asLong();
    }

    public boolean isMinimum()
    {
        return asLong() == 0;
    }

    @Override
    public boolean isNonZero()
    {
        return asLong() != 0;
    }

    @Override
    public boolean isZero()
    {
        return asLong() == 0;
    }

    public void loop(LoopBody<SubClass> body)
    {
        var maximum = this;
        for (var at = minimum(); at.isLessThan(maximum); at = at.next())
        {
            body.at(at);
        }
    }

    public void loop(FilteredLoopBody<SubClass> body)
    {
        var maximum = this;
        for (var at = minimum(); at.isLessThan(maximum); at = at.next())
        {

            if (body.at(at) == FilterAction.REJECT)
            {
                maximum = maximum.incremented();
            }
        }
    }

    public void loop(Runnable code)
    {
        for (var iteration = 0; iteration < asLong(); iteration++)
        {
            code.run();
        }
    }

    public void loopInclusive(LoopBody<SubClass> code)
    {
        for (var at = minimum(); at.isLessThanOrEqualTo(this); at = at.next())
        {
            code.at(at);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public SubClass maximum(SubClass that)
    {
        if (asLong() > that.asLong())
        {
            return (SubClass) this;
        }
        else
        {
            return that;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass maximum()
    {
        return newInstance(Long.MAX_VALUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public SubClass minimum(SubClass that)
    {
        if (asLong() < that.asLong())
        {
            return (SubClass) this;
        }
        else
        {
            return that;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass minimum()
    {
        return inRange(0);
    }

    @Override
    public SubClass minus(SubClass count)
    {
        return minus(count.asLong());
    }

    public SubClass minus(Quantizable count)
    {
        return minus(count.quantum());
    }

    public SubClass minus(long count)
    {
        return inRange(asLong() - count);
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

    /**
     * Returns a new instance of the concrete subclass of this abstract class.
     *
     * @param count The count value
     * @return The new instance
     */
    public abstract SubClass newInstance(long count);

    public int[] newIntArray()
    {
        return new int[asInt()];
    }

    public long[] newLongArray()
    {
        return new long[asInt()];
    }

    @SuppressWarnings({ "unchecked" })
    public <Element> Element[] newObjectArray()
    {
        return (Element[]) new Object[asInt()];
    }

    public short[] newShortArray()
    {
        return new short[asInt()];
    }

    public String[] newStringArray()
    {
        return new String[asInt()];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass next()
    {
        return offset(1);
    }

    public SubClass nextPrime()
    {
        return newInstance(Primes.primeAllocationSize(asInt()));
    }

    public SubClass offset(long offset)
    {
        return inRange(asLong() + offset);
    }

    public SubClass percent(Percent percentage)
    {
        return inRange((long) (asLong() * percentage.asUnitValue()));
    }

    public Percent percentOf(Quantizable total)
    {
        if (total.isZero())
        {
            return Percent._0;
        }
        return Percent.of(asLong() * 100.0 / total.quantum());
    }

    @Override
    public SubClass plus(SubClass count)
    {
        return plus(count.asLong());
    }

    public SubClass plus(Quantizable count)
    {
        return plus(count.quantum());
    }

    public SubClass plus(long count)
    {
        return inRange(asLong() + count);
    }

    /**
     * Returns the next higher power of ten with the given number of digits. For example, if this is a count of 6700,
     * and digits is 5, then the result will be 6700 + 10_000 = 16,700 / 10,000 = 1 * 10,000 = 10,000.
     *
     * @param digits The number of digits
     */
    public SubClass powerOfTenCeiling(int digits)
    {
        return inRange((asLong() + Ints.powerOfTen(digits))
                / Ints.powerOfTen(digits)
                * Ints.powerOfTen(digits));
    }

    /**
     * Returns the next lower power of ten with the given number of digits. For example, if this is a count of 16,700,
     * and digits is 5, then the result will be 16,700 / 10,000 = 1 * 10,000 = 10,000.
     *
     * @param digits The number of digits
     */
    public SubClass powerOfTenFloor(int digits)
    {
        return inRange(asLong() / Ints.powerOfTen(digits) * Ints.powerOfTen(digits));
    }

    /**
     * Rounds this count up to the next higher por of two
     */
    public SubClass powerOfTwoCeiling()
    {
        var rounded = 1L;
        while (rounded < asLong())
        {
            rounded <<= 1;
        }
        return newInstance(rounded);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long quantum()
    {
        return asLong();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        return asInt();
    }

    @Override
    public SubClass times(SubClass count)
    {
        return times(count.asLong());
    }

    public SubClass times(Quantizable count)
    {
        return times(count.quantum());
    }

    public SubClass times(double multiplier)
    {
        return inRange((long) (get() * multiplier));
    }

    public SubClass times(long multiplier)
    {
        return inRange(asLong() * multiplier);
    }

    public SubClass times(Percent percentage)
    {
        return times(percentage.asUnitValue());
    }

    public Range<SubClass> toExclusive(SubClass maximum)
    {
        return Range.exclusive(asSubclassType(), maximum);
    }

    public Range<SubClass> toInclusive(SubClass maximum)
    {
        return Range.inclusive(asSubclassType(), maximum);
    }

    @Override
    public String toString()
    {
        return asCommaSeparatedString();
    }

    @SuppressWarnings("unchecked")
    private SubClass asSubclassType()
    {
        return (SubClass) this;
    }
}
