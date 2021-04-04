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
import com.telenav.kivakit.core.kernel.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A problem that is severe enough to result in data loss, but not severe enough to halt the current operation. If the
 * operation will not succeed, {@link OperationHalted} should be used instead.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Problem extends OperationStatusMessage
{
    public Problem(final String message, final Object... arguments)
    {
        super(message);
        cause(new Throwable("Problem stack trace"));
        arguments(arguments);
    }

    public Problem(final Throwable cause, final String message, final Object... arguments)
    {
        super(message + ": " + Message.escape(cause.getMessage()));
        cause(cause);
        arguments(arguments);
    }

    public Problem()
    {
    }

    @Override
    public Severity severity()
    {
        return Severity.MEDIUM_HIGH;
    }

    @Override
    public Status status()
    {
        return Status.PROBLEM;
    }
}
