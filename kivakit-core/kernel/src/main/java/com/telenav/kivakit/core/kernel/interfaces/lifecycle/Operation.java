////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.lifecycle;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceLifeCycle;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An operation that can be executing. Calling {@link #isRunning()} will return true if the operation is running.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceLifeCycle.class)
public interface Operation
{
    /**
     * @return True if this operation is in progress, false if it has not started yet or if it has been stopped.
     */
    boolean isRunning();
}
