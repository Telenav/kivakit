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
import com.telenav.kivakit.core.logging.loggers.LogServiceLogger;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.factory.Factory;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Creates new {@link Logger} instances via {@link #newLogger()}. By default, instances of {@link LogServiceLogger} are
 * created, but a new factory can be installed with {@link #factory}.
 *
 * @author jonathanl (shibo)
 * @see Logger
 * @see LogServiceLogger
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramLogging.class)
@UmlRelation(label = "creates", referent = Logger.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class LoggerFactory
{
    /** The current logger factory */
    private static Factory<Logger> factory = LogServiceLogger::new;

    /** The global logger, for cases like collections where it's too cumbersome to require listeners */
    private static Logger global;

    /**
     * Returns a logger instance for use in contexts where it is too awkward to implement or pass in a {@link Listener}.
     * For example, some trivial classes and static methods may need to report problems, but are not important enough to
     * justify the complexity of reporting those problems an external listener.
     */
    public static Logger globalLogger()
    {
        if (global == null)
        {
            global = newLogger();
        }
        return global;
    }

    /**
     * Sets the global logger
     *
     * @param logger The logger
     */
    public static void globalLogger(Logger logger)
    {
        global = logger;
    }

    /**
     * Installs a custom logger factory. This overrides logger creation for the entire system
     *
     * @param factory The logger factory to use
     */
    public static void globalLoggerFactory(Factory<Logger> factory)
    {
        LoggerFactory.factory = factory;
    }

    /**
     * Returns a new logger instance, created using the current logger factory
     */
    public static Logger newLogger()
    {
        return factory.newInstance();
    }
}
