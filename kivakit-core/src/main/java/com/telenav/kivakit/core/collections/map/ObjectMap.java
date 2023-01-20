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
import com.telenav.kivakit.core.value.count.Maximum;

import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;

/**
 * A trivial extension of {@link BaseMap} for storing objects
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTED)
public class ObjectMap<Key, Value> extends BaseMap<Key, Value>
{
    /**
     * Returns an {@link ObjectMap} with the given maximum size
     */
    public static <Key, Value> ObjectMap<Key, Value> map(Maximum maximumSize)
    {
        return new ObjectMap<>(maximumSize);
    }

    /**
     * Returns a new {@link ObjectMap}
     */
    public static <Key, Value> ObjectMap<Key, Value> map()
    {
        return map(MAXIMUM);
    }

    public ObjectMap()
    {
    }

    public ObjectMap(Maximum maximumSize)
    {
        super(maximumSize);
    }

    public ObjectMap(Maximum maximumSize, Map<Key, Value> map)
    {
        super(maximumSize, map);
    }

    public ObjectMap(Map<Key, Value> map)
    {
        super(map);
    }

    public ObjectMap<Key, Value> copy()
    {
        var copy = new ObjectMap<Key, Value>();
        copy.putAll(this);
        return copy;
    }
}
