////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.numeric;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramExampleBaseList;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceNumeric;

/**
 * An object (usually some sort of collection or store of values) which has a size.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceNumeric.class)
@UmlClassDiagram(diagram = DiagramExampleBaseList.class)
public interface Sized extends Countable
{
    @Override
    default Count count()
    {
        return Count.count(size());
    }

    /**
     * @return True if the size is zero
     */
    default boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * @return The size of the object
     */
    int size();
}
