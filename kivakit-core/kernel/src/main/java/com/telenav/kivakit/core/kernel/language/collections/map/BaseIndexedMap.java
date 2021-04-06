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

package com.telenav.kivakit.core.kernel.language.collections.map;

import com.telenav.kivakit.core.kernel.interfaces.collection.Indexable;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A map that allows access to data by both index and key. Note that {@link LinkedHashMap} preserves the order of values
 * in the map, but it does not allow indexing of the map.
 *
 * @author jonathanl (shibo)
 * @see Indexable
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsMap.class)
@UmlExcludeSuperTypes(Iterable.class)
public class BaseIndexedMap<Key, Value> extends BaseMap<Key, Value> implements Iterable<Value>, Indexable<Value>
{
    private final ObjectList<Value> list;

    public BaseIndexedMap(final Maximum maximumSize)
    {
        super(maximumSize);
        list = new ObjectList<>(maximumSize);
    }

    @Override
    public @NotNull Iterator<Value> asIterator(final Matcher<Value> matcher)
    {
        return list.asIterator();
    }

    @Override
    public void clear()
    {
        list.clear();
        super.clear();
    }

    @Override
    public Value get(final int index)
    {
        return list.get(index);
    }

    public int indexOf(final Value value)
    {
        return list.indexOf(value);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<Value> iterator()
    {
        return list.iterator();
    }

    @Override
    public String join(final String separator)
    {
        return list.join(separator);
    }

    @Override
    public Value put(final Key key, final Value value)
    {
        list.add(value);
        return super.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends Key, ? extends Value> map)
    {
        for (final java.util.Map.Entry<? extends Key, ? extends Value> entry : map.entrySet())
        {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Value remove(final Object key)
    {
        list.remove(get(key));
        return super.remove(key);
    }

    public void sort(final Comparator<Value> comparator)
    {
        list.sort(comparator);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Collection<Value> values()
    {
        return list;
    }
}
