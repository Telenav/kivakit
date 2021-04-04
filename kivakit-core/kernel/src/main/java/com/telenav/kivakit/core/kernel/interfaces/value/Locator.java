////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.value;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Locates a value of the given type. This interface is the same as other interfaces in terms of function, but sometimes
 * {@link Locator} is the right name to use.
 *
 * @param <Value> the type of value to locate.
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceValue.class)
public interface Locator<Value>
{
    /**
     * @return The located value
     */
    Value locate();
}
