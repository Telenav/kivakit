////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.messaging.messages.status;

import com.telenav.kivakit.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.kernel.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.kernel.messaging.messages.Severity;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageType;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An alert with a proposed solution, intended to help IT staff resolve the issue.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
@LexakaiJavadoc(complete = true)
public class Alert extends OperationStatusMessage
{
    private String solution = "No solution provided";

    public Alert(final String message, final Object... arguments)
    {
        super(message);
        cause(new Throwable());
        arguments(arguments);
    }

    public Alert()
    {
    }

    @Override
    public String formatted(final MessageFormatter.Format format)
    {
        return super.formatted(format) + "\nProposed Solution: " + solution();
    }

    @Override
    public Severity severity()
    {
        return Severity.HIGH;
    }

    public Alert solution(final String solution)
    {
        this.solution = solution;
        return this;
    }

    public String solution()
    {
        return solution;
    }

    @Override
    public Status status()
    {
        return Status.FAILED;
    }
}
