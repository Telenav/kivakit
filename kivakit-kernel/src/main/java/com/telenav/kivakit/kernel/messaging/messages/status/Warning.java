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

import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.kernel.messaging.messages.Severity;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A notice that something has gone wrong, but it is of low severity and has not resulted in any significant problem.
 * Typically, however, something may need to be looked at.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Warning extends OperationStatusMessage
{
    public Warning(final String message, final Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    public Warning(final Throwable cause, final String message, final Object... arguments)
    {
        super(message + ": " + Message.escape(cause.getMessage()));
        cause(cause);
        arguments(arguments);
    }

    public Warning()
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
        return Status.COMPLETED;
    }
}