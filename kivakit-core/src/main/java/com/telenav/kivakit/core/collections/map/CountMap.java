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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.value.count.ConcurrentMutableCount;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Countable;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;
import static java.lang.Math.min;

/**
 * Keeps a {@link ConcurrentMutableCount} for each key.
 *
 * <p><b>Adding</b></p>
 *
 * <ul>
 *     <li>{@link #decrement(Object)}</li>
 *     <li>{@link #increment(Object)}</li>
 *     <li>{@link #mergeIn(CountMap)}</li>
 *     <li>{@link #plus(Object, Countable)}</li>
 *     <li>{@link #plus(Object, long)}</li>
 * </ul>
 *
 * <p><b>Values</b></p>
 *
 * <ul>
 *     <li>{@link #count(Object)}</li>
 *     <li>{@link #minimum()}</li>
 *     <li>{@link #maximum()}</li>
 *     <li>{@link #total()}</li>
 * </ul>
 *
 * <p><b>Limiting</b></p>
 *
 * <ul>
 *     <li>{@link #bottom(Maximum)}</li>
 *     <li>{@link #bottom(Maximum, Comparator)}</li>
 *     <li>{@link #top(Maximum)}</li>
 *     <li>{@link #top(Maximum, Comparator)}</li>
 *     <li>{@link #pruneCountsLessThan(Count)}</li>
 * </ul>
 *
 * <p><b>Sorting</b></p>
 *
 * <ul>
 *     <li>{@link #ascendingEntries(Maximum, Comparator)}</li>
 *     <li>{@link #descendingEntries(Maximum, Comparator)}</li>
 *     <li>{@link #sortedByDescendingCount()}</li>
 *     <li>{@link #sortedKeys()}</li>
 *     <li>{@link #sortedKeys(Comparator)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "UnusedReturnValue", "unused" })
@UmlClassDiagram(diagram = DiagramCollections.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTATION_COMPLETE)
public class CountMap<Key> extends ObjectMap<Key, ConcurrentMutableCount>
{
    private AtomicLong total = new AtomicLong();

    public CountMap()
    {
        this(MAXIMUM);
    }

    public CountMap(Maximum maximum)
    {
        super(maximum);
    }

    public CountMap(CountMap<Key> that)
    {
        super(MAXIMUM);
        mergeIn(that);
        total = that.total;
    }

    /**
     * Returns entries sorted in ascending order with the given comparator
     */
    public ObjectList<Map.Entry<Key, ConcurrentMutableCount>> ascendingEntries(
            Maximum maximum,
            Comparator<? super Map.Entry<Key, ConcurrentMutableCount>> comparator)
    {
        assert maximum != null;
        var sorted = new ObjectList<>(entrySet());
        sorted.sort(comparator);
        return sorted.subList(0, min(sorted.size(), maximum.asInt()));
    }

    /**
     * Returns the bottom entries, up to the maximum
     */
    public CountMap<Key> bottom(Maximum maximum)
    {
        return bottom(maximum, Map.Entry.comparingByValue());
    }

    /**
     * Returns the bottom entries, up to the maximum, in sorted order
     */
    public CountMap<Key> bottom(Maximum maximum, Comparator<? super Map.Entry<Key, ConcurrentMutableCount>> comparator)
    {
        var bottom = new CountMap<Key>();
        for (var entry : ascendingEntries(maximum, comparator))
        {
            bottom.plus(entry.getKey(), entry.getValue());
        }
        return bottom;
    }

    /**
     * Returns the count for the given key
     */
    public Count count(Key key)
    {
        return computeIfAbsent(key, ignored -> new ConcurrentMutableCount()).count();
    }

    /**
     * Decrements the count for the given key
     *
     * @return This object, for chaining
     */
    public CountMap<Key> decrement(Key key)
    {
        var count = computeIfAbsent(key, ignored -> new ConcurrentMutableCount());
        count.decrement();
        total.decrementAndGet();
        return this;
    }

    /**
     * Returns entries sorted in descending order with the given comparator
     */
    public ObjectList<Map.Entry<Key, ConcurrentMutableCount>> descendingEntries(
            Maximum maximum,
            Comparator<? super Map.Entry<Key, ConcurrentMutableCount>> comparator)
    {
        assert maximum != null;
        ObjectList<Map.Entry<Key, ConcurrentMutableCount>> sorted = new ObjectList<>(entrySet());
        sorted.sort(comparator.reversed());
        return sorted.subList(0, min(sorted.size(), maximum.asInt()));
    }

    /**
     * Increments the count for the given key
     */
    public CountMap<Key> increment(Key key)
    {
        var count = computeIfAbsent(key, ignored -> new ConcurrentMutableCount());
        count.increment();
        total.incrementAndGet();
        return this;
    }

    /**
     * Joins the key/value pairs in this map using the given separator
     */
    public String join(String separator)
    {
        var list = new StringList();
        for (var key : sortedKeys())
        {
            list.add(key + " = " + count(key));
        }
        return list.join(separator);
    }

    /**
     * Returns the key with the maximum count
     */
    public Key maximum()
    {
        var maximum = Long.MIN_VALUE;
        Key minimumKey = null;
        for (var entry : entrySet())
        {
            if (entry.getValue().asLong() > maximum)
            {
                minimumKey = entry.getKey();
                maximum = entry.getValue().asLong();
            }
        }
        return minimumKey;
    }

    /**
     * Adds all counts from the given map to this mqp
     */
    public void mergeIn(CountMap<Key> that)
    {
        for (var entry : that.entrySet())
        {
            plus(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Returns the key with the minimum count
     */
    public Key minimum()
    {
        var minimum = Long.MAX_VALUE;
        Key minimumKey = null;
        for (var entry : entrySet())
        {
            if (entry.getValue().asLong() < minimum)
            {
                minimumKey = entry.getKey();
                minimum = entry.getValue().asLong();
            }
        }
        return minimumKey;
    }

    /**
     * Adds the given count to the given key
     */
    public CountMap<Key> plus(Key key, Countable value)
    {
        plus(key, value.count().asLong());
        return this;
    }

    /**
     * Adds the given count to the given key
     */
    public Count plus(Key key, long value)
    {
        var count = computeIfAbsent(key, ignored -> new ConcurrentMutableCount());
        count.plus(value);
        var total = this.total.addAndGet(value);
        return Count.count(total);
    }

    /**
     * Prunes all counts less than the given minimum
     */
    public CountMap<Key> pruneCountsLessThan(Count minimum)
    {
        var counts = new CountMap<Key>();
        for (var entry : entrySet())
        {
            if (entry.getValue().asLong() >= minimum.get())
            {
                counts.plus(entry.getKey(), entry.getValue());
            }
        }
        return counts;
    }

    /**
     * Removes all counts in the given map
     */
    public void removeAll(CountMap<Key> map)
    {
        for (Key value : map.keySet())
        {
            remove(value);
        }
    }

    /**
     * Returns a list of keys sorted by descending count
     */
    public ObjectList<Key> sortedByAscendingCount()
    {
        return sortedByDescendingCount().reversed();
    }

    /**
     * Returns a list of keys sorted by descending count
     */
    public ObjectList<Key> sortedByDescendingCount()
    {
        List<Map.Entry<Key, ConcurrentMutableCount>> entries = new ArrayList<>(entrySet());
        entries.sort(Map.Entry.comparingByValue());
        var sorted = new ObjectList<Key>();
        for (var entry : entries)
        {
            sorted.add(entry.getKey());
        }
        return sorted;
    }

    /**
     * Returns a list of keys in sorted order
     */
    @SuppressWarnings("unchecked")
    public ObjectList<Key> sortedKeys()
    {
        return sortedKeys((a, b) -> ((Comparable<Key>) a).compareTo(b));
    }

    /**
     * Returns a list of keys in sorted order, using the given comparator
     */
    public ObjectList<Key> sortedKeys(Comparator<Key> comparator)
    {
        ObjectList<Key> keys = new ObjectList<>(keySet());
        keys.sort(comparator);
        return keys;
    }

    @Override
    public String toString()
    {
        return join(", ");
    }

    /**
     * Returns the top entries, up to the maximum
     */
    public CountMap<Key> top(Maximum maximum)
    {
        return top(maximum, Map.Entry.comparingByValue());
    }

    /**
     * Returns the top entries, up to the maximum, in sorted order
     */
    public CountMap<Key> top(Maximum maximum, Comparator<Map.Entry<Key, ConcurrentMutableCount>> comparator)
    {
        var top = new CountMap<Key>();
        for (var entry : descendingEntries(maximum, comparator))
        {
            top.plus(entry.getKey(), entry.getValue());
        }
        return top;
    }

    /**
     * Returns the total of all counts in this map
     */
    public long total()
    {
        return total.get();
    }
}
