////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.model;

import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceModel;

/**
 * An object which has an identifier and is also {@link Quantizable} as are many objects.
 *
 * @author jonathanl (shibo)
 * @see Quantizable
 */
@UmlClassDiagram(diagram = DiagramInterfaceModel.class)
public interface Identifiable extends Quantizable
{
    /**
     * @return The identifier for this object
     */
    long identifier();

    /**
     * @return The identifier as a quantum
     */
    @Override
    default long quantum()
    {
        return identifier();
    }
}
