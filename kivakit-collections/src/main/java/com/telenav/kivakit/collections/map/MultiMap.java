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

package com.telenav.kivakit.collections.map;

import com.telenav.kivakit.collections.project.lexakai.diagrams.DiagramMap;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.map.BaseMap;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * A map from key to an {@link ObjectList} of values. Values can be added with {@link #add(Object, Object)} and {@link
 * #addAll(Object, Collection)}. A flat list of all values in the map can be retrieved with {@link #flatValues()}. The
 * {@link ObjectList} for a key can be retrieved with {@link #get(Object)} or {@link #list(Object)}. If the list for the
 * given key does not yet exist, {@link #list(Object)} will create a new one.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMap.class)
@LexakaiJavadoc(complete = true)
public class MultiMap<Key, Value> extends BaseMap<Key, ObjectList<Value>>
{
    private final Maximum maximumValues;

    public MultiMap()
    {
        this(Maximum.MAXIMUM, Maximum.MAXIMUM);
    }

    public MultiMap(final Maximum maximumKeys, final Maximum maximumValues)
    {
        super(maximumKeys);
        this.maximumValues = maximumValues;
    }

    /**
     * In case another implementation wants to control the type of map used underneath
     */
    protected MultiMap(final Maximum maximumKeys, final Maximum maximumValues, final Map<Key, ObjectList<Value>> map)
    {
        super(maximumKeys, map);
        this.maximumValues = maximumValues;
    }

    public void add(final Key key, final Value value)
    {
        getOrCreate(key).add(value);
    }

    public void addAll(final Collection<? extends Key> keys, final Value value)
    {
        for (final Key key : keys)
        {
            getOrCreate(key).add(value);
        }
    }

    public void addAll(final Key key, final Collection<? extends Value> value)
    {
        getOrCreate(key).addAll(value);
    }

    public ObjectList<Value> flatValues()
    {
        final var values = new ObjectList<Value>();
        for (final List<Value> list : values())
        {
            values.addAll(list);
        }
        return values;
    }

    public ObjectList<Value> list(final Key key)
    {
        return computeIfAbsent(key, ignored -> new ObjectList<>());
    }

    public Count maximumListSize()
    {
        var maximum = 0;
        for (final List<Value> list : values())
        {
            maximum = Math.max(list.size(), maximum);
        }
        return Count.count(maximum);
    }

    public void sort(final Comparator<? super Value> comparator)
    {
        for (final var entry : entrySet())
        {
            entry.getValue().sort(comparator);
        }
    }

    public int totalValues()
    {
        var count = 0;
        for (final List<Value> list : values())
        {
            if (list != null)
            {
                count += list.size();
            }
        }
        return count;
    }

    @Override
    protected ObjectList<Value> onInitialize(final Key key)
    {
        return new ObjectList<>(maximumValues);
    }
}
