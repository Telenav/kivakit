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
 * Interface to something that has a {@link Count} value.
 *
 * @author jonathanl (shibo)
 * @see Count
 */
@UmlClassDiagram(diagram = DiagramInterfaceNumeric.class)
@UmlClassDiagram(diagram = DiagramExampleBaseList.class)
public interface Countable
{
    /**
     * @return The count
     */
    Count count();
}
