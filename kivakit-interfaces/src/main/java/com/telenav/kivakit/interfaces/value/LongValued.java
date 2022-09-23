package com.telenav.kivakit.interfaces.value;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.numeric.Zeroable;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/**
 * An object that has a long representation
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = UNNECESSARY,
            documentation = SUFFICIENT)
public interface LongValued extends Zeroable
{
    /**
     * @return This long value cast to a byte
     */
    default byte asByte()
    {
        return (byte) longValue();
    }

    /**
     * @return This long value cast to a char
     */
    default char asChar()
    {
        return (char) longValue();
    }

    /**
     * @return This long value cast to a double
     */
    default double asDouble()
    {
        return (double) longValue();
    }

    /**
     * @return This long value cast to a float
     */
    default float asFloat()
    {
        return (float) longValue();
    }

    /**
     * @return This long value cast to an int
     */
    default int asInt()
    {
        return (int) longValue();
    }

    /**
     * @return This value
     */
    default long asLong()
    {
        return longValue();
    }

    /**
     * @return This long value cast to a short
     */
    default short asShort()
    {
        return (short) longValue();
    }

    /**
     * @return The absolute difference between this long value and that long value
     */
    default long difference(LongValued that)
    {
        return Math.abs(longValue() - that.longValue());
    }

    /**
     * @param that The value to compare with
     * @param within The tolerance
     * @return True if the difference between this value and that value is within the given tolerance
     */
    default boolean isApproximately(LongValued that, LongValued within)
    {
        return difference(that) <= within.longValue();
    }

    /**
     * @return True if this > that
     */
    default boolean isGreaterThan(LongValued that)
    {
        return longValue() > that.longValue();
    }

    /**
     * @return True if this >= that
     */
    default boolean isGreaterThanOrEqualTo(LongValued that)
    {
        return longValue() >= that.longValue();
    }

    /**
     * @return True if this < that
     */

    default boolean isLessThan(LongValued that)
    {
        return longValue() < that.longValue();
    }

    /**
     * @return True if this <= that
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
     * @return The long value for this object
     */
    long longValue();
}

