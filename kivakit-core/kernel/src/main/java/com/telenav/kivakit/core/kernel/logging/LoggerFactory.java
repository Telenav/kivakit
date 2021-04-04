////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging;

import com.telenav.kivakit.core.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.core.kernel.logging.loggers.LogServiceLogger;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLogging;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

/**
 * Creates new {@link Logger} instances via {@link #newLogger()}. By default, instances of {@link LogServiceLogger} are
 * created, but a new factory can be installed with {@link #factory}.
 *
 * @author jonathanl (shibo)
 * @see Logger
 * @see LogServiceLogger
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
@UmlRelation(label = "creates", referent = Logger.class)
public class LoggerFactory
{
    private static Factory<Logger> factory = LogServiceLogger::new;

    /**
     * Installs a custom logger factory
     *
     * @param factory The logger factory to use
     */
    public static void factory(final Factory<Logger> factory)
    {
        LoggerFactory.factory = factory;
    }

    /**
     * @return A new logger instance
     */
    public static Logger newLogger()
    {
        return factory.newInstance();
    }
}
