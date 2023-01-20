package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Interface to an object that can logically be zero or non-zero.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("SpellCheckingInspection")
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface Zeroable
{
    /**
     * Returns true if this object is non-zero
     *
     * @return True if this object is not zero
     */
    default boolean isNonZero()
    {
        return !isZero();
    }

    /**
     * Returns true if this object is zero
     *
     * @return True if this object is zero
     */
    boolean isZero();
}
