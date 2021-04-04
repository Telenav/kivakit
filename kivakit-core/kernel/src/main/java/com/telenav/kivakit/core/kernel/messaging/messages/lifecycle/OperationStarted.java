////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.messages.lifecycle;

import com.telenav.kivakit.core.kernel.messaging.messages.OperationLifecycleMessage;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageType;

/**
 * An operation has started
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class OperationStarted extends OperationLifecycleMessage
{
    public static final OperationStarted INSTANCE = new OperationStarted();

    public OperationStarted()
    {
        super("OperationSucceeded");
    }

    protected OperationStarted(final String message, final Object... arguments)
    {
        super(message, arguments);
    }

    @Override
    public OperationStatus operationStatus()
    {
        return OperationStatus.STARTED;
    }
}
