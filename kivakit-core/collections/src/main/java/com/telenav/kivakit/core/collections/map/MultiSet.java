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

package com.telenav.kivakit.core.collections.map;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramSet;
import com.telenav.kivakit.core.kernel.language.collections.map.BaseMap;
import com.telenav.kivakit.core.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.core.kernel.language.iteration.Iterables;
import com.telenav.kivakit.core.kernel.language.iteration.Next;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A map from key to a set of values.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
public class MultiSet<Key, Value> extends BaseMap<Key, Set<Value>>
{
    private final Maximum maximumValues;

    public MultiSet(final Maximum maximumKeys, final Maximum maximumValues)
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
    protected MultiSet(final Maximum maximumKeys, final Maximum maximumValues, final Map<Key, Set<Value>> map)
    {
        super(maximumKeys, map);
        this.maximumValues = maximumValues;
    }

    public void add(final Key key, final Value value)
    {
        getOrCreate(key).add(value);
    }

    public int entryCount()
    {
        var count = 0;
        for (final var set : values())
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
            private final Iterator<Set<Value>> sets = values().iterator();

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

    public Set<Value> getOrEmptySet(final Object key)
    {
        return getOrDefault(key, Collections.emptySet());
    }

    public Count maximumSetSize()
    {
        var maximum = 0;
        for (final var set : values())
        {
            maximum = Math.max(set.size(), maximum);
        }
        return Count.count(maximum);
    }

    public void removeFromSet(final Key key, final Value value)
    {
        final var set = getOrEmptySet(key);
        set.remove(value);
    }

    public void replaceValue(final Key key, final Value value)
    {
        removeFromSet(key, value);
        add(key, value);
    }

    @Override
    protected Set<Value> onInitialize(final Key key)
    {
        return new ObjectSet<>(maximumValues);
    }
}
