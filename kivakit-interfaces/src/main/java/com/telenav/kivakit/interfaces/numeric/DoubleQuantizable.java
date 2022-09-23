package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.annotations.code.CodeQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/***
 * A quantizable object that can be turned into a <i>double</i> value.
 *
 * @author jonathanl (shibo)
 * @see Quantizable
 */
@CodeQuality(stability = STABLE,
             testing = UNNECESSARY,
             documentation = COMPLETE)
public interface DoubleQuantizable extends Zeroable
{
    /**
     * Returns the double quantum value
     */
    double quantumDouble();

    @Override
    default boolean isZero()
    {
        return quantumDouble() == 0.0;
    }
}
