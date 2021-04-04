////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.reflection.Method;
import com.telenav.kivakit.core.kernel.language.threading.context.CallStack;
import com.telenav.kivakit.core.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLogging;

import static com.telenav.kivakit.core.kernel.language.threading.context.CallStack.Matching.EXACT;
import static com.telenav.kivakit.core.kernel.language.threading.context.CallStack.Matching.SUBCLASS;
import static com.telenav.kivakit.core.kernel.language.threading.context.CallStack.Proximity.IMMEDIATE;

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
