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
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;
import static com.telenav.kivakit.core.value.count.Maximum.maximum;

/**
 * A map from key to an {@link ObjectList} of values. Values can be added with {@link #add(Object, Object)} and
 * {@link #addAll(Object, Collection)}. A flat list of all values in the map can be retrieved with
 * {@link #flatValues()}. The {@link ObjectList} for a key can be retrieved with {@link #get(Object)} or
 * {@link #list(Object)}. If the list for the given key does not yet exist, {@link #list(Object)} will create a new
 * one.
 *
 * <p><b>Adding Values</b></p>
 *
 * <ul>
 *     <li>{@link #add(Object, Object)}</li>
 *     <li>{@link #addAll(Collection, Object)}</li>
 *     <li>{@link #addAll(Object, Collection)}</li>
 *     <li>{@link #addIfNotNull(Object, Object)}</li>
 * </ul>
 *
 * <p><b>Retrieving Values</b></p>
 *
 * <ul>
 *     <li>{@link #flatValues()}</li>
 *     <li>{@link #list(Object)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class MultiMap<Key, Value> extends BaseMap<Key, ObjectList<Value>>
{
    public MultiMap()
    {
        this(MAXIMUM, MAXIMUM);
    }

    public MultiMap(Maximum maximumKeys, Maximum maximumValues)
    {
        super(maximumKeys);
    }

    public MultiMap(Maximum maximumKeys, Maximum maximumValues, Map<Key, ObjectList<Value>> map)
    {
        super(maximumKeys, map);
    }

    public MultiMap(Map<Key, ObjectList<Value>> map)
    {
        this(MAXIMUM, MAXIMUM, map);
    }

    /**
     * Adds the given value to the list found under the given key
     *
     * @param key The key
     * @param value The value to add
     */
    public void add(Key key, Value value)
    {
        getOrCreate(key).add(value);
    }

    /**
     * Adds the given value to each list found under the given keys
     *
     * @param keys The keys
     * @param value The value to add under each key
     */
    public void addAll(Collection<? extends Key> keys, Value value)
    {
        for (Key key : keys)
        {
            getOrCreate(key).add(value);
        }
    }

    /**
     * Adds the given values to the list found under the given key
     *
     * @param key The key
     * @param values The values to add
     */
    public void addAll(Key key, Collection<? extends Value> values)
    {
        getOrCreate(key).addAll(values);
    }

    /**
     * Adds the given value to the list found under the given key if the value is not nul
     *
     * @param key The key
     * @param value The value to add
     */
    public boolean addIfNotNull(Key key, Value value)
    {
        if (key != null && value != null)
        {
            add(key, value);
            return true;
        }
        return false;
    }

    /**
     * Returns a list with all values in this multi-map
     */
    public ObjectList<Value> flatValues()
    {
        var values = new ObjectList<Value>();
        for (List<Value> list : values())
        {
            values.addAll(list);
        }
        return values;
    }

    /**
     * Returns the list found under the given key
     *
     * @param key The key
     * @return The object list
     */
    public ObjectList<Value> list(Key key)
    {
        return computeIfAbsent(key, ignored -> new ObjectList<>());
    }

    /**
     * Returns the size of the longest list in this map
     */
    public Count maximumListSize()
    {
        var maximum = 0;
        for (List<Value> list : values())
        {
            maximum = Math.max(list.size(), maximum);
        }
        return Count.count(maximum);
    }

    /**
     * Sorts the values in each list in this map
     *
     * @param comparator The comparator to use when sorting
     */
    public void sort(Comparator<? super Value> comparator)
    {
        for (var entry : entrySet())
        {
            entry.getValue().sort(comparator);
        }
    }

    /**
     * The total number of values in this map
     */
    public int valueCount()
    {
        var count = 0;
        for (List<Value> list : values())
        {
            if (list != null)
            {
                count += list.size();
            }
        }
        return count;
    }

    @Override
    protected ObjectList<Value> onCreateValue(Key key)
    {
        return new ObjectList<>(maximum(maximumSize()));
    }
}
