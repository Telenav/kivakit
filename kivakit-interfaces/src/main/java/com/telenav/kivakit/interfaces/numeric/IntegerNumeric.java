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
        MapFactory<Long, Value>,
        NextValue<Value>,
        Castable,
        Quantizable
{
}
