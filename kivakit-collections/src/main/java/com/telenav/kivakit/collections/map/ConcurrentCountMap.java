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

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Countable;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Keeps a count for each key allowing for safe concurrency.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class ConcurrentCountMap<Key>
{
    private final ConcurrentHashMap<Key, AtomicLong> counts = new ConcurrentHashMap<>();

    private final AtomicLong total = new AtomicLong();

    public ConcurrentCountMap<Key> add(Key key, Count value)
    {
        add(key, value.get());
        return this;
    }

    public ConcurrentCountMap<Key> add(Key key, Countable value)
    {
        add(key, value.count());
        return this;
    }

    public Count add(Key key, long value)
    {
        var total = get(key).addAndGet(value);
        if (total < 0)
        {
            Ensure.warning("Adding $ to count caused long overflow", value);
        }
        this.total.addAndGet(value);
        return Count.count(total);
    }

    public boolean contains(Key key)
    {
        return counts.containsKey(key);
    }

    public Count count(Key key)
    {
        return Count.count(get(key).get());
    }

    public ConcurrentCountMap<Key> decrement(Key key)
    {
        get(key).decrementAndGet();
        total.decrementAndGet();
        return this;
    }

    public List<Map.Entry<Key, AtomicLong>> descendingEntries(Count maximum,
                                                              Comparator<Map.Entry<Key, AtomicLong>> comparator)
    {
        assert maximum != null;
        List<Map.Entry<Key, AtomicLong>> sorted = new ArrayList<>(counts.entrySet());
        sorted.sort(comparator.reversed());
        return sorted.subList(0, Math.min(sorted.size(), maximum.asInt()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof ConcurrentCountMap)
        {
            var that = (ConcurrentCountMap<Key>) object;
            if (counts.size() != that.counts.size())
            {
                return false;
            }
            for (var entry : counts.entrySet())
            {
                var value = that.counts.get(entry.getKey());
                if (value != null && value.get() != entry.getValue().get())
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

    public ConcurrentCountMap<Key> increment(Key key)
    {
        if (get(key).getAndIncrement() == Long.MAX_VALUE)
        {
            Ensure.fail("Count has exceeded maximum value");
        }
        total.incrementAndGet();
        return this;
    }

    public Collection<Key> keySet()
    {
        return counts.keySet();
    }

    public void mergeIn(ConcurrentCountMap<Key> that)
    {
        for (var entry : that.counts.entrySet())
        {
            add(entry.getKey(), entry.getValue().get());
        }
    }

    public Key minimum()
    {
        var minimumCount = Long.MAX_VALUE;
        Key minimum = null;
        for (var entry : counts.entrySet())
        {
            if (entry.getValue().get() < minimumCount)
            {
                minimum = entry.getKey();
                minimumCount = entry.getValue().get();
            }
        }
        return minimum;
    }

    public ConcurrentCountMap<Key> prune(Count minimum)
    {
        var counts = new ConcurrentCountMap<Key>();
        for (var entry : this.counts.entrySet())
        {
            if (entry.getValue().get() >= minimum.get())
            {
                counts.add(entry.getKey(), entry.getValue().get());
            }
        }
        return counts;
    }

    public void remove(Key key)
    {
        counts.remove(key);
    }

    public int size()
    {
        return counts.size();
    }

    public List<Key> sortedByDescendingCount()
    {
        List<Map.Entry<Key, AtomicLong>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(Comparator.comparingLong(entry -> entry.getValue().get()));
        List<Key> sorted = new ArrayList<>();
        for (var entry : entries)
        {
            sorted.add(entry.getKey());
        }
        return sorted;
    }

    @SuppressWarnings("unchecked")
    public List<Key> sortedKeys()
    {
        return sortedKeys((a, b) -> ((Comparable<Key>) a).compareTo(b));
    }

    public List<Key> sortedKeys(Comparator<Key> comparator)
    {
        List<Key> keys = new ArrayList<>(keySet());
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

    public ConcurrentCountMap<Key> top(Count maximum)
    {
        return top(maximum, Comparator.comparingLong(entry -> entry.getValue().get()));
    }

    public ConcurrentCountMap<Key> top(Count maximum, Comparator<Map.Entry<Key, AtomicLong>> comparator)
    {
        var top = new ConcurrentCountMap<Key>();
        for (var entry : descendingEntries(maximum, comparator))
        {
            top.add(entry.getKey(), entry.getValue().get());
        }
        return top;
    }

    public long total()
    {
        return total.get();
    }

    private AtomicLong get(Key key)
    {
        var count = counts.get(key);
        if (count == null)
        {
            counts.putIfAbsent(key, new AtomicLong());
            count = counts.get(key);
        }
        return count;
    }
}
