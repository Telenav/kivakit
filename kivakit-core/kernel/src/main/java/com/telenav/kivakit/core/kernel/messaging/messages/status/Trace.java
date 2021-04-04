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
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageType;

/**
 * A trace message with no severity level. traces can be turned on and off through the {@link Debug} class.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Trace extends OperationStatusMessage
{
    public Trace(final String message, final Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    public Trace(final Throwable cause, final String message, final Object... arguments)
    {
        super(message + ": " + cause.getMessage());
        cause(cause);
        arguments(arguments);
    }

    public Trace()
    {
    }

    @Override
    public Severity severity()
    {
        return Severity.NONE;
    }

    @Override
    public Status status()
    {
        return Status.SUCCEEDED;
    }
}
