package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.value.LongValued;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Interface for common operations on integers (in the math sense, not in the sense of {@link Integer}).
 *
 * @param <Value> The integer type
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_DEFAULT_EXTENSIBLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public interface Numeric<Value extends LongValued> extends
        Arithmetic<Value>,
        Maximizable<Value>,
        Minimizable<Value>
{
}
