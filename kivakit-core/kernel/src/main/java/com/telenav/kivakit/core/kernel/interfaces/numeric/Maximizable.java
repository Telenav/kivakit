////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.numeric;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceNumeric;

/**
 * Determines which of two objects is the maximum. For example, the Angle object is {@link Maximizable} and implements
 * this method:
 * <pre>
 *     public Angle maximum(final Angle that) { ... }
 * </pre>
 *
 * @param <Value> The type of object to compare
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceNumeric.class)
public interface Maximizable<Value>
{
    /**
     * @return The maximum of this value and the given value.
     */
    Value maximum(Value value);
}
