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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.collection.SpaceLimited;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.MORE_TESTING_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * A base class for bounded maps which wraps a {@link Map} implementation. The following methods are added to the base
 * functionality:
 *
 * <ul>
 *     <li>{@link #get(Object, Object)} - Typesafe version of {@link #getOrDefault(Object, Object)}</li>
 *     <li>{@link #getOrCreate(Object)} - Creates missing values using {@link #onCreateValue(Object)}</li>
 *     <li>{@link #put(Object, Object, Object)} - Puts the given value. Uses the default value if the value is null</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "UnusedReturnValue", "EqualsWhichDoesntCheckParameterClass" })
@UmlClassDiagram(diagram = DiagramCollections.class)
@ApiQuality(stability = STABLE_EXPANDABLE,
            testing = MORE_TESTING_NEEDED,
            documentation = FULLY_DOCUMENTED)
public class BaseMap<Key, Value> implements
        Map<Key, Value>,
        SpaceLimited
{
    /** The map to wrap */
    private final Map<Key, Value> map;

    /** The maximum number of values that can be stored in this list */
    private final int maximumSize;

    /** True if this set ran out of room, and we've already warned about it */
    private boolean warnedAboutOutOfRoom;

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
    public BaseMap(Maximum maximumSize)
    {
        this(maximumSize, new HashMap<>());
    }

    /**
     * A bounded map with the given implementation
     */
    public BaseMap(Maximum maximumSize, Map<Key, Value> map)
    {
        this.maximumSize = ensureNotNull(maximumSize.asInt());
        this.map = map;
    }

    /**
     * An unbounded map with the given implementation
     */
    public BaseMap(Map<Key, Value> map)
    {
        this(Maximum.MAXIMUM, map);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear()
    {
        map.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Value compute(Key key,
                         @NotNull BiFunction<? super Key, ? super Value, ? extends Value> remappingFunction)
    {
        return map.compute(key, remappingFunction);
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public Value computeIfAbsent(Key key, @NotNull Function<? super Key, ? extends Value> mappingFunction)
    {
        return map.computeIfAbsent(key, mappingFunction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Value computeIfPresent(Key key,
                                  @NotNull BiFunction<? super Key, ? super Value, ? extends Value> remappingFunction)
    {
        return map.computeIfPresent(key, remappingFunction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(Object key)
    {
        return map.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(Object value)
    {
        return map.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<java.util.Map.Entry<Key, Value>> entrySet()
    {
        return map.entrySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object that)
    {
        return map.equals(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forEach(BiConsumer<? super Key, ? super Value> action)
    {
        map.forEach(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Value get(Object key)
    {
        return map.get(key);
    }

    /**
     * Typesafe version of {@link #getOrDefault(Object, Object)}
     *
     * @param key The key
     * @param defaultValue The default value
     * @return The retrieved value
     */
    public Value get(Key key, Value defaultValue)
    {
        var value = get(key);
        return value == null ? defaultValue : value;
    }

    /**
     * Gets the value associated with the given key. If no value exists, the value returned by
     * {@link #onCreateValue(Object)} is stored in the map, and returned.
     *
     * @param key The key
     * @return The value returned by {@link #onCreateValue(Object)}
     */
    public Value getOrCreate(Key key)
    {
        var value = map.get(key);
        if (value == null)
        {
            value = onCreateValue(key);
            if (value != null)
            {
                put(key, value);
            }
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Value getOrDefault(Object key, Value defaultValue)
    {
        return map.getOrDefault(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return map.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Key> keySet()
    {
        return map.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Value merge(Key key, @NotNull Value value,
                       @NotNull BiFunction<? super Value, ? super Value, ? extends Value> remappingFunction)
    {
        return map.merge(key, value, remappingFunction);
    }

    /**
     * Called when a bounded list runs out of room
     */
    @Override
    public void onOutOfRoom(int values)
    {
        if (!warnedAboutOutOfRoom)
        {
            warnedAboutOutOfRoom = true;
            Ensure.warning(new Throwable(), "Adding $ values, would exceed maximum size of $. Ignoring operation.", values, totalRoom());
        }
    }

    /**
     * Stores the given value under the given key. Checks that there is room to do so first.
     * <p>
     * {@inheritDoc}
     * </p>
     */
    @Override
    public Value put(Key key, Value value)
    {
        if (hasRoomFor(1))
        {
            return map.put(key, ensureNotNull(value));
        }
        return null;
    }

    /**
     * Stores the given value under the given key. If the value is null, uses the defaultValue instead.
     *
     * @param key The key
     * @param value The value
     * @param defaultValue The default value to use if the value is null
     * @return Any value that was replaced
     */
    public Value put(Key key, Value value, Value defaultValue)
    {
        ensureNotNull(value);
        ensureNotNull(defaultValue);
        return put(key, value == null ? defaultValue : value);
    }

    /**
     * Stores the given values from the given map in this map. Checks that there is room to do so first.
     * <p>
     * {@inheritDoc}
     * </p>
     */
    @Override
    public void putAll(@NotNull Map<? extends Key, ? extends Value> that)
    {
        if (hasRoomFor(that.size()))
        {
            this.map.putAll(that);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Value putIfAbsent(@NotNull Key key, Value value)
    {
        return map.putIfAbsent(key, value);
    }

    /**
     * Stores the given value under the given key, if the value is non-null.
     *
     * @return True if the value was stored
     */
    public boolean putIfNotNull(Key key, Value value)
    {
        if (key != null && value != null)
        {
            put(key, value);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(@NotNull Object key, Object value)
    {
        return map.remove(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Value remove(@NotNull Object key)
    {
        return map.remove(key);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Value replace(@NotNull Key key, @NotNull Value value)
    {
        return map.replace(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void replaceAll(@NotNull BiFunction<? super Key, ? super Value, ? extends Value> function)
    {
        map.replaceAll(function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        return map.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        var list = new StringList();
        for (var entry : entrySet())
        {
            list.add(entry.getKey() + " = " + entry.getValue());
        }
        return "[" + list.join() + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int totalRoom()
    {
        return maximumSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Value> values()
    {
        return map.values();
    }

    protected Map<Key, Value> map()
    {
        return map;
    }

    protected Value onCreateValue(Key key)
    {
        return null;
    }
}
