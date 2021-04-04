////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging.loggers;

import com.telenav.kivakit.core.kernel.logging.Log;
import com.telenav.kivakit.core.kernel.logging.logs.text.ConsoleLog;
import com.telenav.kivakit.core.kernel.language.collections.set.Sets;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLogging;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.Set;

/**
 * A simple logger for bootstrapping purposes (the logging system itself may need to do logging, for example, which can
 * lead to problems)
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
public class ConsoleLogger extends BaseLogger
{
    private final Log log = new ConsoleLog();

    @Override
    @UmlExcludeMember
    protected Set<Log> logs()
    {
        return Sets.of(log);
    }
}
