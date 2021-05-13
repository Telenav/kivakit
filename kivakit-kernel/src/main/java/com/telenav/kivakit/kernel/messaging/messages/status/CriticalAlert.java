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
import com.telenav.kivakit.kernel.messaging.messages.Severity;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageType;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A high priority {@link Alert} that needs immediate attention. A proposed solution for IT staff can be added with
 * {@link #solution(String)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
@LexakaiJavadoc(complete = true)
public class CriticalAlert extends Alert
{
    public CriticalAlert(final String solution, final String message, final Object... arguments)
    {
        super(solution, message, arguments);
    }

    public CriticalAlert(final String solution, final Throwable cause, final String message, final Object... arguments)
    {
        super(solution, message + ": " + Message.escape(cause.getMessage()), arguments);
        cause(cause);
        arguments(arguments);
    }

    public CriticalAlert()
    {
    }

    @Override
    public Severity severity()
    {
        return Severity.CRITICAL;
    }

    @Override
    public CriticalAlert solution(final String solution)
    {
        super.solution(solution);
        return this;
    }
}
