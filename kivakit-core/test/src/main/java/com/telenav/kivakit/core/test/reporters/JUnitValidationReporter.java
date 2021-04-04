package com.telenav.kivakit.core.test.reporters;

import com.telenav.kivakit.core.kernel.data.validation.BaseValidationReporter;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.test.project.lexakai.diagrams.DiagramTest;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.junit.Assert;

import static com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter.Format.WITHOUT_EXCEPTION;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramTest.class)
public class JUnitValidationReporter extends BaseValidationReporter
{
    @Override
    public void report(final Message message)
    {
        Assert.fail(message.formatted(WITHOUT_EXCEPTION));
    }
}
