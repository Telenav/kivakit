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

package com.telenav.kivakit.core.kernel.messaging.messages.status;

import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.core.kernel.messaging.messages.Severity;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A problem with low severity that may cause a less-than-optimal result but will not cause the operation to halt or
 * data to be discarded.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Quibble extends OperationStatusMessage
{
    public Quibble(final String message, final Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    public Quibble(final Throwable cause, final String message, final Object... arguments)
    {
        super(message + ": " + Message.escape(cause.getMessage()));
        cause(cause);
        arguments(arguments);
    }

    public Quibble()
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
