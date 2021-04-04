package com.telenav.kivakit.core.kernel.data.validation;

import com.telenav.kivakit.core.kernel.data.validation.reporters.AssertingValidationReporter;
import com.telenav.kivakit.core.kernel.data.validation.reporters.LogValidationReporter;
import com.telenav.kivakit.core.kernel.data.validation.reporters.NullValidationReporter;
import com.telenav.kivakit.core.kernel.data.validation.reporters.ThrowingValidationReporter;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidation;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidationReporter;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageListenerType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Reporter of validation issues.
 *
 * @author jonathanl (shibo)
 * @see ThrowingValidationReporter
 * @see LogValidationReporter
 * @see AssertingValidationReporter
 * @see NullValidationReporter
 */
@UmlClassDiagram(diagram = DiagramMessageListenerType.class)
@UmlClassDiagram(diagram = DiagramDataValidation.class)
@UmlClassDiagram(diagram = DiagramDataValidationReporter.class)
public interface ValidationReporter extends Listener
{
    /**
     * Reports a validation issue
     */
    void report(Message message);
}
