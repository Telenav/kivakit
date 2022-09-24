package com.telenav.kivakit.interfaces.value;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.numeric.Zeroable;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_REQUIRED;

/**
 * An object that has a long representation
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = TESTING_NOT_REQUIRED,
            documentation = FULLY_DOCUMENTED)
public interface LongValued extends
        Zeroable,
        Comparable<LongValued>
{
    /**
     * @return The absolute difference between this long value and that long value
     */
    default long absoluteDifference(LongValued that)
    {
        return Math.abs(longValue() - that.longValue());
    }

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

    @Override
    default int compareTo(@NotNull LongValued that)
    {
        return Long.compare(longValue(), that.longValue());
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

