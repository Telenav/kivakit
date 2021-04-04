////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.code;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceCode;

/**
 * A piece of code that can be executed in a loop. The {@link #iteration(int)} method is called with the iteration
 * number for each time through the loop.
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceCode.class)
public interface Loopable
{
    /**
     * Executes the target code for the nth iteration
     *
     * @param iteration The loop iteration
     */
    void iteration(int iteration);
}
