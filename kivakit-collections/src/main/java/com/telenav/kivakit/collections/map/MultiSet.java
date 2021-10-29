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

package com.telenav.kivakit.collections.map;

import com.telenav.kivakit.collections.project.lexakai.diagrams.DiagramSet;
import com.telenav.kivakit.kernel.language.collections.map.BaseMap;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.iteration.Iterables;
import com.telenav.kivakit.kernel.language.iteration.Next;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A map from key to an {@link ObjectSet} of values. Values can be added with {@link #add(Object, Object)}. A flattened
 * set of all values in the map can be retrieved with {@link #flatValues()}. The {@link ObjectSet} for a key can be
 * retrieved with {@link #set(Object)}. If the set for the given key does not yet exist, one is created.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
@LexakaiJavadoc(complete = true)
public class MultiSet<Key, Value> extends BaseMap<Key, ObjectSet<Value>>
{
    private final Maximum maximumValues;

    public MultiSet(Maximum maximumKeys, Maximum maximumValues)
    {
        super(maximumKeys);
        this.maximumValues = maximumValues;
    }

    public MultiSet()
    {
        this(Maximum.MAXIMUM, Maximum.MAXIMUM);
    }

    /**
     * In case another implementation wants to control the type of map used underneath
     */
    protected MultiSet(Maximum maximumKeys, Maximum maximumValues, Map<Key, ObjectSet<Value>> map)
    {
        super(maximumKeys, map);
        this.maximumValues = maximumValues;
    }

    public void add(Key key, Value value)
    {
        getOrCreate(key).add(value);
    }

    public int entryCount()
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

    public Iterable<Value> flatValues()
    {
        return Iterables.iterable(() -> new Next<>()
        {
            private final Iterator<ObjectSet<Value>> sets = values().iterator();

            private Iterator<Value> values;

            @Override
            public Value onNext()
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

    public Set<Value> getOrEmptySet(Object key)
    {
        return getOrDefault(key, new ObjectSet<>());
    }

    public Count maximumSetSize()
    {
        var maximum = 0;
        for (var set : values())
        {
            maximum = Math.max(set.size(), maximum);
        }
        return Count.count(maximum);
    }

    public void removeFromSet(Key key, Value value)
    {
        var set = getOrEmptySet(key);
        set.remove(value);
    }

    public void replaceValue(Key key, Value value)
    {
        removeFromSet(key, value);
        add(key, value);
    }

    public ObjectSet<Value> set(Key key)
    {
        return computeIfAbsent(key, ignored -> new ObjectSet<>());
    }

    @Override
    protected ObjectSet<Value> onInitialize(Key key)
    {
        return new ObjectSet<>(maximumValues);
    }
}
