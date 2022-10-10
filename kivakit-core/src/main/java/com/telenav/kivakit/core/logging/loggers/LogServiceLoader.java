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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogging;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.core.logging.logs.text.ConsoleLog;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.string.AsciiArt.textBox;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Loads logs that implement the service provider interface (SPI) {@link Log}.
 * </p>
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #logForName(Listener, String)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see LogServiceLogger
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
@CodeQuality(audience = AUDIENCE_INTERNAL,
             stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
class LogServiceLoader
{
    @UmlAggregation(label = "loads")
    private static List<Log> availableLogs;

    /**
     * Returns the log with the given name, or null if there is no log with the given name
     */
    public static Log logForName(Listener listener, String name)
    {
        for (var log : availableLogs(listener))
        {
            if (log.name().equalsIgnoreCase(name))
            {
                return log;
            }
        }
        return null;
    }

    private static synchronized List<Log> availableLogs(Listener listener)
    {
        if (availableLogs == null)
        {
            availableLogs = new ArrayList<>();
            availableLogs.add(new ConsoleLog());
            for (var service : ServiceLoader.load(Log.class))
            {
                listener.announce("Log '${class}' is available", service.getClass());
                availableLogs.add(service);
            }
            if (availableLogs.isEmpty())
            {
                listener.announce(textBox("Logging can be configured with KIVAKIT_LOG and KIVAKIT_LOG_LEVEL environment variables.\n"
                        + "See https://tinyurl.com/mhc3ss5s for details."));
            }
        }
        return availableLogs;
    }
}
