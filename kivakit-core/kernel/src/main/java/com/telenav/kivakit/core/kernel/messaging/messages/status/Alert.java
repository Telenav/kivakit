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

@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Alert extends OperationStatusMessage
{
    public Alert(final String solution, final String message, final Object... arguments)
    {
        super(message + "\nSOLUTION: " + solution);
        cause(new Throwable());
        arguments(arguments);
    }

    public Alert()
    {
    }

    @Override
    public Severity severity()
    {
        return Severity.HIGH;
    }

    @Override
    public Status status()
    {
        return Status.FAILED;
    }
}
