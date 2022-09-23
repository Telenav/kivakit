package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.interfaces.numeric.Zeroable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.NONE;
import static java.lang.Math.addExact;
import static java.lang.Math.subtractExact;

/**
 * Accurate model of a number of nanoseconds, represented as a number of seconds and a number of nanoseconds. This
 * yields the same precision is the same as in Java time.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("DuplicatedCode")
@CodeQuality(stability = STABLE,
             testing = NONE,
             documentation = SUFFICIENT)
public class Nanoseconds implements
        Comparable<Nanoseconds>,
        Zeroable
{
    /** The number of nanoseconds in a second */
    private static final long NANOSECONDS_PER_SECOND = (long) 1E9;

    /** Zero nanoseconds */
    public static Nanoseconds ZERO = nanoseconds(0);

    /** One nanosecond */
    public static Nanoseconds ONE = nanoseconds(1);

    /** One nanosecond */
    public static Nanoseconds ONE_SECOND = nanoseconds(NANOSECONDS_PER_SECOND);

    /**
     * Factory method
     *
     * @param milliseconds The number of nanoseconds
     * @return The new {@link Nanoseconds} object
     */
    public static Nanoseconds milliseconds(double milliseconds)
    {
        return nanoseconds(milliseconds * 1E6);
    }

    /**
     * Factory method
     *
     * @param nanoseconds The number of nanoseconds
     * @return The new {@link Nanoseconds} object
     */
    public static Nanoseconds nanoseconds(long nanoseconds)
    {
        return new Nanoseconds(nanoseconds / NANOSECONDS_PER_SECOND, nanoseconds % NANOSECONDS_PER_SECOND);
    }

    /**
     * Factory method
     *
     * @param nanoseconds The number of nanoseconds
     * @return The new {@link Nanoseconds} object
     */
    public static Nanoseconds nanoseconds(double nanoseconds)
    {
        return nanoseconds((long) (nanoseconds / 1E9), (long) (nanoseconds % NANOSECONDS_PER_SECOND));
    }

    /**
     * Factory method
     *
     * @param nanoseconds The number of nanoseconds
     * @return The new {@link Nanoseconds} object
     */
    public static Nanoseconds nanoseconds(BigDecimal nanoseconds)
    {
        return new Nanoseconds(
                (long) nanoseconds.divide(BigDecimal.valueOf(NANOSECONDS_PER_SECOND), RoundingMode.DOWN).doubleValue(),
                nanoseconds.remainder(BigDecimal.valueOf(NANOSECONDS_PER_SECOND)).longValueExact());
    }

    /**
     * Factory method that constructs from a number of seconds and a number of nanoseconds. If the nanoseconds value is
     * larger than 1E9, any excess seconds are moved into the seconds value.
     *
     * @param seconds The number of seconds
     * @param nanoseconds The number of nanoseconds, which may be large enough to include seconds
     * @return The new {@link Nanoseconds} object
     */
    public static Nanoseconds nanoseconds(long seconds, long nanoseconds)
    {
        return new Nanoseconds(seconds + nanoseconds / NANOSECONDS_PER_SECOND, nanoseconds % NANOSECONDS_PER_SECOND);
    }

    /**
     * Factory method that constructs from a number of seconds
     *
     * @param seconds The number of seconds
     * @return The new {@link Nanoseconds} object
     */
    public static Nanoseconds seconds(double seconds)
    {
        return ONE_SECOND.times(seconds);
    }

    /** The number of nanoseconds in this time measurement */
    private final long nanoseconds;

    /** The number of seconds in this time measurement */
    private final long seconds;

    /**
     * For serialization
     */
    protected Nanoseconds()
    {
        this(0, 0);
    }

    /**
     * Constructs from a number of seconds and a number of nanoseconds, allowing large, precise values
     */
    private Nanoseconds(long seconds, long nanoseconds)
    {
        this.seconds = seconds;
        this.nanoseconds = nanoseconds;
    }

    /**
     * Converts this count of nanoseconds to a {@link BigDecimal} value
     *
     * @return The number of nanoseconds
     */
    public BigDecimal asBigDecimal()
    {
        return BigDecimal.valueOf(this.seconds)
                .multiply(BigDecimal.valueOf(NANOSECONDS_PER_SECOND))
                .add(BigDecimal.valueOf(this.nanoseconds));
    }

    /**
     * This number of nanoseconds as a double-precision floating point value
     *
     * @return The number of nanoseconds
     */
    public double asDouble()
    {
        return seconds * 1E9 + nanoseconds;
    }

    public int compareTo(Nanoseconds that)
    {
        int result = Long.compare(seconds, that.seconds);
        if (result != 0)
        {
            return result;
        }
        return Long.compare(this.nanoseconds, that.nanoseconds);
    }

    /**
     * Computes this value divided by the given value
     *
     * @param divisor The divisor
     * @return The dividend
     */
    public double dividedBy(Nanoseconds divisor)
    {
        if (divisor.isZero())
        {
            throw new IllegalArgumentException("Cannot divide by zero");
        }

        return asBigDecimal().divide(divisor.asBigDecimal(), RoundingMode.HALF_DOWN).doubleValue();
    }

    /**
     * Divides this number of nanoseconds by the given divisor
     *
     * @param divisor The scaling factor
     * @return This number of nanoseconds scaled by the given factor
     */
    public Nanoseconds dividedBy(double divisor)
    {
        if (divisor == 0)
        {
            throw new IllegalArgumentException("Cannot divide by zero");
        }

        if (divisor == 1)
        {
            return this;
        }

        return nanoseconds(asBigDecimal().divide(BigDecimal.valueOf(divisor), RoundingMode.HALF_DOWN));
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Nanoseconds)
        {
            var that = (Nanoseconds) object;
            return this.seconds() == that.seconds() && this.nanoseconds() == that.nanoseconds();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(seconds(), nanoseconds());
    }

    public boolean isGreaterThan(Nanoseconds that)
    {
        return compareTo(that) > 0;
    }

    public boolean isGreaterThanOrEqualTo(Nanoseconds that)
    {
        return compareTo(that) >= 0;
    }

    public boolean isLessThan(Nanoseconds that)
    {
        return compareTo(that) < 0;
    }

    public boolean isLessThanOrEqualTo(Nanoseconds that)
    {
        return compareTo(that) <= 0;
    }

    public boolean isNegative()
    {
        return isLessThan(ZERO);
    }

    /**
     * @return True if this is zero nanoseconds
     */
    @Override
    public boolean isZero()
    {
        return equals(ZERO);
    }

    /**
     * Computes this minus that
     *
     * @param that The nanoseconds to subtract
     * @return The difference
     */
    public Nanoseconds minus(Nanoseconds that)
    {
        if (that.isZero())
        {
            return this;
        }

        // Compute the number of seconds using both fields (ensuring no overflow happens),
        var seconds = subtractExact(subtractExact(this.seconds, that.seconds), that.nanoseconds / NANOSECONDS_PER_SECOND);

        // then return the number of nanoseconds using the seconds, and any nanoseconds left over.
        return nanoseconds(seconds, addExact(this.nanoseconds, that.nanoseconds));
    }

    /**
     * Computes the remainder when dividing by the given divisor
     *
     * @param divisor The divisor
     * @return The remainder (modulus)
     */
    public Nanoseconds modulo(Nanoseconds divisor)
    {
        if (divisor.isZero())
        {
            throw new IllegalArgumentException("Cannot divide by zero");
        }

        return nanoseconds(asBigDecimal().remainder(divisor.asBigDecimal()));
    }

    /**
     * Adds the given number of nanoseconds to this value
     *
     * @param that The nanoseconds to add
     * @return The sum of this and that
     */
    public Nanoseconds plus(Nanoseconds that)
    {
        if (that.isZero())
        {
            return this;
        }

        // Compute the number of seconds using both fields (ensuring no overflow happens),
        var seconds = addExact(addExact(this.seconds, that.seconds), that.nanoseconds / NANOSECONDS_PER_SECOND);

        // then return the number of nanoseconds using the seconds, and any nanoseconds left over.
        return nanoseconds(seconds, addExact(this.nanoseconds, that.nanoseconds));
    }

    /**
     * Rounds this value down to the nearest unit value
     *
     * @param unit The quantization unit
     * @return The rounded value
     */
    public Nanoseconds roundDown(Nanoseconds unit)
    {
        return nanoseconds(asBigDecimal()
                .divide(unit.asBigDecimal(), RoundingMode.DOWN)
                .multiply(unit.asBigDecimal()));
    }

    /**
     * Rounds this value up to the nearest unit value
     *
     * @param unit The quantization unit
     * @return The rounded value
     */
    public Nanoseconds roundUp(Nanoseconds unit)
    {
        return nanoseconds(asBigDecimal()
                .divide(unit.asBigDecimal(), RoundingMode.UP)
                .multiply(unit.asBigDecimal()));
    }

    /**
     * Multiplies this number of nanoseconds by the given factor
     *
     * @param factor The scaling factor
     * @return This number of nanoseconds scaled by the given factor
     */
    public Nanoseconds times(double factor)
    {
        if (factor == 0)
        {
            return ZERO;
        }

        if (factor == 1)
        {
            return this;
        }

        return nanoseconds(asBigDecimal().multiply(BigDecimal.valueOf(factor)));
    }

    public String toString()
    {
        return String.format("%d.%09d", seconds(), nanoseconds());
    }

    /**
     * @return The number of nanoseconds, not including any seconds
     */
    private long nanoseconds()
    {
        return nanoseconds % NANOSECONDS_PER_SECOND;
    }

    /**
     * @return The number of seconds, including any seconds in the nanoseconds field
     */
    private long seconds()
    {
        return this.seconds + nanoseconds / NANOSECONDS_PER_SECOND;
    }
}
