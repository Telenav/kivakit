package com.telenav.kivakit.core.kernel.data.validation.reporters;

import com.telenav.kivakit.core.kernel.data.validation.BaseValidationReporter;
import com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidationReporter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * A validation reporter that throws a {@link ValidationFailure} exception for any message that is reported to it.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataValidationReporter.class)
@UmlRelation(label = "throws", referent = ValidationFailure.class)
public class ThrowingValidationReporter extends BaseValidationReporter
{
    @Override
    @UmlExcludeMember
    public void report(final Message message)
    {
        throw new ValidationFailure(message.cause(), message.formatted(MessageFormatter.Format.WITH_EXCEPTION));
    }
}
