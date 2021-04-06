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

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.core.kernel.interfaces.factory.MapFactory;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Countable;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * Keeps a count for each key.
 *
 * @author jonathanl (shibo)
 */
public class ConcurrentCountMap<T>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Pattern KEY_VALUE = Pattern.compile("(?:key\\w+)\\s*=\\s*(?:count\\w+)");

    public static <T> ConcurrentCountMap<T> parse(final Factory<ConcurrentCountMap<T>> mapFactory,
                                                  final MapFactory<String, T> keyFactory, final String text)
    {
        final var map = mapFactory.newInstance();
        for (final var entry : text.split(",\\s*"))
        {
            final var matcher = KEY_VALUE.matcher(entry);
            if (matcher.matches())
            {
                final var key = keyFactory.newInstance(matcher.group("key"));
                final var count = Count.parse(matcher.group("count"));
                if (key != null && count != null)
                {
                    map.add(key, count);
                }
            }
        }
        return map;
    }

    private final AtomicLong total = new AtomicLong();

    private final ConcurrentHashMap<T, AtomicLong> counts = new ConcurrentHashMap<>();

    public ConcurrentCountMap<T> add(final T key, final Count value)
    {
        add(key, value.get());
        return this;
    }

    public ConcurrentCountMap<T> add(final T key, final Countable value)
    {
        add(key, value.count());
        return this;
    }

    public Count add(final T key, final long value)
    {
        final var total = get(key).addAndGet(value);
        if (total < 0)
        {
            LOGGER.warning("Adding $ to count caused long overflow", value);
        }
        this.total.addAndGet(value);
        return Count.count(total);
    }

    public boolean contains(final T key)
    {
        return counts.containsKey(key);
    }

    public Count count(final T key)
    {
        return Count.count(get(key).get());
    }

    public ConcurrentCountMap<T> decrement(final T key)
    {
        get(key).decrementAndGet();
        total.decrementAndGet();
        return this;
    }

    public List<Map.Entry<T, AtomicLong>> descendingEntries(final Count maximum,
                                                            final Comparator<Map.Entry<T, AtomicLong>> comparator)
    {
        assert maximum != null;
        final List<Map.Entry<T, AtomicLong>> sorted = new ArrayList<>(counts.entrySet());
        sorted.sort(comparator.reversed());
        return sorted.subList(0, Math.min(sorted.size(), maximum.asInt()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof ConcurrentCountMap)
        {
            final var that = (ConcurrentCountMap<T>) object;
            if (counts.size() != that.counts.size())
            {
                return false;
            }
            for (final var entry : counts.entrySet())
            {
                final var value = that.counts.get(entry.getKey());
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

    public ConcurrentCountMap<T> increment(final T key)
    {
        if (get(key).getAndIncrement() == Long.MAX_VALUE)
        {
            Ensure.fail("Count has exceeded maximum value");
        }
        total.incrementAndGet();
        return this;
    }

    public Collection<T> keySet()
    {
        return counts.keySet();
    }

    public void mergeIn(final ConcurrentCountMap<T> that)
    {
        for (final var entry : that.counts.entrySet())
        {
            add(entry.getKey(), entry.getValue().get());
        }
    }

    public T minimum()
    {
        var minimumCount = Long.MAX_VALUE;
        T minimum = null;
        for (final var entry : counts.entrySet())
        {
            if (entry.getValue().get() < minimumCount)
            {
                minimum = entry.getKey();
                minimumCount = entry.getValue().get();
            }
        }
        return minimum;
    }

    public ConcurrentCountMap<T> prune(final Count minimum)
    {
        final var counts = new ConcurrentCountMap<T>();
        for (final var entry : this.counts.entrySet())
        {
            if (entry.getValue().get() >= minimum.get())
            {
                counts.add(entry.getKey(), entry.getValue().get());
            }
        }
        return counts;
    }

    public void remove(final T key)
    {
        counts.remove(key);
    }

    public int size()
    {
        return counts.size();
    }

    public List<T> sortedByDescendingCount()
    {
        final List<Map.Entry<T, AtomicLong>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(Comparator.comparingLong(entry -> entry.getValue().get()));
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

    public ConcurrentCountMap<T> top(final Count maximum)
    {
        return top(maximum, Comparator.comparingLong(entry -> entry.getValue().get()));
    }

    public ConcurrentCountMap<T> top(final Count maximum, final Comparator<Map.Entry<T, AtomicLong>> comparator)
    {
        final var top = new ConcurrentCountMap<T>();
        for (final var entry : descendingEntries(maximum, comparator))
        {
            top.add(entry.getKey(), entry.getValue().get());
        }
        return top;
    }

    public long total()
    {
        return total.get();
    }

    private AtomicLong get(final T key)
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
