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
 * Uncategorized, generic information about the progress of the current operation with no severity and which does not
 * indicate any problem.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Information extends OperationStatusMessage
{
    public Information(final String message, final Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    public Information()
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
