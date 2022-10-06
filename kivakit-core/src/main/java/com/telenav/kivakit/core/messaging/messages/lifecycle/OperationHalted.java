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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramMessageType;
import com.telenav.kivakit.core.messaging.messages.OperationLifecycleMessage;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.messaging.Message.OperationStatus.HALTED;
import static com.telenav.kivakit.core.messaging.messages.Severity.HIGH;

/**
 * A problem with severity high enough to halt the current operation. If the problem is not severe enough,
 * {@link Problem} should be used instead.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
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

    /**
     * {@inheritDoc}
     */
    @Override
    public OperationStatus operationStatus()
    {
        return HALTED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Severity severity()
    {
        return HIGH;
    }
}
