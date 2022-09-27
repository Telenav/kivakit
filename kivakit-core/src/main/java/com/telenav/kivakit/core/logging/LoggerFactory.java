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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogging;
import com.telenav.kivakit.core.logging.loggers.LogServiceLogger;
import com.telenav.kivakit.interfaces.factory.Factory;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

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
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class LoggerFactory
{
    /** The current logger factory */
    private static Factory<Logger> factory = LogServiceLogger::new;

    /**
     * Installs a custom logger factory. This overrides logger creation for the entire system
     *
     * @param factory The logger factory to use
     */
    public static void factory(Factory<Logger> factory)
    {
        LoggerFactory.factory = factory;
    }

    /**
     * @return A new logger instance, created using the current logger factory
     */
    public static Logger newLogger()
    {
        return factory.newInstance();
    }
}
