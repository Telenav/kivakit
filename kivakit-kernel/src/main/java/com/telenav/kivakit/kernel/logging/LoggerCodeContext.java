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

package com.telenav.kivakit.kernel.logging;

import com.telenav.kivakit.kernel.language.reflection.Method;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLogging;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.kernel.language.threading.context.CallStack;
import com.telenav.kivakit.kernel.language.threading.context.CodeContext;

import static com.telenav.kivakit.kernel.language.threading.context.CallStack.Matching.EXACT;
import static com.telenav.kivakit.kernel.language.threading.context.CallStack.Matching.SUBCLASS;
import static com.telenav.kivakit.kernel.language.threading.context.CallStack.Proximity.IMMEDIATE;

/**
 * Information about the origin of a {@link LogEntry}, including the host and class.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
public class LoggerCodeContext extends CodeContext
{
    public LoggerCodeContext()
    {
        // The logger code context is the immediate caller of any subclass of logger, ignoring any intervening LoggerFactory calls
        super(CallStack.callerOf(IMMEDIATE, SUBCLASS, Logger.class, EXACT, LoggerFactory.class));
    }

    public LoggerCodeContext(final Method callerOf)
    {
        super(callerOf);
    }

    public LoggerCodeContext(final String locationName)
    {
        super(locationName);
    }
}
