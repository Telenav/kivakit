////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.collections.map.string;

import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.TreeMap;

/**
 * A bounded string map using a {@link TreeMap} implementation.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsMap.class)
public class StringMap<T> extends BaseStringMap<T>
{
    public StringMap()
    {
        super(Maximum.MAXIMUM, new TreeMap<>());
    }

    public StringMap(final Maximum maximumSize)
    {
        super(maximumSize, new TreeMap<>());
    }
}
