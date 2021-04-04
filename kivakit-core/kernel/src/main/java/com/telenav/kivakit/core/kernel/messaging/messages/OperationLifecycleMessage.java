////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.messages;

import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationFailed;
import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationStarted;
import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationSucceeded;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageType;

/**
 * A message representing a state transition in the lifecycle of an operation. For example, an operation may have failed
 * or succeeded or someone may need to be alerted of a serious problem.
 *
 * @author jonathanl (shibo)
 * @see OperationStarted
 * @see OperationSucceeded
 * @see OperationFailed
 * @see OperationHalted
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public abstract class OperationLifecycleMessage extends OperationMessage
{
    protected OperationLifecycleMessage()
    {
    }

    protected OperationLifecycleMessage(final String message, final Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    @Override
    public final Status status()
    {
        return Status.NOT_APPLICABLE;
    }
}
