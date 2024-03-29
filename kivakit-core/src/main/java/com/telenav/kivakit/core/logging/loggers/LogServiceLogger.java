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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogging;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.core.logging.LoggerCodeContext;
import com.telenav.kivakit.core.logging.logs.text.ConsoleLog;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.Set;
import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.Sets.hashSet;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.logging.loggers.LogServiceLoader.logForName;
import static com.telenav.kivakit.core.messaging.Listener.consoleListener;
import static com.telenav.kivakit.core.messaging.Messages.parseMessageType;
import static com.telenav.kivakit.core.os.Console.console;
import static com.telenav.kivakit.core.string.Strings.isNullOrBlank;
import static com.telenav.kivakit.core.vm.Properties.systemPropertyOrEnvironmentVariable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * A Java Services logger that creates loggers by inspecting the KIVAKIT_LOG and KIVAKIT_LOG_LEVEL environment
 * variables. Log providers specified by comma separated descriptors in the KIVAKIT_LOG variable are loaded by
 * {@link LogServiceLoader} as services. They are then configured and added to the list of logs. Configuration
 * properties are specified as key value pairs.
 *
 * <pre>
 * -DKIVAKIT_LOG="console level=warning,file level=information file=log.txt"</pre>
 *
 * <p>
 * This command line sends warnings to the console and information messages (and higher) to the specified log file.
 * </p>
 *
 * <p><b>Logging</b></p>
 *
 * <p>
 * More details about logging are available in <a
 * href="../../../../../../../../../kivakit-core/documentation/logging.md">kivakit-core</a>.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
@UmlRelation(label = "configures", referent = Log.class)
@UmlRelation(label = "loads log services with", referent = LogServiceLoader.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class LogServiceLogger extends BaseLogger
{
    /** List of logs to log to, initially just a console log, unless logs are specified with KIVAKIT_LOG */
    @UmlAggregation(label = "logs to")
    private static Set<Log> logs = hashSet(new ConsoleLog());

    /** True if loggers have been dynamically loaded */
    private static boolean loaded;

    @UmlExcludeMember
    public LogServiceLogger()
    {
        this(new LoggerCodeContext());
    }

    @UmlExcludeMember
    public LogServiceLogger(LoggerCodeContext context)
    {
        super(context);
    }

    /**
     * Returns the set of loggers described by the KIVAKIT_LOG property (either a system property or an environment
     * variable). If KIVAKIT_LOG has not been set, logging will go to a {@link ConsoleLog}.
     */
    @Override
    @UmlExcludeMember
    public synchronized ObjectSet<Log> logs()
    {
        // If we haven't loaded log services yet,
        if (!loaded)
        {
            // then we're loading them,
            loaded = true;

            // so get log service descriptors
            var descriptors = systemPropertyOrEnvironmentVariable("KIVAKIT_LOG");
            if (descriptors != null)
            {
                // and for each descriptor,
                ObjectSet<Log> loadedLogs = new ObjectSet<>();
                for (var descriptor : descriptors.split(","))
                {
                    // load and configure the log
                    var log = log(descriptor);
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

        return set(logs);
    }

    /**
     * Loads and configures a log based on a descriptor, as described above
     */
    private static Log log(String descriptor)
    {
        // If the descriptor matches "<log-name> <key>=<value> ..."
        var descriptorMatcher = Pattern
                .compile("(?<log>\\w+)(?<arguments>.*)", CASE_INSENSITIVE)
                .matcher(descriptor);

        if (descriptorMatcher.matches())
        {
            // get the log name
            var logName = descriptorMatcher.group("log");

            // and the configuration, if any
            var configuration = new VariableMap<String>();
            var arguments = descriptorMatcher.group("arguments");
            if (!isNullOrBlank(arguments))
            {
                var propertyPattern = Pattern.compile("\\s*(?<key>[\\w+-]+)\\s*=\\s*(?<value>\\S+)\\s*",
                        CASE_INSENSITIVE);
                var properties = arguments.trim().split(" ");
                for (var property : properties)
                {
                    if (!isNullOrBlank(property))
                    {
                        var matcher = propertyPattern.matcher(property);
                        if (matcher.matches())
                        {
                            configuration.put(matcher.group("key"), matcher.group("value"));
                        }
                        else
                        {
                            fail("Didn't understand property '$' of log '$", property, logName);
                        }
                    }
                }
            }

            // load the log as a Java Service
            var log = logForName(console(), logName);
            if (log != null)
            {
                // then override if the individual log has a level=<message> identifier
                var message = configuration.asString("level");
                if (message != null)
                {
                    log.level(parseMessageType(consoleListener(), message).severity());
                }

                // and then let the log configure itself with the remaining properties
                log.configure(configuration);

                return log;
            }
            else
            {
                fail("Could not find log named: $", logName);
            }
        }
        else
        {
            fail("Descriptor does not match \"<log-name> <key>=<value> ...\": $", descriptor);
        }

        return null;
    }
}
