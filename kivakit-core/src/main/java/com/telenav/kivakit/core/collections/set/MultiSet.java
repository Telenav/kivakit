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

package com.telenav.kivakit.core.collections.set;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.iteration.Iterables;
import com.telenav.kivakit.core.collections.map.BaseMap;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.collection.NextIterator;

import java.util.Iterator;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;

/**
 * A map from key to an {@link ObjectSet} of values. Values can be added with {@link #add(Object, Object)}. A flattened
 * set of all values in the map can be retrieved with {@link #flatValues()}. The {@link ObjectSet} for a key can be
 * retrieved with {@link #set(Object)}. If the set for the given key does not yet exist, one is created.
 *
 * <p><b>Adding Values</b></p>
 *
 * <ul>
 *     <li>{@link #add(Object, Object)}</li>
 * </ul>
 *
 * <p><b>Retrieving Values</b></p>
 *
 * <ul>
 *     <li>{@link #flatValues()}</li>
 *     <li>{@link #set(Object)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class MultiSet<Key, Value> extends BaseMap<Key, ObjectSet<Value>>
{
    /** The maximum number of values in the set for each key */
    private final Maximum maximumValues;

    /**
     * Creates a "multi-set" where each key has a set of values rather than a single value
     *
     * @param maximumKeys The maximum number of keys
     * @param maximumValues The maximum number of values in the set for each key
     */
    public MultiSet(Maximum maximumKeys, Maximum maximumValues)
    {
        super(maximumKeys);
        this.maximumValues = maximumValues;
    }

    /**
     * Creates an unbounded "multi-set" where each key has a set of values rather than a single value
     */
    public MultiSet()
    {
        this(MAXIMUM, MAXIMUM);
    }

    /**
     * Adds the given value to the set under the given key
     *
     * @param key The key
     * @param value The value to add
     */
    public void add(Key key, Value value)
    {
        getOrCreate(key).add(value);
    }

    /**
     * Returns a list with all values in this multimap
     */
    public Iterable<Value> flatValues()
    {
        return Iterables.iterable(() -> new NextIterator<>()
        {
            private final Iterator<ObjectSet<Value>> sets = values().iterator();

            private Iterator<Value> values;

            @Override
            public Value next()
            {
                while (values == null || !values.hasNext())
                {
                    if (sets.hasNext())
                    {
                        values = sets.next().iterator();
                    }
                    else
                    {
                        return null;
                    }
                }
                return values.next();
            }
        });
    }

    /**
     * Gets the set of values for the given key. If there is no set, returns the empty set.
     *
     * @param key The key to access
     * @return The set
     */
    public Set<Value> getOrEmptySet(Object key)
    {
        return getOrDefault(key, new ObjectSet<>());
    }

    /**
     * Returns the size of the largest set in this map
     */
    public Count maximumSetSize()
    {
        var maximum = 0;
        for (var set : values())
        {
            maximum = Math.max(set.size(), maximum);
        }
        return Count.count(maximum);
    }

    /**
     * Removes the given value from the set found under the given key
     *
     * @param key The key
     * @param value The value to remove
     */
    public void removeFromSet(Key key, Value value)
    {
        var set = getOrEmptySet(key);
        set.remove(value);
    }

    /**
     * Replaces the given value from the set found under the given key
     *
     * @param key The key
     * @param value The value to remove
     */
    public void replaceValue(Key key, Value value)
    {
        removeFromSet(key, value);
        add(key, value);
    }

    /**
     * Returns the set found under the given key
     *
     * @param key The key
     * @return The set
     */
    public ObjectSet<Value> set(Key key)
    {
        return computeIfAbsent(key, ignored -> new ObjectSet<>());
    }

    /**
     * The total number of values in this map
     */
    public int valueCount()
    {
        var count = 0;
        for (var set : values())
        {
            if (set != null)
            {
                count += set.size();
            }
        }
        return count;
    }

    @Override
    protected ObjectSet<Value> onCreateValue(Key key)
    {
        return new ObjectSet<>(maximumValues);
    }
}
