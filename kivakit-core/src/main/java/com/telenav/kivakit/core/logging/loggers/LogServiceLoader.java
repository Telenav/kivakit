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

package com.telenav.kivakit.core.logging.loggers;

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.logs.text.ConsoleLog;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogging;
import com.telenav.kivakit.core.string.AsciiArt;
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

    public static Log log(String name)
    {
        for (var log : logs())
        {
            if (log.name().equalsIgnoreCase(name))
            {
                return log;
            }
        }
        Ensure.fail("No log called '" + name + "'");
        return null;
    }

    public static synchronized List<Log> logs()
    {
        if (logs == null)
        {
            logs = new ArrayList<>();
            logs.add(new ConsoleLog());
            for (var service : ServiceLoader.load(Log.class))
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
