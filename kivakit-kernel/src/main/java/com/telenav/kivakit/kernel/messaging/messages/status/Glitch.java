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
 * <p>
 * {@link OperationStatusMessage}s in order of importance:
 * </p>
 * <ul>
 *     <li>Critical Alert - An operation failed and needs <i>immediate attention</i> from a human operator</li>
 *     <li>Alert - An operation failed and needs to be looked at by an operator soon</li>
 *     <li>FatalProblem - An unrecoverable problem has caused an operation to fail and needs to be addressed</li>
 *     <li>Problem - Something has gone wrong and needs to be addressed, but it's not fatal to the current operation</li>
 *     <li><b>Glitch</b> - A minor problem has occurred. Unlike a Warning, a Glitch indicates validation failure or minor data loss has occurred. Unlike a Problem, a Glitch indicates that the operation will definitely recover and continue.</li>
 *     <li>Warning - A minor issue occurred which should be corrected, but does not necessarily require attention</li>
 *     <li>Quibble - A trivial issue that does not require correction</li>
 *     <li>Announcement - Announcement of an important phase of an operation</li>
 *     <li>Narration - A step in some operation has started or completed</li>
 *     <li>Information - Commonly useful information that doesn't represent any problem</li>
 *     <li>Trace - Diagnostic information for use when debugging</li>
 * </ul>
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