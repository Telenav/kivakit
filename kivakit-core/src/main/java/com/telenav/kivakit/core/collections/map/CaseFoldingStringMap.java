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
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * A {@link StringMap} where values that are added with {@link #put(Object, Object)} are automatically converted to
 * lowercase and can be retrieved with {@link #get(Object)} in a case-independent manner.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTATION_COMPLETE)
public class CaseFoldingStringMap<Value> extends StringMap<Value>
{
    /**
     * Constructs a case folding map from string to value, of the given size.
     */
    public CaseFoldingStringMap(Maximum maximumSize)
    {
        super(maximumSize);
    }

    /**
     * Constructs a case folding map from string to value, with no maximum size
     */
    public CaseFoldingStringMap()
    {
        super(Maximum.MAXIMUM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Value get(Object key)
    {
        if (key == null)
        {
            return null;
        }
        if (key instanceof String)
        {
            return super.get(key.toString().toLowerCase());
        }
        fail("Key must be a String");
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Value getOrDefault(Object key, Value defaultValue)
    {
        var value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Value put(String key, Value value)
    {
        return super.put(key.toLowerCase(), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Value put(String key, Value value, Value defaultValue)
    {
        if (value != null)
        {
            return put(key, value);
        }
        else
        {
            return put(key, defaultValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(@NotNull Map<? extends String, ? extends Value> that)
    {
        for (var key : that.keySet())
        {
            put(key, that.get(key));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable Value putIfAbsent(@NotNull String key, Value valueIfAbsent)
    {
        var value = get(key);
        if (value == null)
        {
            return put(key, valueIfAbsent);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean putIfNotNull(String key, Value value)
    {
        if (value != null)
        {
            put(key, value);
            return true;
        }
        return false;
    }
}
