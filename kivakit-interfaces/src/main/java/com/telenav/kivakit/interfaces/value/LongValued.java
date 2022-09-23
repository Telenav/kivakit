package com.telenav.kivakit.interfaces.value;

import com.telenav.kivakit.interfaces.numeric.Zeroable;

/**
 * An object that has a long representation
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@SuppressWarnings("unused")
public interface LongValued extends Zeroable
{
    default byte asByte()
    {
        return (byte) longValue();
    }

    default char asChar()
    {
        return (char) longValue();
    }

    default double asDouble()
    {
        return (double) longValue();
    }

    default float asFloat()
    {
        return (float) longValue();
    }

    default int asInt()
    {
        return (int) longValue();
    }

    default long asLong()
    {
        return longValue();
    }

    default short asShort()
    {
        return (short) longValue();
    }

    default boolean isApproximately(LongValued that, LongValued within)
    {
        return Math.abs(longValue() - that.longValue()) <= within.longValue();
    }

    default boolean isGreaterThan(LongValued that)
    {
        return longValue() > that.longValue();
    }

    default boolean isGreaterThanOrEqualTo(LongValued that)
    {
        return longValue() >= that.longValue();
    }

    default boolean isLessThan(LongValued that)
    {
        return longValue() < that.longValue();
    }

    default boolean isLessThanOrEqualTo(LongValued that)
    {
        return longValue() <= that.longValue();
    }

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

