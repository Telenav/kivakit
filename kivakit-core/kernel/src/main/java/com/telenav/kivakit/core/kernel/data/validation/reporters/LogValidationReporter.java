package com.telenav.kivakit.core.kernel.data.validation.reporters;

import com.telenav.kivakit.core.kernel.data.validation.BaseValidationReporter;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidationReporter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * A validation reporter that logs any messages reported to it.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataValidationReporter.class)
public class LogValidationReporter extends BaseValidationReporter
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    @Override
    @UmlExcludeMember
    public void report(final Message message)
    {
        LOGGER.log(message);
    }
}
