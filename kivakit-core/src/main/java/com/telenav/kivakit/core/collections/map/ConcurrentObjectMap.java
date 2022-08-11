////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.map;

import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Class for concurrent maps with a bounded number of values.
 *
 * @author jonathanl
 * @author Junwei
 * @version 1.0.0 2012-12-27
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
public class ConcurrentObjectMap<Key, Value> extends BaseMap<Key, Value> implements java.util.concurrent.ConcurrentMap<Key, Value>
{
    /**
     * A bounded concurrent map
     */
    public ConcurrentObjectMap(Maximum maximumSize)
    {
        this(maximumSize, new ConcurrentHashMap<>());
    }

    /**
     * A bounded concurrent map with the given implementation
     */
    public ConcurrentObjectMap(Maximum maximumSize, java.util.concurrent.ConcurrentMap<Key, Value> map)
    {
        super(maximumSize, map);
    }

    /**
     * An unbounded concurrent map
     */
    public ConcurrentObjectMap()
    {
        this(Maximum.MAXIMUM);
    }

    /**
     * An unbounded concurrent map with the given implementation
     */
    protected ConcurrentObjectMap(java.util.concurrent.ConcurrentMap<Key, Value> map)
    {
        this(Maximum.MAXIMUM, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Value getOrCreate(Object key)
    {
        var value = get(key);
        if (value == null)
        {
            value = onInitialize((Key) key);
            if (value != null)
            {
                var oldValue = putIfAbsent((Key) key, value);
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
    public Value putIfAbsent(Key key, Value value)
    {
        if (checkSize(1))
        {
            return concurrentMap().putIfAbsent(key, value);
        }
        return null;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean remove(Object key, Object value)
    {
        return concurrentMap().remove(key, value);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean replace(Key key, Value oldValue, Value newValue)
    {
        return concurrentMap().replace(key, oldValue, newValue);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Value replace(Key key, Value value)
    {
        return concurrentMap().replace(key, value);
    }

    private java.util.concurrent.ConcurrentMap<Key, Value> concurrentMap()
    {
        return (java.util.concurrent.ConcurrentMap<Key, Value>) super.map();
    }
}
