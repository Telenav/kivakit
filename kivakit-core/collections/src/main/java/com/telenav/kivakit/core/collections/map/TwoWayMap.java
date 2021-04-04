////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.map;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramMap;
import com.telenav.kivakit.core.kernel.language.collections.map.BaseMap;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;

/**
 * A map from key to value and from value to key.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMap.class)
public class TwoWayMap<Key, Value> extends BaseMap<Key, Value>
{
    /** The reverse map from value to key */
    private final LinkedMap<Value, Key> valueToKey;

    public TwoWayMap()
    {
        this(Maximum.MAXIMUM);
    }

    public TwoWayMap(final Maximum maximumSize)
    {
        valueToKey = new LinkedMap<>(maximumSize);
    }

    @Override
    public void clear()
    {
        super.clear();
        valueToKey.clear();
    }

    public Key key(final Value value)
    {
        return valueToKey.get(value);
    }

    @Override
    public Value put(final Key key, final Value value)
    {
        valueToKey.put(value, key);
        return super.put(key, value);
    }

    @Override
    public Value remove(final Object key)
    {
        final var value = super.remove(key);
        valueToKey.remove(value);
        return value;
    }
}
