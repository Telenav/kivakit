////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.messages.status;

import com.telenav.kivakit.core.kernel.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.core.kernel.messaging.messages.Severity;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageType;

/**
 * The current step succeeded and does not indicate any problem
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Success extends OperationStatusMessage
{
    public static final Success INSTANCE = new Success();

    public Success(final String message, final Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    public Success()
    {
    }

    @Override
    public Severity severity()
    {
        return Severity.NONE;
    }

    @Override
    public final Status status()
    {
        return Status.SUCCEEDED;
    }
}
