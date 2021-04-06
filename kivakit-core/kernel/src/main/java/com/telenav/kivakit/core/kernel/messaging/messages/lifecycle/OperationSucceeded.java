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

package com.telenav.kivakit.core.kernel.messaging.messages.lifecycle;

import com.telenav.kivakit.core.kernel.messaging.messages.OperationLifecycleMessage;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageType;

/**
 * A message representing the success of an operation
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class OperationSucceeded extends OperationLifecycleMessage
{
    public static final OperationSucceeded INSTANCE = new OperationSucceeded();

    public OperationSucceeded()
    {
        super("OperationSucceeded");
    }

    protected OperationSucceeded(final String message, final Object... arguments)
    {
        super(message, arguments);
    }

    @Override
    public OperationStatus operationStatus()
    {
        return OperationStatus.SUCCEEDED;
    }
}
