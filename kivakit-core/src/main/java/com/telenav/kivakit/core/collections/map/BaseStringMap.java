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

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.core.project.lexakai.DiagramCollections;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * A bounded map from string to value.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
public abstract class BaseStringMap<Value> extends BaseMap<String, Value>
{
    protected BaseStringMap(Maximum maximumSize)
    {
        super(maximumSize);
    }

    protected BaseStringMap(Maximum maximumSize, Map<String, Value> map)
    {
        super(maximumSize, map);
    }

    /**
     * @return The value of the given key as a double, or an exception is thrown if the value is invalid or missing
     */
    public double asDouble(String key)
    {
        return Double.parseDouble(key);
    }

    /**
     * @return The value of the given key as an integer, or an exception is thrown if the value is invalid or missing
     */
    public int asInt(String key)
    {
        return Integer.parseInt(asString(key));
    }

    /**
     * @return The value of the given key as a long, or an exception is thrown if the value is invalid or missing
     */
    public long asLong(String key)
    {
        return Long.parseLong(asString(key));
    }

    /**
     * @return The given key as a path with no trailing slash
     */
    public String asPath(String key)
    {
        var value = asString(key);
        return value == null ? null : Strip.trailing(value, "/");
    }

    public String asString(String key)
    {
        return (String) super.get(key);
    }

    public StringList asStringList()
    {
        var entries = new StringList();
        var keys = new ArrayList<>(keySet());
        keys.sort(Comparator.naturalOrder());
        for (var key : keys)
        {
            entries.add(key + " = " + get(key));
        }
        return entries;
    }

    /**
     * @return The given value as a {@link URI}
     */
    public URI asUri(String key)
    {
        return URI.create(key);
    }

    /**
     * @return The keys and values of this map separated by the given separator
     */
    public String join(String separator)
    {
        return asStringList().join(separator);
    }

    @Override
    public String toString()
    {
        return join("\n");
    }
}
