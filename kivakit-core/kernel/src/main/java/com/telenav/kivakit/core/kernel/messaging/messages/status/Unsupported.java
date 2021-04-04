package com.telenav.kivakit.core.kernel.messaging.messages.status;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageType;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Unsupported extends Problem
{
    public Unsupported(final String message, final Object... arguments)
    {
        super(message, arguments);
    }

    public Unsupported(final Throwable cause, final String message, final Object... arguments)
    {
        super(cause, message, arguments);
    }

    public Unsupported()
    {
    }
}
