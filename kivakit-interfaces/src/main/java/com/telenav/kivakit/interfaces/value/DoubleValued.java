package com.telenav.kivakit.interfaces.value;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static java.lang.Double.compare;
import static java.lang.Math.abs;

/**
 * An object that has a double representation
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface DoubleValued extends LongValued
{
    /**
     * Returns the absolute difference between this double value and that double value
     */
    default double absoluteDifference(DoubleValued that)
    {
        return abs(doubleValue() - that.doubleValue());
    }

    /**
     * Returns a {@link Comparable} that compares this object to another {@link DoubleValued} object.
     */
    default Comparable<DoubleValued> doubleComparable()
    {
        return that -> compare(doubleValue(), that.doubleValue());
    }

    /**
     * Returns the double value for this object
     *
     * @return The double value for this object
     */
    double doubleValue();

    /**
     * @param that The value to compare with
     * @param within The tolerance
     * @return True if the difference between this value and that value is within the given tolerance
     */
    default boolean isCloseTo(DoubleValued that, DoubleValued within)
    {
        return absoluteDifference(that) <= within.doubleValue();
    }

    /**
     * Returns true if this level is close to the given level, within the given tolerance
     *
     * @param that The level to compare with
     * @param tolerance The amount of maximum amount difference that is still considered "close"
     */
    default boolean isCloseTo(DoubleValued that, double tolerance)
    {
        return abs(that.doubleValue() - that.doubleValue()) < tolerance;
    }

    /**
     * Returns true if this > that
     */
    default boolean isGreaterThan(DoubleValued that)
    {
        return doubleValue() > that.doubleValue();
    }

    /**
     * Returns true if this >= that
     */
    default boolean isGreaterThanOrEqualTo(DoubleValued that)
    {
        return doubleValue() >= that.doubleValue();
    }

    /**
     * Returns true if this < that
     */

    default boolean isLessThan(DoubleValued that)
    {
        return doubleValue() < that.doubleValue();
    }

    /**
     * Returns true if this <= that
     */
    default boolean isLessThanOrEqualTo(DoubleValued that)
    {
        return doubleValue() <= that.doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isZero()
    {
        return doubleValue() == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default long longValue()
    {
        return (long) doubleValue();
    }
}
