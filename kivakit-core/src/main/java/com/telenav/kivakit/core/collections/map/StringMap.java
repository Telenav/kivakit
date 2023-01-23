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

import java.util.TreeMap;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.collections.map.StringMap.KeyCaseSensitivity.PRESERVE_CASE;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;

/**
 * A bounded string map using a {@link TreeMap} implementation.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTED)
public class StringMap<Value> extends BaseStringMap<Value>
{
    public enum KeyCaseSensitivity
    {
        PRESERVE_CASE,
        FOLD_CASE_LOWER,
        FOLD_CASE_UPPER
    }

    private KeyCaseSensitivity keyCaseSensitivity = PRESERVE_CASE;

    public StringMap()
    {
        super(MAXIMUM, new TreeMap<>());
    }

    public StringMap(Maximum maximumSize)
    {
        super(maximumSize, new TreeMap<>());
    }

    /**
     * Returns a copy of this string map
     */
    public StringMap<Value> copy()
    {
        var copy = new StringMap<Value>();
        copy.putAll(this);
        return copy;
    }

    @Override
    public Value get(String key, Value defaultValue)
    {
        return super.get(fold(key), defaultValue);
    }

    @Override
    public Value put(String key, Value value)
    {
        return super.put(fold(key), value);
    }

    public StringMap<Value> withKeyCaseSensitivity(KeyCaseSensitivity keyCaseSensitivity)
    {
        var copy = copy();
        copy.keyCaseSensitivity = keyCaseSensitivity;
        return copy;
    }

    private String fold(String key)
    {
        return switch (keyCaseSensitivity)
            {
                case PRESERVE_CASE -> key;
                case FOLD_CASE_LOWER -> key.toLowerCase();
                case FOLD_CASE_UPPER -> key.toUpperCase();
            };
    }
}
