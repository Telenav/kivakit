package com.telenav.kivakit.core.kernel.language.collections.map;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.interfaces.factory.Factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * @author jonathanl (shibo)
 */
public class Maps
{
    /**
     * @return A copy of the given map
     */
    public static <Key, Value> Map<Key, Value> copy(final Factory<Map<Key, Value>> factory,
                                                    final Map<Key, Value> map)
    {
        return deepCopy(factory, map, value -> value);
    }

    /**
     * @return A copy of the given map
     */
    public static <Key, Value> Map<Key, Value> deepCopy(final Map<Key, Value> map,
                                                        final Function<Value, Value> clone)
    {
        return deepCopy(HashMap::new, map, clone);
    }

    /**
     * @return A copy of the given map
     */
    public static <Key, Value> Map<Key, Value> deepCopy(final Factory<Map<Key, Value>> factory,
                                                        final Map<Key, Value> map,
                                                        final Function<Value, Value> clone)
    {
        final var copy = factory.newInstance();
        for (final var key : map.keySet())
        {
            final var value = map.get(key);
            Ensure.ensureNotNull(value);
            final var clonedValue = clone.apply(value);
            Ensure.ensureNotNull(clonedValue);
            copy.put(key, clonedValue);
        }
        return copy;
    }
}
