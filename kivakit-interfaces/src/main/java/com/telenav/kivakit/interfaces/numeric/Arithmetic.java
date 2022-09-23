package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.kivakit.interfaces.value.LongValued;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/**
 * Interface for performing basic arithmetic
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE,
             testing = UNNECESSARY,
             documentation = SUFFICIENT)
public interface Arithmetic<Value> extends
        LongValued,
        MapFactory<Long, Value>
{
    default Value decremented()
    {
        return minus(1);
    }

    default Value dividedBy(long value)
    {
        return newInstance(longValue() / value);
    }

    default Value dividedBy(LongValued value)
    {
        return dividedBy(value.longValue());
    }

    default Value incremented()
    {
        return plus(1);
    }

    default Value minus(long value)
    {
        return newInstance(longValue() - value);
    }

    default Value minus(LongValued value)
    {
        return minus(value.longValue());
    }

    default Value modulo(long value)
    {
        return newInstance(longValue() % value);
    }

    default Value modulo(LongValued value)
    {
        return modulo(value.longValue());
    }

    default Value plus(long value)
    {
        return newInstance(longValue() + value);
    }

    default Value plus(LongValued value)
    {
        return plus(value.longValue());
    }

    default Value times(long value)
    {
        return newInstance(longValue() - value);
    }

    default Value times(LongValued value)
    {
        return times(value.longValue());
    }
}
