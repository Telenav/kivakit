package com.telenav.kivakit.interfaces.value;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.interfaces.numeric.Zeroable;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static java.lang.Long.compare;
import static java.lang.Math.abs;

/**
 * An object that has a long representation
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface LongValued extends Zeroable
{
    /**
     * Returns the absolute difference between this long value and that long value
     */
    default long absoluteDifference(LongValued that)
    {
        return abs(longValue() - that.longValue());
    }

    /**
     * Returns this long value cast to a byte
     */
    default byte asByte()
    {
        return (byte) longValue();
    }

    /**
     * Returns this long value cast to a char
     */
    default char asChar()
    {
        return (char) longValue();
    }

    /**
     * Returns this long value cast to a double
     */
    default double asDouble()
    {
        return (double) longValue();
    }

    /**
     * Returns this long value cast to a float
     */
    default float asFloat()
    {
        return (float) longValue();
    }

    /**
     * Returns this long value cast to an int
     */
    default int asInt()
    {
        return (int) longValue();
    }

    /**
     * Returns this value
     */
    default long asLong()
    {
        return longValue();
    }

    /**
     * Returns this long value cast to a short
     */
    default short asShort()
    {
        return (short) longValue();
    }

    /**
     * @param that The value to compare with
     * @param within The tolerance
     * @return True if the difference between this value and that value is within the given tolerance
     */
    default boolean isApproximately(LongValued that, LongValued within)
    {
        return absoluteDifference(that) <= within.longValue();
    }

    /**
     * Returns true if this > that
     */
    default boolean isGreaterThan(LongValued that)
    {
        return longValue() > that.longValue();
    }

    /**
     * Returns true if this >= that
     */
    default boolean isGreaterThanOrEqualTo(LongValued that)
    {
        return longValue() >= that.longValue();
    }

    /**
     * Returns true if this < that
     */

    default boolean isLessThan(LongValued that)
    {
        return longValue() < that.longValue();
    }

    /**
     * Returns true if this <= that
     */
    default boolean isLessThanOrEqualTo(LongValued that)
    {
        return longValue() <= that.longValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isZero()
    {
        return longValue() == 0;
    }

    /**
     * Returns a {@link Comparable} that compares this object to another {@link LongValued} object.
     */
    default Comparable<LongValued> longComparable()
    {
        return that -> compare(longValue(), that.longValue());
    }

    /**
     * Returns the long value for this object
     */
    long longValue();
}
