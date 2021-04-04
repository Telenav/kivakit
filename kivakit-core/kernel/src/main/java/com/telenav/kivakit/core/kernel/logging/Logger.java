////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Filtered;
import com.telenav.kivakit.core.kernel.interfaces.io.Flushable;
import com.telenav.kivakit.core.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.Repeater;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLogging;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageListenerType;
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
 * private static final Logger LOGGER = LoggerFactory.newLogger();
 *
 *    [...]
 *
 * var converter = new IntegerConverter(LOGGER);
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
 * var reporter = LOGGER.listenTo(new ReportProcessor());
 * </pre>
 * Logs are loaded dynamically through using the service provider interface (SPI) {@link Log}. Which log
 * implementation(s) are loaded for use in an application can be chosen by the end-user from the command line with the
 * system property KIVAKIT_LOG.
 *
 * @author jonathanl (shibo)
 * @see <a href="https://tinyurl.com/mhc3ss5s">KivaKit Logging Documentation</a>
 * @see LogEntry
 * @see Listener
 * @see Filtered
 * @see Filter
 * @see Flushable
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
@UmlClassDiagram(diagram = DiagramMessageListenerType.class)
@UmlExcludeSuperTypes({ Flushable.class })
public interface Logger extends Listener, Filtered<LogEntry>, Flushable
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
    default void onMessage(final Message message)
    {
        log(message);
    }

    /**
     * @return The time this logger was constructed
     */
    @UmlExcludeMember
    Time startTime();
}
