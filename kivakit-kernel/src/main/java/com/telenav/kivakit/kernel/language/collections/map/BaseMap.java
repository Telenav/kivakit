////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.collections.map;

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A base class for bounded maps which wraps a {@link Map} implementation.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsMap.class)
public class BaseMap<Key, Value> implements Map<Key, Value>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /** The map to wrap */
    private final Map<Key, Value> map;

    /** True if this map is out of room */
    private boolean outOfRoom;

    /** The maximum size of this map */
    private final Maximum maximumSize;

    /**
     * Unbounded map
     */
    public BaseMap()
    {
        this(Maximum.MAXIMUM);
    }

    /**
     * Bounded map
     */
    public BaseMap(final Maximum maximumSize)
    {
        this(maximumSize, new HashMap<>());
    }

    /**
     * A bounded map with the given implementation
     */
    public BaseMap(final Maximum maximumSize, final Map<Key, Value> map)
    {
        Ensure.ensure(maximumSize != null);
        this.map = map;
        this.maximumSize = maximumSize;
        checkSize(0);
    }

    /**
     * An unbounded map with the given implementation
     */
    public BaseMap(final Map<Key, Value> map)
    {
        this(Maximum.MAXIMUM, map);
    }

    @Override
    public void clear()
    {
        map.clear();
    }

    @Override
    public boolean containsKey(final Object key)
    {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value)
    {
        return map.containsValue(value);
    }

    @Override
    public Set<java.util.Map.Entry<Key, Value>> entrySet()
    {
        return map.entrySet();
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object == this)
        {
            return true;
        }

        if (!(object instanceof BaseMap))
        {
            return false;
        }

        return map.equals(((BaseMap<?, ?>) object).map);
    }

    @Override
    public Value get(final Object key)
    {
        return map.get(key);
    }

    public Value get(final Object key, final Value defaultValue)
    {
        final var value = get(key);
        return value == null ? defaultValue : value;
    }

    public Value getOrCreate(final Key key)
    {
        var value = map.get(key);
        if (value == null)
        {
            value = onInitialize(key);
            if (value != null)
            {
                put(key, value);
            }
        }
        return value;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(map);
    }

    @Override
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    @Override
    public Set<Key> keySet()
    {
        return map.keySet();
    }

    public final Maximum maximumSize()
    {
        return maximumSize;
    }

    @Override
    public Value put(final Key key, final Value value)
    {
        if (checkSize(1))
        {
            checkNullValue(value);
            return map.put(key, value);
        }
        return null;
    }

    public Value put(final Key key, final Value value, final Value defaultValue)
    {
        checkNullValue(value);
        checkNullValue(defaultValue);
        return put(key, value == null ? defaultValue : value);
    }

    @Override
    public void putAll(final Map<? extends Key, ? extends Value> map)
    {
        for (final Map.Entry<? extends Key, ? extends Value> entry : map.entrySet())
        {
            put(entry.getKey(), entry.getValue());
        }
    }

    public void putIfNotNull(final Key key, final Value value)
    {
        if (value != null)
        {
            put(key, value);
        }
    }

    @Override
    public Value remove(final Object key)
    {
        return map.remove(key);
    }

    @Override
    public int size()
    {
        return map.size();
    }

    @Override
    public String toString()
    {
        final var list = new StringList();
        for (final var entry : entrySet())
        {
            list.add(entry.getKey() + " = " + entry.getValue());
        }
        return "[" + list.join() + "]";
    }

    @Override
    public Collection<Value> values()
    {
        return map.values();
    }

    protected boolean checkSize(final int increase)
    {
        if (size() + increase > maximumSize().asInt())
        {
            if (!outOfRoom)
            {
                LOGGER.problem(new Throwable(), "Maximum size of ${debug} exceeded", maximumSize());
                outOfRoom = true;
            }
            return false;
        }
        else
        {
            outOfRoom = false;
        }
        return true;
    }

    protected Map<Key, Value> map()
    {
        return map;
    }

    protected Value onInitialize(final Key key)
    {
        return null;
    }

    private void checkNullValue(final Value value)
    {
        if (value == null)
        {
            Ensure.fail("Cannot put null values into a map");
        }
    }
}
