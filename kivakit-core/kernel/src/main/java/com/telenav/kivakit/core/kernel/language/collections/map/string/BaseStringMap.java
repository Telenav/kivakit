////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.collections.map.string;

import com.telenav.kivakit.core.kernel.language.collections.map.BaseMap;
import com.telenav.kivakit.core.kernel.language.reflection.property.Property;
import com.telenav.kivakit.core.kernel.language.reflection.property.PropertyValueSource;
import com.telenav.kivakit.core.kernel.language.strings.CaseFormat;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;

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
