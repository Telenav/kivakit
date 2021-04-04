package com.telenav.kivakit.core.kernel.data.validation;

import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidation;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidationReporter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * Base implementation of validation reporter that reports any message it hears in {@link #onMessage(Message)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataValidation.class)
@UmlClassDiagram(diagram = DiagramDataValidationReporter.class)
public abstract class BaseValidationReporter implements ValidationReporter
{
    /**
     * Reports the given message
     */
    @Override
    @UmlExcludeMember
    public final void onMessage(final Message message)
    {
        report(message);
    }
}
