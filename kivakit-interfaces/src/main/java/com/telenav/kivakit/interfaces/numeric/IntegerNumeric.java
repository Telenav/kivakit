package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.interfaces.collection.NextValue;
import com.telenav.kivakit.interfaces.factory.MapFactory;

/**
 * Interface for common operations on integers (in the math sense, not in the sense of {@link Integer}).
 *
 * @param <Value> The integer type
 * @author jonathanl (shibo)
 */
public interface IntegerNumeric<Value> extends
        Arithmetic<Value>,
        Comparable<Value>,
        Maximizable<Value>,
        Minimizable<Value>,
        NextValue<Value>,
        Castable,
        MapFactory<Long, Value>,
        Quantizable
{
    default Value decremented()
    {
        return minus(newInstance(1L));
    }

    default Value incremented()
    {
        return plus(newInstance(1L));
    }
}
