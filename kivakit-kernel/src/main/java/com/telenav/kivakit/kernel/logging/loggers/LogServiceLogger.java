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

package com.telenav.kivakit.kernel.logging.loggers;

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.collections.set.Sets;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.kernel.logging.Log;
import com.telenav.kivakit.kernel.logging.LoggerCodeContext;
import com.telenav.kivakit.kernel.logging.logs.text.ConsoleLog;
import com.telenav.kivakit.kernel.messaging.messages.OperationMessage;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLogging;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A Java Services logger that creates loggers by inspecting the KIVAKIT_LOG and KIVAKIT_LOG_LEVEL environment
 * variables. Log providers specified by comma separated descriptors in the KIVAKIT_LOG variable are loaded by {@link
 * LogServiceLoader} as services. They are then configured and added to the list of logs. Configuration properties are
 * specified as key value pairs.
 * <pre>
 * -DKIVAKIT_LOG="console level=warning,file level=information location=log.txt"
 * </pre>
 * This command line sends warnings to the console and information messages (and higher) to the specified log file.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
@UmlRelation(label = "configures", referent = Log.class)
@UmlRelation(label = "loads log services with", referent = LogServiceLoader.class)
public class LogServiceLogger extends BaseLogger
{
    /** List of logs to log to, initially just a console log, unless logs are specified with KIVAKIT_LOG */
    @UmlAggregation(label = "logs to")
    private static Set<Log> logs = Sets.of(new ConsoleLog());

    private static boolean loaded;

    @UmlExcludeMember
    public LogServiceLogger()
    {
        this(new LoggerCodeContext());
    }

    @UmlExcludeMember
    public LogServiceLogger(final LoggerCodeContext context)
    {
        super(context);
    }

    @Override
    @UmlExcludeMember
    public synchronized Set<Log> logs()
    {
        // If we haven't loaded log services yet,
        if (!loaded)
        {
            // then we're loading them,
            loaded = true;

            // so get log service descriptors
            final var descriptors = JavaVirtualMachine.property("KIVAKIT_LOG");
            if (descriptors != null)
            {
                // and for each descriptor,
                final Set<Log> loadedLogs = new HashSet<>();
                for (final var descriptor : descriptors.split(","))
                {
                    // load and configure the log
                    final var log = log(descriptor);
                    if (log != null)
                    {
                        // and add it to the list
                        loadedLogs.add(log);
                    }
                    else
                    {
                        throw new IllegalStateException("KIVAKIT_LOG=" + descriptor + " is invalid. "
                                + "For details on this system property, see: https://tinyurl.com/zctf9bss");
                    }
                }

                // and finally replace the initial list containing only a console log with the logs
                // we loaded from the KIVAKIT_LOG environment variable
                logs = loadedLogs;
            }
        }

        return logs;
    }

    private static Log log(final String descriptor)
    {
        // If the descriptor matches "<log-name> <key>=<value> ..."
        final var descriptorMatcher = Pattern
                .compile("(?<log>\\w+)\\s*(?<arguments>.*)", Pattern.CASE_INSENSITIVE)
                .matcher(descriptor);

        if (descriptorMatcher.matches())
        {
            // get the log name
            final var logName = descriptorMatcher.group("log");

            // and the configuration, if any
            final Map<String, String> configuration = new HashMap<>();
            final var arguments = descriptorMatcher.group("arguments");
            if (!Strings.isEmpty(arguments))
            {
                final var propertyPattern = Pattern.compile("\\s*(?<key>[\\w+-]+)\\s*=\\s*(?<value>\\S+)\\s*",
                        Pattern.CASE_INSENSITIVE);
                final var properties = arguments.split(" ");
                for (final var property : properties)
                {
                    if (!Strings.isEmpty(property))
                    {
                        final var matcher = propertyPattern.matcher(property);
                        if (matcher.matches())
                        {
                            configuration.put(matcher.group("key"), matcher.group("value"));
                        }
                        else
                        {
                            Ensure.fail("Didn't understand property '" + property + "' of log '" + logName + "'");
                        }
                    }
                }
            }

            // load the log as a Java Service
            final var log = LogServiceLoader.log(logName);

            // then override if the individual log has a level=<message> identifier
            final var message = configuration.get("level");
            if (message != null)
            {
                log.level(OperationMessage.of(message).severity());
            }

            // and then let the log configure itself with the remaining properties
            log.configure(configuration);

            return log;
        }
        else
        {
            return null;
        }
    }
}
