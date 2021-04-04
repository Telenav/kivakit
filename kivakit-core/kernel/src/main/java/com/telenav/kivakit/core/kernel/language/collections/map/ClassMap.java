////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.collections.map;

import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A map from class to value.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsMap.class)
public class ClassMap<T> extends BaseMap<Class<?>, T>
{
    public ClassMap(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    public void put(final T object)
    {
        put(object.getClass(), object);
    }
}
