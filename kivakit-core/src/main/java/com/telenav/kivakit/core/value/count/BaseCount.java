package com.telenav.kivakit.core.value.count;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.core.language.primitive.Longs;
import com.telenav.kivakit.core.language.primitive.Primes;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.interfaces.numeric.Numeric;
import com.telenav.kivakit.interfaces.value.DoubleValued;
import com.telenav.kivakit.interfaces.value.FormattedLongValued;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.kivakit.interfaces.value.Source;

import java.util.function.Consumer;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.value.count.BitCount.bits;
import static com.telenav.kivakit.core.value.count.Estimate.estimate;
import static java.lang.Math.min;

/**
 * Base class for classes that represent some kind of cardinal number. Each method in this class works for all subclass
 * types. Instances of the subclass are created with {@link #onNewInstance(long)}. If a subclass wishes to store
 * information in its own field, it can override {@link #asLong()}.
 *
 * <p>
 * Counts have useful properties that primitive values like <i>long</i> and <i>int</i> do not have:
 * </p>
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
 *     <li>Counts implement the {@link LongValued} interface, which makes them interoperable with methods that consume {@link LongValued} objects.</li>
 *     <li>Counts provide a more readable, comma-separated String representation by default</li>
 * </ol>
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
 * best approach is to simply use {@link Count} objects until a clear inefficiency shows up in a profiler like YourKit.
 * </p>
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
 * </ul>
 *
 * <hr>
 *
 * <p><b>String Representations</b></p>
 *
 * <ul>
 *     <li>{@link #asString(Format)} - This count formatted in the given format</li>
 * </ul>
 *
 * <hr>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
 *     <li>{@link #compareTo(Countable)} - {@link Comparable#compareTo(Object)} implementation</li>
 *     <li>{@link #isLessThan(LongValued)} - True if this count is less than the given quantum</li>
 *     <li>{@link #isGreaterThan(LongValued)} - True if this count is greater than the given quantum</li>
 *     <li>{@link #isLessThanOrEqualTo(LongValued)} - True if this count is less than or equal to the given quantum</li>
 *     <li>{@link #isGreaterThanOrEqualTo(LongValued) - True if this count is greater than or equal to the given quantum}</li>
 *     <li>{@link #isZero()} - True if this count is zero</li>
 *     <li>{@link #isNonZero()} - True if this count is not zero</li>
 * </ul>
 *
 * <hr>
 *
 * <p><b>Minima and Maxima</b></p>
 *
 * <ul>
 *     <li>{@link #isMaximum()} - True if this count is {@link #maximum()}</li>
 *     <li>{@link #isMinimum()} - True if this count is zero</li>
 *     <li>{@link #asMaximum()} - Converts this count to a {@link Maximum}</li>
 *     <li>{@link #asMinimum()} - Converts this count to a {@link Minimum}</li>
 *     <li>{@link #maximize(LongValued)} - Returns the maximum of this count and the given count</li>
 *     <li>{@link #minimize(LongValued)} - Returns the minimum of this count and the given count</li>
 * </ul>
 *
 * <p><b>Arithmetic</b></p>
 *
 * <ul>
 *     <li>{@link #decremented()} - This count minus one</li>
 *     <li>{@link #incremented()} - This count plus one</li>
 *     <li>{@link #plus(LongValued)} - This count plus the given count</li>
 *     <li>{@link #plus(long)} - This count plus the given value</li>
 *     <li>{@link #minus(LongValued)} - This count minus the given count</li>
 *     <li>{@link #minus(long)} - This count minus the given value</li>
 *     <li>{@link #dividedBy(LongValued)} - This count divided by the given count, using integer division without rounding</li>
 *     <li>{@link #dividedBy(long)} - This count divided by the given value, using integer division without rounding</li>
 *     <li>{@link #times(LongValued)} - This count times the given count</li>
 *     <li>{@link #times(long)} - This count times the given value</li>
 *     <li>{@link #times(double)} - This count times the given value, cast to a long value</li>
 *     <li>{@link #times(Percent)} - This count times the given percentage</li>
 * </ul>
 *
 * <p><b>Mathematics</b></p>
 *
 * <ul>
 *     <li>{@link #percent(Percent)} - The given percentage of this count</li>
 *     <li>{@link #percentOf(LongValued)} - This count as a percentage of the given count</li>
 *     <li>{@link #dividesEvenlyBy(LongValued)} - True if there is no remainder when dividing this count by the given count</li>
 *     <li>{@link #powerOfTenCeiling(int)} - The maximum value of this count taking on the given number of digits</li>
 *     <li>{@link #powerOfTenFloor(int)} - The minimum value of this count taking on the given number of digits</li>
 *     <li>{@link #asPrimeAllocationSize()} - The next prime value from a limited table of primes, useful in allocating linear hashmaps</li>
 *     <li>{@link #bitsToRepresent()} - The number of bits required to represent this count</li>
 *     <li>{@link #powerOfTwoCeiling()} - The next power of two above this count</li>
 * </ul>
 *
 * <p><b>Looping</b></p>
 *
 * <ul>
 *     <li>{@link #loop(Runnable)} - Runs the given code block {@link #count()} times</li>
 * </ul>
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
 * @param <SubClass> The subclass type
 * @author jonathanl (shibo)
 * @see LongValued
 * @see Countable
 * @see Comparable
 * @see Estimate
 * @see Maximum
 * @see Minimum
 */
@SuppressWarnings({ "unused", "unchecked" })
@ApiQuality(stability = STABLE_EXPANDABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public abstract class BaseCount<SubClass extends BaseCount<SubClass>> implements
        FormattedLongValued,
        Numeric<SubClass>,
        Countable,
        Comparable<Countable>,
        Source<Long>,
        DoubleValued
{
    /** The underlying primitive cardinal number */
    private final long count;

    protected BaseCount()
    {
        this.count = 0;
    }

    protected BaseCount(long count)
    {
        ensure(count >= 0, "Count of " + count + " is negative");

        this.count = count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double absoluteDifference(DoubleValued that)
    {
        return DoubleValued.super.absoluteDifference(that);
    }

    /**
     * Returns this count as a {@link BitCount}
     */
    public BitCount asBitCount()
    {
        return bits(asLong());
    }

    /**
     * Returns this count as a {@link Count}
     */
    public Count asCount()
    {
        return Count.count(asLong());
    }

    /**
     * Returns this count as an {@link Estimate}
     */
    public Estimate asEstimate()
    {
        return estimate(asLong());
    }

    /**
     * Returns this count as an int
     */
    @Override
    public int asInt()
    {
        var value = asLong();
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }

    /**
     * Returns this count as a {@link Maximum}
     */
    public Maximum asMaximum()
    {
        return Maximum.maximum(asLong());
    }

    /**
     * Returns this count as a {@link Minimum}
     */
    public Minimum asMinimum()
    {
        return Minimum.minimum(asLong());
    }

    /**
     * Returns the next prime value above this count
     */
    public SubClass asPrimeAllocationSize()
    {
        return onNewInstance(Primes.primeAllocationSize(asInt()));
    }

    /**
     * Returns the number of bits required to represent this count value
     */
    public BitCount bitsToRepresent()
    {
        return BitCount.bitsToRepresent(asLong());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Countable that)
    {
        return Long.compare(longValue(), that.count().longValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Count count()
    {
        return Count.count(asLong());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass decremented()
    {
        return Numeric.super.decremented();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass dividedBy(long divisor)
    {
        return inRangeExclusive(asLong() / divisor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass dividedBy(LongValued value)
    {
        return Numeric.super.dividedBy(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dividesEvenlyBy(LongValued value)
    {
        return Numeric.super.dividesEvenlyBy(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Comparable<DoubleValued> doubleComparable()
    {
        return DoubleValued.super.doubleComparable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double doubleValue()
    {
        return asDouble();
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Calls the given consumer with each signed byte value from 0 to this count, exclusive
     */
    public void forEachByte(Consumer<Byte> consumer)
    {
        for (byte i = 0; i < min(Byte.MAX_VALUE, asByte()); i++)
        {
            consumer.accept(i);
        }
    }

    /**
     * Calls the given consumer with each integer from 0 to this count, exclusive
     */
    public void forEachInteger(Consumer<Integer> consumer)
    {
        for (var i = 0; i < asInt(); i++)
        {
            consumer.accept(i);
        }
    }

    /**
     * Calls the given consumer with each long value from 0 to this count, exclusive
     */
    public void forEachLong(Consumer<Long> consumer)
    {
        for (long i = 0; i < asLong(); i++)
        {
            consumer.accept(i);
        }
    }

    /**
     * Calls the given consumer with each short value from 0 to this count, exclusive
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Long.hashCode(asLong());
    }

    /**
     * Returns a count between 0 and this count, exclusive. If the given value is out of range, returns null.
     */
    public SubClass inRangeExclusive(long value)
    {
        if (value >= maximum().asLong())
        {
            return null;
        }
        if (value < 0)
        {
            return null;
        }
        return onNewInstance(value);
    }

    /**
     * Returns a count between 0 and this count, inclusive. If the given value is out of range, returns null.
     */
    public SubClass inRangeInclusive(long value)
    {
        if (value > maximum().asLong())
        {
            return null;
        }
        if (value < 0)
        {
            return null;
        }
        return onNewInstance(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass incremented()
    {
        return Numeric.super.incremented();
    }

    /**
     * Returns true if this count is between the given minimum and exclusive maximum
     */
    public boolean isBetweenExclusive(BaseCount<?> minimum, BaseCount<?> exclusiveMaximum)
    {
        return Longs.isBetweenExclusive(asLong(), minimum.asLong(), exclusiveMaximum.asLong());
    }

    /**
     * Returns true if this count is between the given minimum and inclusive maximum
     */
    public boolean isBetweenInclusive(BaseCount<?> minimum, BaseCount<?> inclusiveMaximum)
    {
        return Longs.isBetweenInclusive(asLong(), minimum.asLong(), inclusiveMaximum.asLong());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCloseTo(DoubleValued that, DoubleValued within)
    {
        return DoubleValued.super.isCloseTo(that, within);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCloseTo(DoubleValued that, double tolerance)
    {
        return DoubleValued.super.isCloseTo(that, tolerance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreaterThan(DoubleValued that)
    {
        return DoubleValued.super.isGreaterThan(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGreaterThanOrEqualTo(DoubleValued that)
    {
        return DoubleValued.super.isGreaterThanOrEqualTo(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessThan(DoubleValued that)
    {
        return DoubleValued.super.isLessThan(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessThanOrEqualTo(DoubleValued that)
    {
        return DoubleValued.super.isLessThanOrEqualTo(that);
    }

    /**
     * Returns true if this count is the maximum value
     */
    public boolean isMaximum()
    {
        return asLong() == maximum().asLong();
    }

    /**
     * Returns true if this count is the minimum value
     */
    public boolean isMinimum()
    {
        return asLong() == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isZero()
    {
        return DoubleValued.super.isZero();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long longValue()
    {
        return count;
    }

    /**
     * Runs the given code this number of times
     */
    public void loop(Runnable code)
    {
        for (var iteration = 0; iteration < asLong(); iteration++)
        {
            code.run();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass maximize(long value)
    {
        return Numeric.super.maximize(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass maximize(LongValued value)
    {
        return Numeric.super.maximize(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass maximum()
    {
        return onNewInstance(Long.MAX_VALUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass minimize(long value)
    {
        return Numeric.super.minimize(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass minimize(LongValued value)
    {
        return Numeric.super.minimize(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass minimum()
    {
        return inRangeExclusive(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass minus(long value)
    {
        return Numeric.super.minus(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass minus(LongValued value)
    {
        return Numeric.super.minus(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass modulo(long value)
    {
        return Numeric.super.modulo(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass modulo(LongValued value)
    {
        return Numeric.super.modulo(value);
    }

    /**
     * Returns a new byte array with this number of elements
     */
    public byte[] newByteArray()
    {
        return new byte[asInt()];
    }

    /**
     * Returns a new char array with this number of elements
     */
    public char[] newCharArray()
    {
        return new char[asInt()];
    }

    /**
     * Returns a new double array with this number of elements
     */
    public double[] newDoubleArray()
    {
        return new double[asInt()];
    }

    /**
     * Returns a new float array with this number of elements
     */
    public float[] newFloatArray()
    {
        return new float[asInt()];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SubClass newInstance(Long count)
    {
        return onNewInstance(count.intValue());
    }

    /**
     * Returns a new int array with this number of elements
     */
    public int[] newIntArray()
    {
        return new int[asInt()];
    }

    /**
     * Returns a new float array with this number of elements
     */
    public long[] newLongArray()
    {
        return new long[asInt()];
    }

    /**
     * Returns a new object array with this number of elements
     */
    @SuppressWarnings({ "unchecked" })
    public <Element> Element[] newObjectArray()
    {
        return (Element[]) new Object[asInt()];
    }

    /**
     * Returns a new short array with this number of elements
     */
    public short[] newShortArray()
    {
        return new short[asInt()];
    }

    /**
     * Returns a new string array with this number of elements
     */
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

    /**
     * Returns the next value, but wraps around to {@link #minimum()} when {@link #next()} returns null.
     */
    public SubClass nextWrapping()
    {
        var next = next();
        return next == null ? minimum() : next;
    }

    /**
     * Returns this count offset by the given offset, or null if the offset is invalid
     */
    public SubClass offset(long offset)
    {
        return plus(offset);
    }

    /**
     * Returns a new instance of the concrete subclass of this abstract class.
     *
     * @param count The count value
     * @return The new instance
     */
    public abstract SubClass onNewInstance(long count);

    /**
     * Returns the given percentage of this count
     */
    public SubClass percent(Percent percentage)
    {
        return inRangeExclusive((long) (asLong() * percentage.asUnitValue()));
    }

    /**
     * Returns percentage that this count represents of the given count
     */
    public Percent percentOf(LongValued that)
    {
        return percentOf(that.longValue());
    }

    /**
     * Returns percentage that this count represents of the given count
     */
    public Percent percentOf(long total)
    {
        if (total == 0)
        {
            return Percent._0;
        }
        return Percent.percent(asLong() * 100.0 / total);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass plus(long count)
    {
        return inRangeExclusive(asLong() + count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass plus(LongValued value)
    {
        return Numeric.super.plus(value);
    }

    /**
     * Returns the next higher power of ten with the given number of digits. For example, if this is a count of 6700,
     * and digits is 5, then the result will be 6700 + 10_000 = 16,700 / 10,000 = 1 * 10,000 = 10,000.
     *
     * @param digits The number of digits
     */
    public SubClass powerOfTenCeiling(int digits)
    {
        return inRangeExclusive((asLong() + Ints.powerOfTen(digits))
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
        return inRangeExclusive(asLong() / Ints.powerOfTen(digits) * Ints.powerOfTen(digits));
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
        return onNewInstance(rounded);
    }

    /**
     * Returns this count times the given multiplier
     */
    public SubClass times(double multiplier)
    {
        return inRangeExclusive((long) (get() * multiplier));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass times(long multiplier)
    {
        return inRangeExclusive(asLong() * multiplier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubClass times(LongValued value)
    {
        return Numeric.super.times(value);
    }

    /**
     * Returns the given percentage of this count
     */
    public SubClass times(Percent percentage)
    {
        return times(percentage.asUnitValue());
    }

    /**
     * {@inheritDoc}
     */
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
