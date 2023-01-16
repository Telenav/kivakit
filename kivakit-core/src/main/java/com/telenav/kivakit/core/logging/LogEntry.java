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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogging;
import com.telenav.kivakit.core.language.reflection.property.IncludeProperty;
import com.telenav.kivakit.core.logging.logs.text.LogFormatter;
import com.telenav.kivakit.core.logging.logs.text.formatters.NarrowLogFormatter;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.MessageFormat;
import com.telenav.kivakit.core.messaging.context.CodeContext;
import com.telenav.kivakit.core.messaging.context.StackTrace;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.kivakit.core.messaging.messages.Triaged;
import com.telenav.kivakit.core.time.Time;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.concurrent.atomic.AtomicInteger;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.language.Classes.simpleName;
import static com.telenav.kivakit.core.language.Objects.isEqual;
import static com.telenav.kivakit.core.messaging.messages.Severity.MEDIUM;
import static com.telenav.kivakit.core.time.Time.now;

/**
 * A log entry object containing a message and meta-information about the origin of the message and its nature.
 *
 * <p><b>Formatting</b></p>
 *
 * <ul>
 *     <li>{@link #format(LogFormatter, MessageFormat...)}</li>
 *     <li>{@link #formattedMessage(MessageFormat...)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #context()}</li>
 *     <li>{@link #created()}</li>
 *     <li>{@link #formattedMessage(MessageFormat...)}</li>
 *     <li>{@link #isSevere()}</li>
 *     <li>{@link #message()}</li>
 *     <li>{@link #messageType()}</li>
 *     <li>{@link #sequenceNumber}</li>
 *     <li>{@link #severity()}</li>
 *     <li>{@link #stackTrace()}</li>
 *     <li>{@link #threadName()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramLogging.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class LogEntry implements Triaged
{
    /**
     * The next log entry sequence number
     */
    private static final AtomicInteger nextSequenceNumber = new AtomicInteger(1);

    /** The sequence number for this entry */
    private int sequenceNumber;

    /** The name of the thread that created this log entry */
    private String threadName;

    /** The code context where this entry was created */
    @UmlAggregation
    private CodeContext context;

    /** The type of message */
    private String messageType;

    /** The formatted message */
    private String formattedMessage;

    /** Any stack trace */
    @UmlAggregation
    private StackTrace stackTrace;

    /** The severity of this log entry */
    private Severity severity;

    /** The time at which this entry was created */
    private Time created;

    /** The message to format for this log entry */
    private transient Message message;

    /** The last log formatter used */
    private transient LogFormatter lastFormatter;

    /** The formatted log entry */
    private transient String formattedEntry;

    /**
     * Construct a log entry for the given logger
     *
     * @param logger The logger
     * @param context The code context
     * @param thread The thread
     * @param message The message to log
     */
    @SuppressWarnings("unused")
    @UmlExcludeMember
    public LogEntry(Logger logger, LoggerCodeContext context, Thread thread, Message message)
    {
        this.context = message.context();
        if (this.context == null)
        {
            this.context = context;
        }
        assert context != null;
        threadName = thread.getName();
        this.message = message;
        created = now();
        messageType = simpleName(message.getClass());
        stackTrace = message.stackTrace();
        severity = message.severity();
        sequenceNumber = nextSequenceNumber.getAndIncrement();
    }

    @UmlExcludeMember
    protected LogEntry()
    {
    }

    /**
     * Returns the code context for this log entry
     */
    @IncludeProperty
    public CodeContext context()
    {
        return context;
    }

    /**
     * Returns the time this log entry was created
     */
    public Time created()
    {
        return created;
    }

    /**
     * Formats this log entry with the given log formatter, configured with the given message format instructions
     *
     * @param formatter The formatter to use
     * @param formats The formatting requirements
     * @return The formatted log entry
     */
    public String format(LogFormatter formatter, MessageFormat... formats)
    {
        ensureNotNull(context);

        // If the formatted entry exists and we're re-formatting with the same formatter,
        if (formattedEntry != null && isEqual(formatter, lastFormatter))
        {
            // then reuse the formatted text we made last time
            return formattedEntry;
        }

        // If there is a formatter set,
        if (formatter != null)
        {
            // then format the entry
            formattedEntry = formatter.format(this, formats);

            // and save the formatter
            lastFormatter = formatter;

            return formattedEntry;
        }

        // Use the narrow log formatter if no formatter is set
        return new NarrowLogFormatter().format(this, formats);
    }

    /**
     * Formats this log entry using the given format(s)
     */
    public String formattedMessage(MessageFormat... formats)
    {
        if (formattedMessage == null)
        {
            formattedMessage = message.formatted(formats);
        }
        return formattedMessage;
    }

    /**
     * Returns true if this log entry is for a severe message type
     */
    public boolean isSevere()
    {
        return severity().isGreaterThanOrEqualTo(MEDIUM);
    }

    /**
     * Returns the message for this log entry
     */
    public Message message()
    {
        return message;
    }

    /**
     * Returns the type of message
     */
    public String messageType()
    {
        return messageType;
    }

    /**
     * Returns the sequence number of this log entry
     */
    public int sequenceNumber()
    {
        return sequenceNumber;
    }

    /**
     * Returns the severity of this log entry
     */
    @Override
    @UmlExcludeMember
    public Severity severity()
    {
        return severity;
    }

    /**
     * Returns any stack trace associated with this log entry
     */
    public StackTrace stackTrace()
    {
        return stackTrace;
    }

    /**
     * Returns the name of the thread that created this log entry
     */
    public String threadName()
    {
        return threadName;
    }

    @Override
    public String toString()
    {
        return "[LogEntry message = " + message() + "]";
    }
}
