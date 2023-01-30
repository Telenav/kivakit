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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramMessageType;
import com.telenav.kivakit.core.messaging.MessageFormat;
import com.telenav.kivakit.core.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.messaging.Message.Status.FAILED;
import static com.telenav.kivakit.core.messaging.messages.Severity.HIGH;

/**
 * An alert with a proposed solution, intended to help IT staff resolve the issue.
 *
 * <p>
 * {@link OperationStatusMessage}s in order of importance:
 * </p>
 * <ul>
 *     <li>Critical Alert - An operation failed and needs <i>immediate attention</i> from a human operator</li>
 *     <li><b>Alert</b> - An operation failed and needs to be looked at by an operator soon</li>
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
 *
 *  @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class Alert extends Problem
{
    private String solution = "No solution provided";

    public Alert(String message, Object... arguments)
    {
        this(new Throwable(), message, arguments);
    }

    public Alert(Throwable cause, String message, Object... arguments)
    {
        super(cause, message, arguments);
    }

    public Alert()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String formatted(MessageFormat... format)
    {
        return super.formatted(format) + "\nProposed Solution: " + solution();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Severity severity()
    {
        return HIGH;
    }

    /**
     * Sets the proposed solution for this alert
     */
    public Alert solution(String solution)
    {
        this.solution = solution;
        return this;
    }

    /**
     * Gets the proposed solution for this alert
     */
    public String solution()
    {
        return solution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status status()
    {
        return FAILED;
    }
}
