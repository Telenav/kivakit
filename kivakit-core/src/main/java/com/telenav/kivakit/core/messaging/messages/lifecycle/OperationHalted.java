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

package com.telenav.kivakit.core.messaging.messages.lifecycle;

import com.telenav.kivakit.core.messaging.messages.OperationLifecycleMessage;
import com.telenav.kivakit.core.lexakai.DiagramMessageType;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A problem with severity high enough to halt the current operation. If the problem is not severe enough, {@link
 * Problem} should be used instead.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class OperationHalted extends OperationLifecycleMessage
{
    public OperationHalted()
    {
    }

    public OperationHalted(String message, Object... arguments)
    {
        super(message);
        cause(new Throwable("OperationHalted error"));
        arguments(arguments);
    }

    public OperationHalted(Throwable cause, String message, Object... arguments)
    {
        super(message);
        cause(cause);
        arguments(arguments);
    }

    @Override
    public OperationStatus operationStatus()
    {
        return OperationStatus.HALTED;
    }

    @Override
    public Severity severity()
    {
        return Severity.HIGH;
    }
}
