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

package com.telenav.kivakit.kernel.language.collections.map.count;

import com.telenav.kivakit.kernel.interfaces.numeric.Countable;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.count.MutableCount;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;

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

    public CountMap(Count initialSize)
    {
        counts = new HashMap<>(initialSize.asInt(), 0.6f);
    }

    public CountMap(CountMap<T> that)
    {
        counts = new HashMap<>();
        mergeIn(that);
        total = that.total;
    }

    public CountMap<T> add(T key, Countable value)
    {
        add(key, value.count().asLong());
        return this;
    }

    public Count add(T key, long value)
    {
        var count = counts.computeIfAbsent(key, ignored -> new MutableCount());
        count.plus(value);
        total += value;
        return Count.count(total);
    }

    public List<Map.Entry<T, MutableCount>> ascendingEntries(Count maximum,
                                                             Comparator<Map.Entry<T, MutableCount>> comparator)
    {
        assert maximum != null;
        List<Map.Entry<T, MutableCount>> sorted = new ArrayList<>(counts.entrySet());
        sorted.sort(comparator);
        return sorted.subList(0, Math.min(sorted.size(), maximum.asInt()));
    }

    public CountMap<T> bottom(Count maximum)
    {
        return bottom(maximum, Map.Entry.comparingByValue());
    }

    public CountMap<T> bottom(Count maximum, Comparator<Map.Entry<T, MutableCount>> comparator)
    {
        var bottom = new CountMap<T>();
        for (var entry : ascendingEntries(maximum, comparator))
        {
            bottom.add(entry.getKey(), entry.getValue());
        }
        return bottom;
    }

    public boolean contains(T key)
    {
        return counts.containsKey(key);
    }

    public Count count(T key)
    {
        return counts.computeIfAbsent(key, ignored -> new MutableCount()).count();
    }

    public CountMap<T> decrement(T key)
    {
        var count = counts.computeIfAbsent(key, ignored -> new MutableCount());
        count.decrement();
        total--;
        return this;
    }

    public List<Map.Entry<T, MutableCount>> descendingEntries(Count maximum,
                                                              Comparator<Map.Entry<T, MutableCount>> comparator)
    {
        assert maximum != null;
        List<Map.Entry<T, MutableCount>> sorted = new ArrayList<>(counts.entrySet());
        sorted.sort(comparator.reversed());
        return sorted.subList(0, Math.min(sorted.size(), maximum.asInt()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof CountMap)
        {
            var that = (CountMap<T>) object;
            if (counts.size() != that.counts.size())
            {
                return false;
            }
            for (var entry : counts.entrySet())
            {
                var value = that.counts.get(entry.getKey());
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

    public CountMap<T> increment(T key)
    {
        var count = counts.computeIfAbsent(key, ignored -> new MutableCount());
        count.increment();
        total++;
        return this;
    }

    public Collection<T> keySet()
    {
        return counts.keySet();
    }

    public void mergeIn(CountMap<T> that)
    {
        for (var entry : that.counts.entrySet())
        {
            add(entry.getKey(), entry.getValue());
        }
    }

    public T minimum()
    {
        var minimumCount = Long.MAX_VALUE;
        T minimum = null;
        for (var entry : counts.entrySet())
        {
            if (entry.getValue().asLong() < minimumCount)
            {
                minimum = entry.getKey();
                minimumCount = entry.getValue().asLong();
            }
        }
        return minimum;
    }

    public CountMap<T> prune(Count minimum)
    {
        var counts = new CountMap<T>();
        for (var entry : this.counts.entrySet())
        {
            if (entry.getValue().asLong() >= minimum.get())
            {
                counts.add(entry.getKey(), entry.getValue());
            }
        }
        return counts;
    }

    public void remove(T key)
    {
        counts.remove(key);
    }

    public void removeAll(CountMap<T> map)
    {
        for (T value : map.keySet())
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
        List<Map.Entry<T, MutableCount>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(Map.Entry.comparingByValue());
        List<T> sorted = new ArrayList<>();
        for (var entry : entries)
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

    public List<T> sortedKeys(Comparator<T> comparator)
    {
        List<T> keys = new ArrayList<>(keySet());
        keys.sort(comparator);
        return keys;
    }

    @Override
    public String toString()
    {
        return toString(", ");
    }

    public String toString(String separator)
    {
        var list = new StringList();
        for (var key : sortedKeys())
        {
            list.add(key + " = " + count(key));
        }
        return list.join(separator);
    }

    public CountMap<T> top(Count maximum)
    {
        return top(maximum, Map.Entry.comparingByValue());
    }

    public CountMap<T> top(Count maximum, Comparator<Map.Entry<T, MutableCount>> comparator)
    {
        var top = new CountMap<T>();
        for (var entry : descendingEntries(maximum, comparator))
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
