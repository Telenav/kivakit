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
import com.telenav.kivakit.core.internal.lexakai.DiagramMessageType;
import com.telenav.kivakit.core.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A message sent to indicate that an activity that might be monitored has occurred. For example, a resource (usually a
 * large resource) might have been allocated. This can be used to track, for example, memory consumption.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Activity extends OperationStatusMessage
{
    public Activity()
    {
    }

    public Activity(String message, Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    public Activity(Throwable cause, String message, Object... arguments)
    {
        super(message + (cause == null
                ? ""
                :  ": " + Message.escape(cause.getMessage())));
        cause(cause);
        arguments(arguments);
    }

    @Override
    public Severity severity()
    {
        return Severity.LOW;
    }

    @Override
    public Status status()
    {
        return Status.SUCCEEDED;
    }
}
