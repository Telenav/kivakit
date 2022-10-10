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

package com.telenav.kivakit.core.logging;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogging;
import com.telenav.kivakit.core.messaging.context.CallStack;
import com.telenav.kivakit.core.messaging.context.CodeContext;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.reflect.Method;
import java.util.Objects;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.context.CallStack.Matching.EXACT;
import static com.telenav.kivakit.core.messaging.context.CallStack.Matching.SUBCLASS;
import static com.telenav.kivakit.core.messaging.context.CallStack.Proximity.IMMEDIATE;

/**
 * Information about the origin of a {@link LogEntry}, including the host and class.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class LoggerCodeContext extends CodeContext
{
    public LoggerCodeContext()
    {
        // The logger code context is the immediate caller of any subclass of logger,
        // ignoring any intervening LoggerFactory calls
        super(Objects.requireNonNull(
                CallStack.callerOf(IMMEDIATE, SUBCLASS, Logger.class, EXACT, LoggerFactory.class)).parentType().type());
    }

    public LoggerCodeContext(Method callerOf)
    {
        super(callerOf);
    }

    public LoggerCodeContext(String locationName)
    {
        super(locationName);
    }
}
