////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.messages.status;

import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.messages.Severity;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramMessageType.class)
public class CriticalAlert extends Alert
{
    public CriticalAlert(final String solution, final String message, final Object... arguments)
    {
        super(solution, message, arguments);
    }

    public CriticalAlert(final String solution, final Throwable cause, final String message, final Object... arguments)
    {
        super(solution, message + ": " + Message.escape(cause.getMessage()), arguments);
        cause(cause);
        arguments(arguments);
    }

    public CriticalAlert()
    {
    }

    @Override
    public Severity severity()
    {
        return Severity.CRITICAL;
    }
}
