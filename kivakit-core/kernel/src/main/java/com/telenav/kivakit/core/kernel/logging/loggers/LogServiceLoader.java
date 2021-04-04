////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging.loggers;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.core.kernel.logging.Log;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.logs.text.ConsoleLog;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLogging;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Loads logs that implement the service provider interface (SPI) {@link Log}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
public class LogServiceLoader
{
    private static final Logger LOGGER = new ConsoleLogger();

    @UmlAggregation(label = "loads")
    private static List<Log> logs;

    public static Log log(final String name)
    {
        for (final var log : logs())
        {
            if (log.name().equalsIgnoreCase(name))
            {
                return log;
            }
        }
        return Ensure.fail("No log called '" + name + "'");
    }

    public static synchronized List<Log> logs()
    {
        if (logs == null)
        {
            logs = new ArrayList<>();
            logs.add(new ConsoleLog());
            for (final var service : ServiceLoader.load(Log.class))
            {
                LOGGER.announce("Log '${class}' is available", service.getClass());
                logs.add(service);
            }
            if (logs.isEmpty())
            {
                LOGGER.announce(AsciiArt.box("Logging can be configured with KIVAKIT_LOG and KIVAKIT_LOG_LEVEL environment variables.\n"
                        + "See https://tinyurl.com/mhc3ss5s for details."));
            }
        }
        return logs;
    }
}
