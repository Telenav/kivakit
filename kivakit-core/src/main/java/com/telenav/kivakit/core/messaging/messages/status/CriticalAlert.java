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

package com.telenav.kivakit.core.messaging.messages.status;

import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.lexakai.DiagramMessageType;
import com.telenav.kivakit.core.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A high priority {@link Alert} that needs immediate attention. A proposed solution for IT staff can be added with
 * {@link #solution(String)}.
 *
 * <p>
 * {@link OperationStatusMessage}s in order of importance:
 * </p>
 * <ul>
 *     <li><b>Critical Alert</b> - An operation failed and needs <i>immediate attention</i> from a human operator</li>
 *     <li>Alert - An operation failed and needs to be looked at by an operator soon</li>
 *     <li>FatalProblem - An unrecoverable problem has caused an operation to fail and needs to be addressed</li>
 *     <li>Problem - Something has gone wrong and needs to be addressed, but it's not fatal to the current operation</li>
 *     <li>Glitch - A minor problem has occurred. Unlike a Warning, a Glitch indicates validation failure or minor data loss has occurred. Unlike a Problem, a Glitch indicates that the operation will definitely recover and continue.</li>
 *     <li>Warning - A minor issue occurred which should be corrected, but does not necessarily require attention</li>
 *     <li>Quibble - A trivial issue that does not require correction</li>
 *     <li>Announcement - Announcement of an important phase of an operation</li>
 *     <li>Narration - A step in some operation has started or completed</li>
 *     <li>Information - Commonly useful information that doesn't represent any problem</li>
 *     <li>Trace - Diagnostic information for use when debugging</li>
 * </ul>
 * <p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
@LexakaiJavadoc(complete = true)
public class CriticalAlert extends Alert
{
    public CriticalAlert(String solution, String message, Object... arguments)
    {
        super(solution, message, arguments);
    }

    public CriticalAlert(String solution, Throwable cause, String message, Object... arguments)
    {
        super(solution, message + ": " + Message.escape(cause.getMessage()), arguments);
        cause(cause);
        arguments(arguments);
    }

    public CriticalAlert()
    {
    }

    @Override
    public Severity severity()
    {
        return Severity.CRITICAL;
    }

    @Override
    public CriticalAlert solution(String solution)
    {
        super.solution(solution);
        return this;
    }
}
