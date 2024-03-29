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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;

/**
 * Class for concurrent maps with a bounded number of values.
 *
 * @author jonathanl
 * @author Junwei
 * @version 1.0.0 2012-12-27
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTED)
public class ConcurrentObjectMap<Key, Value> extends ObjectMap<Key, Value> implements
        java.util.concurrent.ConcurrentMap<Key, Value>
{
    /**
     * A bounded concurrent map
     */
    public ConcurrentObjectMap(Maximum maximumSize)
    {
        this(maximumSize, new ConcurrentHashMap<>());
    }

    /**
     * A bounded concurrent map with the given implementation
     */
    public ConcurrentObjectMap(Maximum maximumSize, java.util.concurrent.ConcurrentMap<Key, Value> map)
    {
        super(maximumSize, map);
    }

    /**
     * An unbounded concurrent map
     */
    public ConcurrentObjectMap()
    {
        this(MAXIMUM);
    }

    /**
     * An unbounded concurrent map with the given implementation
     */
    protected ConcurrentObjectMap(java.util.concurrent.ConcurrentMap<Key, Value> map)
    {
        this(MAXIMUM, map);
    }

    @Override
    public boolean replace(@NotNull Key key, @NotNull Value oldValue, @NotNull Value newValue)
    {
        return concurrentMap().replace(key, oldValue, newValue);
    }

    private ConcurrentHashMap<Key, Value> concurrentMap()
    {
        return (ConcurrentHashMap<Key, Value>) backingMap();
    }
}
