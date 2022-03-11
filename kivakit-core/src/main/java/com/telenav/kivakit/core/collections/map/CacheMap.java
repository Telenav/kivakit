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

import com.telenav.kivakit.core.project.lexakai.DiagramCollections;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A map that has a fixed size and that deletes the oldest entries when that size is exceeded. It also removes entries
 * that are older than the maximum age.
 *
 * @param <Key> The Key
 * @param <Value> The Value
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
public class CacheMap<Key, Value> extends BaseMap<Key, Value>
{
    /** The time that each entry was added or updated */
    private final Map<Key, Time> age = new HashMap<>();

    /** The maximum allowed age of an entry */
    private final Duration maximumEntryAge;

    /** True if this map should expire old entries */
    private final boolean expireOldEntries;

    public CacheMap(Maximum cacheSize)
    {
        this(cacheSize, Duration.MAXIMUM);
    }

    /**
     * Constructor
     *
     * @param cacheSize The size after which the eldest entry will be deleted to leave room for new entries.
     * @param maximumEntryAge The maximum age of an entry before it is expired
     */
    public CacheMap(Maximum cacheSize, Duration maximumEntryAge)
    {
        super(Maximum.MAXIMUM, new LinkedHashMap<>()
        {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Key, Value> eldest)
            {
                return size() >= cacheSize.asInt();
            }
        });

        this.maximumEntryAge = maximumEntryAge;
        this.expireOldEntries = !maximumEntryAge.isMaximum();
    }

    @Override
    public Value get(final Object key)
    {
        if (expireOldEntries && age.get(key).isGreaterThan(maximumEntryAge))
        {
            remove(key);
            return null;
        }
        return super.get(key);
    }

    @Override
    public Value put(final Key key, final Value value)
    {
        if (expireOldEntries)
        {
            age.put(key, Time.now());
        }
        return super.put(key, value);
    }

    @Override
    public Value remove(final Object key)
    {
        if (expireOldEntries)
        {
            age.remove(key);
        }
        return super.remove(key);
    }
}
