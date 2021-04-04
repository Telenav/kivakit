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
 * An object which has a value that can be queried by calling {@link #value()}.
 *
 * @param <Value> The type of value
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceValue.class)
public interface Valued<Value>
{
    /**
     * @return The value
     */
    Value value();
}
