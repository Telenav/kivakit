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

package com.telenav.kivakit.core.kernel.language.collections.map.count;

import com.telenav.kivakit.core.kernel.interfaces.numeric.Countable;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.MutableCount;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps a count for each key. There is some effort to keep results thread-safe, but true accuracy is not guaranteed.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsMap.class)
public class CountMap<T>
{
    private long total;

    private final Map<T, MutableCount> counts;

    public CountMap()
    {
        counts = new HashMap<>();
    }

    public CountMap(final Count initialSize)
    {
        counts = new HashMap<>(initialSize.asInt(), 0.6f);
    }

    public CountMap(final CountMap<T> that)
    {
        counts = new HashMap<>();
        mergeIn(that);
        total = that.total;
    }

    public CountMap<T> add(final T key, final Countable value)
    {
        add(key, value.count().asLong());
        return this;
    }

    public Count add(final T key, final long value)
    {
        final var count = counts.computeIfAbsent(key, ignored -> new MutableCount());
        count.plus(value);
        total += value;
        return Count.count(total);
    }

    public List<Map.Entry<T, MutableCount>> ascendingEntries(final Count maximum,
                                                             final Comparator<Map.Entry<T, MutableCount>> comparator)
    {
        assert maximum != null;
        final List<Map.Entry<T, MutableCount>> sorted = new ArrayList<>(counts.entrySet());
        sorted.sort(comparator);
        return sorted.subList(0, Math.min(sorted.size(), maximum.asInt()));
    }

    public CountMap<T> bottom(final Count maximum)
    {
        return bottom(maximum, Map.Entry.comparingByValue());
    }

    public CountMap<T> bottom(final Count maximum, final Comparator<Map.Entry<T, MutableCount>> comparator)
    {
        final var bottom = new CountMap<T>();
        for (final var entry : ascendingEntries(maximum, comparator))
        {
            bottom.add(entry.getKey(), entry.getValue());
        }
        return bottom;
    }

    public boolean contains(final T key)
    {
        return counts.containsKey(key);
    }

    public Count count(final T key)
    {
        return counts.computeIfAbsent(key, ignored -> new MutableCount()).count();
    }

    public CountMap<T> decrement(final T key)
    {
        final var count = counts.computeIfAbsent(key, ignored -> new MutableCount());
        count.decrement();
        total--;
        return this;
    }

    public List<Map.Entry<T, MutableCount>> descendingEntries(final Count maximum,
                                                              final Comparator<Map.Entry<T, MutableCount>> comparator)
    {
        assert maximum != null;
        final List<Map.Entry<T, MutableCount>> sorted = new ArrayList<>(counts.entrySet());
        sorted.sort(comparator.reversed());
        return sorted.subList(0, Math.min(sorted.size(), maximum.asInt()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof CountMap)
        {
            final var that = (CountMap<T>) object;
            if (counts.size() != that.counts.size())
            {
                return false;
            }
            for (final var entry : counts.entrySet())
            {
                final var value = that.counts.get(entry.getKey());
                if (value != null && !value.equals(entry.getValue()))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return counts.hashCode();
    }

    public CountMap<T> increment(final T key)
    {
        final var count = counts.computeIfAbsent(key, ignored -> new MutableCount());
        count.increment();
        total++;
        return this;
    }

    public Collection<T> keySet()
    {
        return counts.keySet();
    }

    public void mergeIn(final CountMap<T> that)
    {
        for (final var entry : that.counts.entrySet())
        {
            add(entry.getKey(), entry.getValue());
        }
    }

    public T minimum()
    {
        var minimumCount = Long.MAX_VALUE;
        T minimum = null;
        for (final var entry : counts.entrySet())
        {
            if (entry.getValue().asLong() < minimumCount)
            {
                minimum = entry.getKey();
                minimumCount = entry.getValue().asLong();
            }
        }
        return minimum;
    }

    public CountMap<T> prune(final Count minimum)
    {
        final var counts = new CountMap<T>();
        for (final var entry : this.counts.entrySet())
        {
            if (entry.getValue().asLong() >= minimum.get())
            {
                counts.add(entry.getKey(), entry.getValue());
            }
        }
        return counts;
    }

    public void remove(final T key)
    {
        counts.remove(key);
    }

    public void removeAll(final CountMap<T> map)
    {
        for (final T value : map.keySet())
        {
            counts.remove(value);
        }
    }

    public int size()
    {
        return counts.size();
    }

    public List<T> sortedByDescendingCount()
    {
        final List<Map.Entry<T, MutableCount>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(Map.Entry.comparingByValue());
        final List<T> sorted = new ArrayList<>();
        for (final var entry : entries)
        {
            sorted.add(entry.getKey());
        }
        return sorted;
    }

    @SuppressWarnings("unchecked")
    public List<T> sortedKeys()
    {
        return sortedKeys((a, b) -> ((Comparable<T>) a).compareTo(b));
    }

    public List<T> sortedKeys(final Comparator<T> comparator)
    {
        final List<T> keys = new ArrayList<>(keySet());
        keys.sort(comparator);
        return keys;
    }

    @Override
    public String toString()
    {
        return toString(", ");
    }

    public String toString(final String separator)
    {
        final var list = new StringList();
        for (final var key : sortedKeys())
        {
            list.add(key + " = " + count(key));
        }
        return list.join(separator);
    }

    public CountMap<T> top(final Count maximum)
    {
        return top(maximum, Map.Entry.comparingByValue());
    }

    public CountMap<T> top(final Count maximum, final Comparator<Map.Entry<T, MutableCount>> comparator)
    {
        final var top = new CountMap<T>();
        for (final var entry : descendingEntries(maximum, comparator))
        {
            top.add(entry.getKey(), entry.getValue());
        }
        return top;
    }

    public long total()
    {
        return total;
    }
}
