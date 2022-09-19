package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.annotations.code.CodeQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/**
 * Performs type-casting on a long value returned by {@link #asLong()}
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE,
             testing = UNNECESSARY,
             documentation = COMPLETE)
public interface Castable extends CastTrait
{
    default <T extends Number> T as(Class<T> type)
    {
        return cast(this, type);
    }

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
}
