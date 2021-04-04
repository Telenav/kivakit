////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.map;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramMap;
import com.telenav.kivakit.core.kernel.language.collections.map.BaseMap;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A map that has a fixed size and that deletes the oldest data when new one is added. It behaves like a cache
 *
 * @param <Key> The Key
 * @param <Value> The Value
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMap.class)
public class CacheMap<Key, Value> extends BaseMap<Key, Value>
{
    /**
     * Constructor
     *
     * @param cacheSize The size after which the eldest entry will be deleted to leave room for new entries.
     */
    public CacheMap(final Maximum cacheSize)
    {
        super(Maximum.MAXIMUM, new LinkedHashMap<>()
        {
            @Override
            protected boolean removeEldestEntry(final Map.Entry<Key, Value> eldest)
            {
                return size() >= cacheSize.asInt();
            }
        });
    }
}
