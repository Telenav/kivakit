////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.collection;

import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceCollection;

/**
 * An object which has an index and is also {@link Quantizable} as are many objects.
 *
 * @author jonathanl (shibo)
 * @see Quantizable
 */
@UmlClassDiagram(diagram = DiagramInterfaceCollection.class)
public interface Indexed extends Quantizable
{
    /**
     * @return The index
     */
    int index();

    /**
     * @return The index as a quantum value
     */
    @Override
    default long quantum()
    {
        return index();
    }
}
