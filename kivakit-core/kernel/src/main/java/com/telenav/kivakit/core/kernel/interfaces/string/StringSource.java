////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.string;

import com.telenav.kivakit.core.kernel.interfaces.value.Source;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceString;

/**
 * Sadly, the only reason for this class is Java type erasures. Because you cannot implement more than one {@link
 * Source} interface on an object at a time due to type erasure collisions, creating an interface like this one is the
 * only work-around.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceString.class)
public interface StringSource extends Source<String>
{
    /**
     * @return The string value
     */
    @Override
    default String get()
    {
        return string();
    }

    /**
     * @return The string value
     */
    String string();
}
