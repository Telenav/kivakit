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
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogging;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogs;
import com.telenav.kivakit.core.logging.logs.text.ConsoleLog;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.interfaces.comparison.Filtered;
import com.telenav.kivakit.interfaces.io.Closeable;
import com.telenav.kivakit.interfaces.io.Flushable;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Messages.messageForType;
import static com.telenav.kivakit.core.time.Duration.seconds;

/**
 * Accepts log entries for some purpose, such as writing them to a file or displaying them in a terminal window.
 * {@link Logger} implementations write log entries to one or more {@link Log}s via {@link #log(LogEntry)}. Which log
 * entries are logged can be controlled with filters returned from {@link Filtered#filters()} and by setting a minimum
 * severity level with {@link #level(Severity)}. Some logs are configurable with a string-to-string property map via
 * {@link #configure(VariableMap)}. Logs are also {@link Closeable}, {@link Flushable} and {@link Named}.
 * <p>
 * <b>Wiki Documentation</b>
 * <p>
 * <i>For a detailed discussion of KivaKit logging, see the <a href="https://tinyurl.com/mhc3ss5s">KivaKit
 * logging documentation</a> on the wiki.</i>
 * <p>
 * <b>Implementations</b>
 * <p>
 * There are a variety of {@link Log} implementations, including:
 * <ul>
 *     <li>{@link ConsoleLog} - Logs to {@link System#out} and {@link System#err}</li>
 *     <li>EmailLog - Sends (typically high severity) log entries as emails</li>
 *     <li>ServerLog - Makes log entries available to a client</li>
 *     <li>FileLog - Logs entries to one or more files</li>
 * </ul>
 * Which log implementation(s) are used in an application can be selected from the command line with the system
 * property KIVAKIT_LOG (see <a href="https://tinyurl.com/mhc3ss5s">KivaKit logging documentation</a> for details).
 *
 * <p><b>Configuration</b></p>
 *
 * <ul>
 *     <li>{@link #configure(VariableMap)} - Configures this log with the values from the KIVAKIT_LOG environment variable</li>
 * </ul>
 *
 * <p><b>Logging</b></p>
 *
 * <ul>
 *     <li>{@link #level(Severity)} - Sets the minimum severity level to log</li>
 *     <li>{@link #log(LogEntry)}</li>
 *     <li>{@link #maximumFlushTime()}</li>
 * </ul>
 *
 * <p><b>Logging</b></p>
 *
 * <p>
 * More details about logging are available in <a
 * href="../../../../../../../../../kivakit-core/documentation/logging.md">kivakit-core</a>.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Logger
 * @see Filtered
 * @see Named
 * @see Flushable
 * @see Closeable
 * @see <a href="https://tinyurl.com/mhc3ss5s">KivaKit logging documentation</a>
 */
@UmlClassDiagram(diagram = DiagramLogs.class)
@UmlClassDiagram(diagram = DiagramLogging.class)
@UmlRelation(label = "logs", referent = LogEntry.class)
@UmlExcludeSuperTypes({ Named.class, Flushable.class })
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public interface Log extends
        Named,
        Filtered<LogEntry>,
        Closeable,
        Flushable<Duration>
{
    /**
     * Configures the log
     *
     * @param properties A property map specific to the type of log
     */
    void configure(VariableMap<String> properties);

    /**
     * Sets the "log level"
     *
     * @param severity The minimum severity of entries to log
     */
    void level(Severity severity);

    /**
     * Sets the minimum logging level to the severity for the given message type
     */
    default <T extends Message> void level(Class<T> message)
    {
        level(messageForType(message).severity());
    }

    /**
     * Logs the given entry
     */
    void log(LogEntry entry);

    /**
     * {@inheritDoc}
     */
    @Override
    default Duration maximumFlushTime()
    {
        return seconds(30);
    }
}
