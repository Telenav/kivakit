package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.interfaces.collection.NextValue;
import com.telenav.kivakit.interfaces.value.LongValued;

/**
 * Interface for common operations on integers (in the math sense, not in the sense of {@link Integer}).
 *
 * @param <Value> The integer type
 * @author jonathanl (shibo)
 */
public interface Numeric<Value extends LongValued> extends
        Arithmetic<Value>,
        Maximizable<Value>,
        Minimizable<Value>,
        NextValue<Value>
{
    @Override
    default Value next()
    {
        return plus(1);
    }
}
