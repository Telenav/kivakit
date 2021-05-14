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
import com.telenav.kivakit.kernel.language.collections.map.BaseMap;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A map that has a fixed size and that deletes the oldest data when new one is added. It behaves like a cache
 *
 * @param <Key> The Key
 * @param <Value> The Value
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMap.class)
public class CacheMap<Key, Value> extends BaseMap<Key, Value>
{
    /**
     * Constructor
     *
     * @param cacheSize The size after which the eldest entry will be deleted to leave room for new entries.
     */
    public CacheMap(final Maximum cacheSize)
    {
        super(Maximum.MAXIMUM, new LinkedHashMap<>()
        {
            @Override
            protected boolean removeEldestEntry(final Map.Entry<Key, Value> eldest)
            {
                return size() >= cacheSize.asInt();
            }
        });
    }
}
