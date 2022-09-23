package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.value.LongValued;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_REQUIRED;

/**
 * Interface for common operations on integers (in the math sense, not in the sense of {@link Integer}).
 *
 * @param <Value> The integer type
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = TESTING_NOT_REQUIRED,
            documentation = DOCUMENTED)
public interface Numeric<Value extends LongValued> extends
        Arithmetic<Value>,
        Maximizable<Value>,
        Minimizable<Value>
{
}
