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

import com.telenav.kivakit.core.internal.lexakai.DiagramListenerType;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogging;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Filtered;
import com.telenav.kivakit.interfaces.io.Flushable;
import com.telenav.kivakit.interfaces.messaging.Transmittable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

/**
 * A {@link Logger} accepts {@link Message}s via {@link #log(Message)} and writes them to one or more {@link Log}s.
 * Filters can be added to the logger to restrict which messages are logged with {@link #addFilter(Filter)}.
 * <p>
 * <b>Markdown Documentation</b>
 * <p>
 * <i>For a detailed discussion, see the <a href="https://tinyurl.com/mhc3ss5s">KivaKit Logging Documentation</a></i>
 * <p>
 * <b>Messaging</b>
 * <p>
 * Loggers listen for messages via {@link Listener#onReceive(Transmittable)} and forward them to {@link #log(Message)}.
 * This means that {@link Logger}s can be used anywhere a {@link Listener} is required. For example when using a
 * converter, the converter can broadcast warning or problem messages during a conversion and a {@link Logger} can
 * listen to and log those messages.
 * <p>
 * <b>Example</b>
 * <pre>
 * var converter = new IntegerConverter(this);
 * var agent = converter.convert("99");
 * </pre>
 * Another typical use case is for a logger to listen to an object that implements {@link Repeater}.
 * <p>
 * <b>Example</b>
 * <pre>
 * class ReportProcessor extends BaseRepeater&lt;Message&gt; { [...] }
 *
 *     [...]
 *
 * var reporter = listenTo(new ReportProcessor());
 * </pre>
 * Logs are loaded dynamically through using the service provider interface (SPI) {@link Log}. Which log
 * implementation(s) are loaded for use in an application can be chosen by the end-user from the command line with the
 * system property KIVAKIT_LOG.
 *
 * <p><b>Factory Methods</b></p>
 *
 * <p><b>Logging</b></p>
 *
 * <ul>
 *     <li>{@link #codeContext()} - The code context for this logger</li>
 *     <li>{@link #log(Message)} - Logs the given message</li>
 *     <li>{@link #startTime()} - The time of creation for this logger</li>
 * </ul>
 *
 * <p><b>Filtering</b></p>
 *
 * <ul>
 *     <li>{@link #addFilter(Filter)} - Adds the given filter to this log</li>
 *     <li>{@link #filters()} - Returns the list of filters for this log</li>
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
 * @see <a href="https://tinyurl.com/mhc3ss5s">KivaKit Logging Documentation</a>
 * @see LogEntry
 * @see Listener
 * @see Filtered
 * @see Filter
 * @see Flushable
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramLogging.class)
@UmlClassDiagram(diagram = DiagramListenerType.class)
@UmlExcludeSuperTypes({ Flushable.class })
public interface Logger extends
        Listener,
        Filtered<LogEntry>,
        Flushable<Duration>
{
    /**
     * Set a new filter
     *
     * @param filter The filter to set
     */
    void addFilter(Filter<LogEntry> filter);

    /**
     * <b>Not public API</b>
     *
     * @return The code context from which this logger is logging
     */
    @UmlRelation(diagram = DiagramLogging.class, label = "uses")
    LoggerCodeContext codeContext();

    /**
     * Logs the given message
     *
     * @param message The message to log
     */
    void log(Message message);

    /**
     * <b>Not public API</b>
     * <p>
     * This method is only for integrating other logging frameworks which have their own idea of logging context, like
     * log4j or Java logging.
     *
     * @param context The logging context
     * @param message The message
     */
    @UmlExcludeMember
    void log(LoggerCodeContext context, Thread thread, Message message);

    /**
     * Logs any received messages
     */
    @Override
    @UmlExcludeMember
    default void onMessage(Message message)
    {
        log(message);
    }

    /**
     * Returns the time this logger was constructed
     */
    @UmlExcludeMember
    Time startTime();
}
