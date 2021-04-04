////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.messages;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageType;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public abstract class OperationStatusMessage extends OperationMessage
{
    protected OperationStatusMessage()
    {
    }

    protected OperationStatusMessage(final String message, final Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    @Override
    public final OperationStatus operationStatus()
    {
        return OperationStatus.NOT_APPLICABLE;
    }
}
