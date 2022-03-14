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

package com.telenav.kivakit.core.messaging.messages.status.activity;

import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.lexakai.DiagramMessageType;
import com.telenav.kivakit.core.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * The current step failed to produce any useful result
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class StepFailure extends OperationStatusMessage
{
    public static final StepFailure INSTANCE = new StepFailure();

    public StepFailure(String message, Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    public StepFailure(Throwable cause, String message, Object... arguments)
    {
        super(message + ": " + Message.escape(cause.getMessage()));
        cause(cause);
        arguments(arguments);
    }

    public StepFailure()
    {
    }

    @Override
    public Severity severity()
    {
        return Severity.MEDIUM_HIGH;
    }

    @Override
    public final Status status()
    {
        return Status.FAILED;
    }
}
