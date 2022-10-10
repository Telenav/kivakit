package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.interfaces.collection.NextIterator;
import com.telenav.kivakit.interfaces.function.Mapper;
import com.telenav.kivakit.interfaces.value.LongValued;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Interface for performing basic arithmetic
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface Arithmetic<Value> extends
        LongValued,
        NextIterator<Value>,
        Mapper<Long, Value>
{
    /**
     * Returns this value minus one
     */
    default Value decremented()
    {
        return minus(1);
    }

    /**
     * Returns this value divided by the given value
     */
    default Value dividedBy(long value)
    {
        return map(longValue() / value);
    }

    /**
     * Returns this value divided by the given value
     */
    default Value dividedBy(LongValued value)
    {
        return dividedBy(value.longValue());
    }

    /**
     * Returns true if this value divides evenly by the given value
     */
    default boolean dividesEvenlyBy(LongValued value)
    {
        return asLong() % value.longValue() == 0;
    }

    /**
     * Returns this value plus one
     */
    default Value incremented()
    {
        return plus(1);
    }

    /**
     * Returns this value minus the given value
     */
    default Value minus(long value)
    {
        return map(longValue() - value);
    }

    /**
     * Returns this value minus the given value
     */
    default Value minus(LongValued value)
    {
        return minus(value.longValue());
    }

    /**
     * Returns this value modulo the given value
     */
    default Value modulo(long value)
    {
        return map(longValue() % value);
    }

    /**
     * Returns this value modulo the given value
     */
    default Value modulo(LongValued value)
    {
        return modulo(value.longValue());
    }

    /**
     * Returns this value plus one
     */
    @Override
    default Value next()
    {
        return plus(1);
    }

    /**
     * Returns this value plus the given value
     */
    default Value plus(long value)
    {
        return map(longValue() + value);
    }

    /**
     * Returns this value plus the given value
     */
    default Value plus(LongValued value)
    {
        return plus(value.longValue());
    }

    /**
     * Returns this value times the given value
     */
    default Value times(long value)
    {
        return map(longValue() - value);
    }

    /**
     * Returns this value times the given value
     */
    default Value times(LongValued value)
    {
        return times(value.longValue());
    }
}
