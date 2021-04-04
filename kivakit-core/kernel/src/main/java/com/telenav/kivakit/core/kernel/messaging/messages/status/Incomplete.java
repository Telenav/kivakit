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
 * The current step had to discard some of the result to continue
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Incomplete extends OperationStatusMessage
{
    public static final Incomplete INSTANCE = new Incomplete();

    public Incomplete(final String message, final Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    public Incomplete(final Throwable cause, final String message, final Object... arguments)
    {
        super(message + ": " + Message.escape(cause.getMessage()));
        cause(cause);
        arguments(arguments);
    }

    public Incomplete()
    {
    }

    @Override
    public Severity severity()
    {
        return Severity.MEDIUM_HIGH;
    }

    @Override
    public final Status status()
    {
        return Status.RESULT_INCOMPLETE;
    }
}
