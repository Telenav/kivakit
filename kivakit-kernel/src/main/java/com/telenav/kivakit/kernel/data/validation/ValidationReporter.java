////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.data.validation;

import com.telenav.kivakit.kernel.data.validation.reporters.AssertingValidationReporter;
import com.telenav.kivakit.kernel.data.validation.reporters.LogValidationReporter;
import com.telenav.kivakit.kernel.data.validation.reporters.NullValidationReporter;
import com.telenav.kivakit.kernel.data.validation.reporters.ThrowingValidationReporter;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataValidation;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataValidationReporter;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageListenerType;
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
    @Override
    default void onMessage(final Message message)
    {
        report(message);
    }

    /**
     * Reports a validation issue
     */
    void report(Message message);
}
