////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.comparison;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceComparison;

/**
 * Determines if two objects are equal. Unlike the {@link Object#equals(Object)} method, this interface is typesafe.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceComparison.class)
public interface Equality<Value>
{
    /**
     * @return True if value a is equal to value b
     */
    boolean isEqual(Value a, Value b);
}
