////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.messages.lifecycle;

import com.telenav.kivakit.core.kernel.messaging.messages.OperationLifecycleMessage;
import com.telenav.kivakit.core.kernel.messaging.messages.Severity;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageType;

/**
 * A problem with severity high enough to halt the current operation. If the problem is not severe enough, {@link
 * Problem} should be used instead.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class OperationHalted extends OperationLifecycleMessage
{
    public OperationHalted()
    {
    }

    public OperationHalted(final String message, final Object... arguments)
    {
        super(message);
        cause(new Throwable("OperationHalted error"));
        arguments(arguments);
    }

    public OperationHalted(final Throwable cause, final String message, final Object... arguments)
    {
        super(message);
        cause(cause);
        arguments(arguments);
    }

    @Override
    public OperationStatus operationStatus()
    {
        return OperationStatus.HALTED;
    }

    @Override
    public Severity severity()
    {
        return Severity.HIGH;
    }
}
