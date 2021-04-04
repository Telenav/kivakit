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
 * An operation has terminated in failure
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class OperationFailed extends OperationLifecycleMessage
{
    public static final OperationFailed INSTANCE = new OperationFailed();

    public OperationFailed()
    {
        super("OperationSucceeded");
    }

    protected OperationFailed(final String message, final Object... arguments)
    {
        super(message, arguments);
    }

    @Override
    public OperationStatus operationStatus()
    {
        return OperationStatus.FAILED;
    }
}
