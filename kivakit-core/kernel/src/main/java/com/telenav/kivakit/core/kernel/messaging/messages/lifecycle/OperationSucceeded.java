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
 * A message representing the success of an operation
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class OperationSucceeded extends OperationLifecycleMessage
{
    public static final OperationSucceeded INSTANCE = new OperationSucceeded();

    public OperationSucceeded()
    {
        super("OperationSucceeded");
    }

    protected OperationSucceeded(final String message, final Object... arguments)
    {
        super(message, arguments);
    }

    @Override
    public OperationStatus operationStatus()
    {
        return OperationStatus.SUCCEEDED;
    }
}
