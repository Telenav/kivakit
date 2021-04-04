////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.lifecycle;

import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceLifeCycle;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An operation that can be stopped
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceLifeCycle.class)
public interface Stoppable extends Operation
{
    /**
     * Stops this task, waiting no more than the given wait time before giving up.
     */
    void stop(Duration wait);

    /**
     * Stops this task, blocking until the operation is completed
     */
    default void stop()
    {
        stop(Duration.MAXIMUM);
    }
}
