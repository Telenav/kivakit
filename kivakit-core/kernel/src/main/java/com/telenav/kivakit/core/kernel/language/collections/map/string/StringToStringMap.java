////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.collections.map.string;

import com.telenav.kivakit.core.kernel.language.primitives.Ints;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A bounded map from string to string.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsMap.class)
public class StringToStringMap extends BaseStringMap<String>
{
    public StringToStringMap()
    {
        this(Maximum.MAXIMUM);
    }

    public StringToStringMap(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    public int asInt(final String key)
    {
        return Ints.parse(get(key), Integer.MIN_VALUE);
    }

    public Count count(final String key)
    {
        return Count.parse(get(key));
    }

    public String get(final String key)
    {
        return super.get(key);
    }
}
