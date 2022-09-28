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

package com.telenav.kivakit.core.messaging.messages;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationFailed;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationStarted;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationSucceeded;
import com.telenav.kivakit.core.internal.lexakai.DiagramMessageType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

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
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public abstract class OperationLifecycleMessage extends OperationMessage
{
    protected OperationLifecycleMessage()
    {
    }

    protected OperationLifecycleMessage(String message, Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Message.Status status()
    {
        return Message.Status.NOT_APPLICABLE;
    }
}
