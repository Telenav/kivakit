package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.value.LongValued;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Interface for common operations on integers (in the math sense, not in the sense of {@link Integer}).
 *
 * @param <Value> The integer type
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface Numeric<Value extends LongValued> extends
        Arithmetic<Value>,
        Maximizable<Value>,
        Minimizable<Value>
{
}
