package com.telenav.kivakit.interfaces.value;

import com.telenav.kivakit.annotations.code.CodeQuality;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * An object that has a double representation
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@SuppressWarnings("unused")
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface DoubleValued extends LongValued
{
    /**
     * @return The absolute difference between this double value and that double value
     */
    default double absoluteDifference(DoubleValued that)
    {
        return Math.abs(doubleValue() - that.doubleValue());
    }

    /**
     * @return A {@link Comparable} that compares this object to another {@link DoubleValued} object.
     */
    default Comparable<DoubleValued> doubleComparable()
    {
        return that -> Double.compare(doubleValue(), that.doubleValue());
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
        return Math.abs(that.doubleValue() - that.doubleValue()) < tolerance;
    }

    /**
     * @return True if this > that
     */
    default boolean isGreaterThan(DoubleValued that)
    {
        return doubleValue() > that.doubleValue();
    }

    /**
     * @return True if this >= that
     */
    default boolean isGreaterThanOrEqualTo(DoubleValued that)
    {
        return doubleValue() >= that.doubleValue();
    }

    /**
     * @return True if this < that
     */

    default boolean isLessThan(DoubleValued that)
    {
        return doubleValue() < that.doubleValue();
    }

    /**
     * @return True if this <= that
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
