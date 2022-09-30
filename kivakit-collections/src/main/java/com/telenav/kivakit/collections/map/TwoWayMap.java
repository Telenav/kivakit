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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.collections.internal.lexakai.DiagramMap;
import com.telenav.kivakit.core.collections.map.BaseMap;
import com.telenav.kivakit.core.collections.map.LinkedMap;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A map from key to value, but also from value to key (with {@link #key(Object)}).
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMap.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class TwoWayMap<Key, Value> extends BaseMap<Key, Value>
{
    /** The reverse map from value to key */
    private final LinkedMap<Value, Key> valueToKey;

    public TwoWayMap()
    {
        this(Maximum.MAXIMUM);
    }

    public TwoWayMap(Maximum maximumSize)
    {
        valueToKey = new LinkedMap<>(maximumSize);
    }

    @Override
    public void clear()
    {
        super.clear();
        valueToKey.clear();
    }

    public Key key(Value value)
    {
        return valueToKey.get(value);
    }

    @Override
    public Value put(Key key, Value value)
    {
        valueToKey.put(value, key);
        return super.put(key, value);
    }

    @Override
    public Value remove(@NotNull Object key)
    {
        var value = super.remove(key);
        valueToKey.remove(value);
        return value;
    }
}
