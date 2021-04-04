////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.collections.map;

import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Base class for concurrent maps with a bounded number of values.
 *
 * @author jonathanl
 * @author Junwei
 * @version 1.0.0 2012-12-27
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsMap.class)
public class BaseConcurrentMap<Key, Value> extends BaseMap<Key, Value> implements ConcurrentMap<Key, Value>
{
    /**
     * A bounded concurrent map
     */
    public BaseConcurrentMap(final Maximum maximumSize)
    {
        this(maximumSize, new ConcurrentHashMap<>());
    }

    /**
     * A bounded concurrent map with the given implementation
     */
    public BaseConcurrentMap(final Maximum maximumSize, final ConcurrentMap<Key, Value> map)
    {
        super(maximumSize, map);
    }

    /**
     * An unbounded concurrent map
     */
    protected BaseConcurrentMap()
    {
        this(Maximum.MAXIMUM);
    }

    /**
     * An unbounded concurrent map with the given implementation
     */
    protected BaseConcurrentMap(final ConcurrentMap<Key, Value> map)
    {
        this(Maximum.MAXIMUM, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Value getOrCreate(final Object key)
    {
        var value = get(key);
        if (value == null)
        {
            value = onInitialize((Key) key);
            if (value != null)
            {
                final var oldValue = putIfAbsent((Key) key, value);
                if (oldValue != null)
                {
                    value = oldValue;
                }
            }
        }
        return value;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Value putIfAbsent(final Key key, final Value value)
    {
        if (checkSize(1))
        {
            return concurrentMap().putIfAbsent(key, value);
        }
        return null;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean remove(final Object key, final Object value)
    {
        return concurrentMap().remove(key, value);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean replace(final Key key, final Value oldValue, final Value newValue)
    {
        return concurrentMap().replace(key, oldValue, newValue);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Value replace(final Key key, final Value value)
    {
        return concurrentMap().replace(key, value);
    }

    private ConcurrentMap<Key, Value> concurrentMap()
    {
        return (ConcurrentMap<Key, Value>) super.map();
    }
}
