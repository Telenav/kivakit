package com.telenav.kivakit.interfaces.numeric;

/**
 * Performs type-casting on a long value returned by {@link #asLong()}
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("DuplicatedCode")
public interface Castable extends CastTrait
{
    default byte asByte()
    {
        return (byte) asLong();
    }

    default char asChar()
    {
        return (char) asLong();
    }

    default double asDouble()
    {
        return (double) asLong();
    }

    default float asFloat()
    {
        return (float) asLong();
    }

    default int asInt()
    {
        return (int) asLong();
    }

    default long asLong()
    {
        throw new UnsupportedOperationException();
    }

    default short asShort()
    {
        return (short) asLong();
    }

    default <T extends Number> T as(Class<T> type)
    {
        return cast(this, type);
    }
}
