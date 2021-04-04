////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.value;

import com.telenav.kivakit.core.kernel.interfaces.factory.Factory;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceValue;

/**
 * A source of values.
 *
 * @param <Value> The object type
 * @author jonathanl (shibo)
 * @see Factory
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceValue.class)
public interface Source<Value>
{
    /**
     * Transmits the given value
     */
    Value get();
}
