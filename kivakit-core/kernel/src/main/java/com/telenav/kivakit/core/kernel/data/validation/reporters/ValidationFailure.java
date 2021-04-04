package com.telenav.kivakit.core.kernel.data.validation.reporters;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidationReporter;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An exception indicating validation failure that is thrown by {@link ThrowingValidationReporter}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataValidationReporter.class)
public class ValidationFailure extends RuntimeException
{
    public ValidationFailure(final Throwable cause, final String message)
    {
        super(message, cause);
    }
}
