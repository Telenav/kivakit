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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.collections.internal.lexakai.DiagramMap;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.BaseMap;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.collection.Addable;
import com.telenav.kivakit.interfaces.collection.Indexable;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A map that allows access to data by both index and key. Note that {@link LinkedHashMap} preserves the order of values
 * in the map, but it does not allow indexing of the map.
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #compute(Object, BiFunction)}</li>
 *     <li>{@link #computeIfAbsent(Object, Function)}</li>
 *     <li>{@link #computeIfPresent(Object, BiFunction)}</li>
 *     <li>{@link #get(Object)}</li>
 *     <li>{@link #get(Object, Object)}</li>
 *     <li>{@link #get(int)}</li>
 *     <li>{@link #indexOf(Object)}</li>
 *     <li>{@link #iterator()}</li>
 *     <li>{@link #keySet()}</li>
 *     <li>{@link #put(Object, Object)}</li>
 *     <li>{@link #putAll(Map)}</li>
 *     <li>{@link #putIfAbsent(Object, Object)}</li>
 *     <li>{@link #putIfNotNull(Object, Object)}</li>
 *     <li>{@link #values()}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #clear()}</li>
 *     <li>{@link #join()}</li>
 *     <li>{@link #join(String)}</li>
 *     <li>{@link #join(char)}</li>
 *     <li>{@link #remove(Object)}</li>
 *     <li>{@link #sort(Comparator)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Indexable
 */
@UmlClassDiagram(diagram = DiagramMap.class)
@UmlExcludeSuperTypes(Iterable.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public abstract class BaseIndexedMap<Key, Value> extends BaseMap<Key, Value> implements
        Iterable<Value>,
        Indexable<Value>,
        Addable<Value>
{
    /** The list of values in insertion order */
    private final ObjectList<Value> list;

    protected BaseIndexedMap(Maximum maximumSize)
    {
        super(maximumSize);
        list = new ObjectList<>(maximumSize);
    }

    @Override
    public @NotNull
    Iterator<Value> asIterator(Matcher<Value> matcher)
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
    public Value get(int index)
    {
        return list.get(index);
    }

    public int indexOf(Value value)
    {
        return list.indexOf(value);
    }

    @Override
    public Iterator<Value> iterator()
    {
        return list.iterator();
    }

    @Override
    public String join(String separator)
    {
        return list.join(separator);
    }

    @Override
    public Value put(Key key, Value value)
    {
        list.add(value);
        return super.put(key, value);
    }

    @Override
    public void putAll(@NotNull Map<? extends Key, ? extends Value> that)
    {
        for (Map.Entry<? extends Key, ? extends Value> entry : that.entrySet())
        {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Value remove(@NotNull Object key)
    {
        list.remove(get(key));
        return super.remove(key);
    }

    public void sort(Comparator<Value> comparator)
    {
        list.sort(comparator);
    }

    @Override
    public Collection<Value> values()
    {
        return list;
    }
}
