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
 * An operation that can be started or restarted.
 *
 * @author jonathanl (shibo)
 * @see Operation
 */
@UmlClassDiagram(diagram = DiagramInterfaceLifeCycle.class)
public interface Startable extends Operation
{
    /**
     * @return True if this task started
     */
    boolean start();
}
