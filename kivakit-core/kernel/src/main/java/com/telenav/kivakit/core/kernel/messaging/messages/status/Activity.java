////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.messages.status;

import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.core.kernel.messaging.messages.Severity;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A message sent to indicate that an activity that might be monitored has occurred. For example, a resource (usually a
 * large resource) might have been allocated. This can be used to track, for example, memory consumption.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Activity extends OperationStatusMessage
{
    public Activity()
    {
    }

    public Activity(final String message, final Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    public Activity(final Throwable cause, final String message, final Object... arguments)
    {
        super(message + ": " + Message.escape(cause.getMessage()));
        cause(cause);
        arguments(arguments);
    }

    @Override
    public Severity severity()
    {
        return Severity.LOW;
    }

    @Override
    public Status status()
    {
        return Status.SUCCEEDED;
    }
}
