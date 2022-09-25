package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.collection.NextIterator;
import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.kivakit.interfaces.value.LongValued;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_REQUIRED;

/**
 * Interface for performing basic arithmetic
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = TESTING_NOT_REQUIRED,
            documentation = FULLY_DOCUMENTED)
public interface Arithmetic<Value> extends
        LongValued,
        NextIterator<Value>,
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

    default Value next()
    {
        return plus(1);
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
