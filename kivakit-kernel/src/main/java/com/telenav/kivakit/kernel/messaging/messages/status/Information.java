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

import com.telenav.kivakit.kernel.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.kernel.messaging.messages.Severity;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageType;

/**
 * Uncategorized, generic information about the progress of the current operation with no severity and which does not
 * indicate any problem.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Information extends OperationStatusMessage
{
    public Information(final String message, final Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    public Information()
    {
    }

    @Override
    public Severity severity()
    {
        return Severity.NONE;
    }

    @Override
    public Status status()
    {
        return Status.SUCCEEDED;
    }
}