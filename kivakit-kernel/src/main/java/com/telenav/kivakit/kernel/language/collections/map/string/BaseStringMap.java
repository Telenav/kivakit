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

package com.telenav.kivakit.kernel.language.collections.map.string;

import com.telenav.kivakit.kernel.language.collections.map.BaseMap;
import com.telenav.kivakit.kernel.language.reflection.property.Property;
import com.telenav.kivakit.kernel.language.reflection.property.PropertyValueSource;
import com.telenav.kivakit.kernel.language.strings.CaseFormat;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsMap;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * A bounded map from string to value which can serve as a {@link PropertyValueSource}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsMap.class)
public abstract class BaseStringMap<Value> extends BaseMap<String, Value> implements PropertyValueSource
{
    protected BaseStringMap(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    protected BaseStringMap(final Maximum maximumSize, final Map<String, Value> map)
    {
        super(maximumSize, map);
    }

    public StringList asStringList()
    {
        final var entries = new StringList();
        final var keys = new ArrayList<>(keySet());
        keys.sort(Comparator.naturalOrder());
        for (final var key : keys)
        {
            entries.add(key + " = " + get(key));
        }
        return entries;
    }

    /**
     * @return The keys and values of this map separated by the given separator
     */
    public String join(final String separator)
    {
        return asStringList().join(separator);
    }

    @Override
    public String toString()
    {
        return join("\n");
    }

    @Override
    public Value valueFor(final Property property)
    {
        final var value = get(property.name());
        if (value != null)
        {
            return value;
        }
        return get(CaseFormat.camelCaseToHyphenated(property.name()));
    }
}
