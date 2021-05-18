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

package com.telenav.kivakit.kernel.messaging.messages;

import com.telenav.kivakit.kernel.messaging.messages.lifecycle.OperationFailed;
import com.telenav.kivakit.kernel.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.kernel.messaging.messages.lifecycle.OperationStarted;
import com.telenav.kivakit.kernel.messaging.messages.lifecycle.OperationSucceeded;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A message representing a state transition in the lifecycle of an operation. For example, an operation may have failed
 * or succeeded or someone may need to be alerted of a serious problem.
 *
 * @author jonathanl (shibo)
 * @see OperationStarted
 * @see OperationSucceeded
 * @see OperationFailed
 * @see OperationHalted
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public abstract class OperationLifecycleMessage extends OperationMessage
{
    protected OperationLifecycleMessage()
    {
    }

    protected OperationLifecycleMessage(final String message, final Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    @Override
    public final Status status()
    {
        return Status.NOT_APPLICABLE;
    }
}
