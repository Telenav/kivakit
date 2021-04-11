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
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * Keeps a count for each key allowing for safe concurrency.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class ConcurrentCountMap<Key>
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

    private final ConcurrentHashMap<Key, AtomicLong> counts = new ConcurrentHashMap<>();

    public ConcurrentCountMap<Key> add(final Key key, final Count value)
    {
        add(key, value.get());
        return this;
    }

    public ConcurrentCountMap<Key> add(final Key key, final Countable value)
    {
        add(key, value.count());
        return this;
    }

    public Count add(final Key key, final long value)
    {
        final var total = get(key).addAndGet(value);
        if (total < 0)
        {
            LOGGER.warning("Adding $ to count caused long overflow", value);
        }
        this.total.addAndGet(value);
        return Count.count(total);
    }

    public boolean contains(final Key key)
    {
        return counts.containsKey(key);
    }

    public Count count(final Key key)
    {
        return Count.count(get(key).get());
    }

    public ConcurrentCountMap<Key> decrement(final Key key)
    {
        get(key).decrementAndGet();
        total.decrementAndGet();
        return this;
    }

    public List<Map.Entry<Key, AtomicLong>> descendingEntries(final Count maximum,
                                                              final Comparator<Map.Entry<Key, AtomicLong>> comparator)
    {
        assert maximum != null;
        final List<Map.Entry<Key, AtomicLong>> sorted = new ArrayList<>(counts.entrySet());
        sorted.sort(comparator.reversed());
        return sorted.subList(0, Math.min(sorted.size(), maximum.asInt()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof ConcurrentCountMap)
        {
            final var that = (ConcurrentCountMap<Key>) object;
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

    public ConcurrentCountMap<Key> increment(final Key key)
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

    public void mergeIn(final ConcurrentCountMap<Key> that)
    {
        for (final var entry : that.counts.entrySet())
        {
            add(entry.getKey(), entry.getValue().get());
        }
    }

    public Key minimum()
    {
        var minimumCount = Long.MAX_VALUE;
        Key minimum = null;
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

    public ConcurrentCountMap<Key> prune(final Count minimum)
    {
        final var counts = new ConcurrentCountMap<Key>();
        for (final var entry : this.counts.entrySet())
        {
            if (entry.getValue().get() >= minimum.get())
            {
                counts.add(entry.getKey(), entry.getValue().get());
            }
        }
        return counts;
    }

    public void remove(final Key key)
    {
        counts.remove(key);
    }

    public int size()
    {
        return counts.size();
    }

    public List<Key> sortedByDescendingCount()
    {
        final List<Map.Entry<Key, AtomicLong>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(Comparator.comparingLong(entry -> entry.getValue().get()));
        final List<Key> sorted = new ArrayList<>();
        for (final var entry : entries)
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

    public List<Key> sortedKeys(final Comparator<Key> comparator)
    {
        final List<Key> keys = new ArrayList<>(keySet());
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

    public ConcurrentCountMap<Key> top(final Count maximum)
    {
        return top(maximum, Comparator.comparingLong(entry -> entry.getValue().get()));
    }

    public ConcurrentCountMap<Key> top(final Count maximum, final Comparator<Map.Entry<Key, AtomicLong>> comparator)
    {
        final var top = new ConcurrentCountMap<Key>();
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

    private AtomicLong get(final Key key)
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
