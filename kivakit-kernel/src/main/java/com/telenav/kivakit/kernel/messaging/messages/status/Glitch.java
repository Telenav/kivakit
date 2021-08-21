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

package com.telenav.kivakit.kernel.messaging.messages.status;

import com.telenav.kivakit.kernel.data.validation.ValidationIssues;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.kernel.messaging.messages.Severity;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A glitch is an issue that represents a temporary problem where recovery will happen. Unlike a {@link Warning},
 * however, a glitch should be addressed because it is causing an invalid state and possibly data loss. The {@link
 * ValidationIssues#isValid()} method returns false if it contains a {@link Problem} or a {@link Glitch} (or anything
 * worse), but not if it contains a {@link Warning}. You can think of a glitch as a "stern warning" and an indication
 * that you are possibly losing data.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Glitch extends OperationStatusMessage
{
    public Glitch(final String message, final Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    public Glitch(final Throwable cause, final String message, final Object... arguments)
    {
        super(message + ": " + Message.escape(cause.getMessage()));
        cause(cause);
        arguments(arguments);
    }

    public Glitch()
    {
    }

    @Override
    public Severity severity()
    {
        return Severity.LOW;
    }

    @Override
    public Status status()
    {
        return Status.RESULT_COMPROMISED;
    }
}
