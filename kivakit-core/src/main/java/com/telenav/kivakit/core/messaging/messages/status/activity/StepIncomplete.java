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

package com.telenav.kivakit.core.messaging.messages.status.activity;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.internal.lexakai.DiagramMessageType;
import com.telenav.kivakit.core.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * The current step had to discard some result to continue
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class StepIncomplete extends OperationStatusMessage
{
    public static final StepIncomplete INSTANCE = new StepIncomplete();

    public StepIncomplete(String message, Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    public StepIncomplete(Throwable cause, String message, Object... arguments)
    {
        super(message + ": " + Message.escapeMessageText(cause.getMessage()));
        cause(cause);
        arguments(arguments);
    }

    public StepIncomplete()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Severity severity()
    {
        return Severity.MEDIUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Status status()
    {
        return Status.RESULT_INCOMPLETE;
    }
}
