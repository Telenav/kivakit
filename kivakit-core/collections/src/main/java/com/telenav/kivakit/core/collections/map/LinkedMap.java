////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.map;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramMap;
import com.telenav.kivakit.core.kernel.language.collections.map.BaseMap;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;

import java.util.LinkedHashMap;

/**
 * A {@link BaseMap} with {@link LinkedHashMap} as the implementation.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMap.class)
public class LinkedMap<Key, Value> extends BaseMap<Key, Value>
{
    public LinkedMap(final Maximum maximumSize)
    {
        super(maximumSize, new LinkedHashMap<>());
        checkSize(0);
    }
}
