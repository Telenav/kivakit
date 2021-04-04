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
 * A step in a narrative describing a series of successful steps performed by an operation.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Narration extends OperationStatusMessage
{
    public Narration(final String message, final Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    public Narration()
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
