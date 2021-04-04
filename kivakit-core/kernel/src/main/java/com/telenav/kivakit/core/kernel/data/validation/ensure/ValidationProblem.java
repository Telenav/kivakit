package com.telenav.kivakit.core.kernel.data.validation.ensure;

import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidationEnsure;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A validation problem reported by {@link Ensure} failures.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataValidationEnsure.class)
public class ValidationProblem extends Problem
{
    public ValidationProblem(final String message, final Object... arguments)
    {
        super(message, arguments);
    }

    public ValidationProblem(final Throwable cause, final String message, final Object... arguments)
    {
        super(cause, message, arguments);
    }

    public ValidationProblem()
    {
    }
}
