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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramMessageType;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.messaging.Message.Status.RESULT_INCOMPLETE;
import static com.telenav.kivakit.core.messaging.messages.Severity.MEDIUM;

/**
 * The current step had to discard some result to continue
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class StepIncomplete extends Warning
{
    public StepIncomplete(String message, Object... arguments)
    {
        this(new Throwable(), message, arguments);
    }

    public StepIncomplete(Throwable cause, String message, Object... arguments)
    {
        super(cause, message, arguments);
    }

    public StepIncomplete()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Severity severity()
    {
        return MEDIUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Status status()
    {
        return RESULT_INCOMPLETE;
    }
}
