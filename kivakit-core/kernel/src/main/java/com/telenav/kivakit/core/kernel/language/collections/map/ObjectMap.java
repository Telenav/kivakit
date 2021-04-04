package com.telenav.kivakit.core.kernel.language.collections.map;

import com.telenav.kivakit.core.kernel.language.values.count.Maximum;

import java.util.Map;

/**
 * @author jonathanl (shibo)
 */
public class ObjectMap<Key, Value> extends BaseMap<Key, Value>
{
    public ObjectMap()
    {
    }

    public ObjectMap(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    public ObjectMap(final Maximum maximumSize,
                     final Map<Key, Value> map)
    {
        super(maximumSize, map);
    }

    public ObjectMap(final Map<Key, Value> map)
    {
        super(map);
    }
}
