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

import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.language.Objects;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.logging.logs.text.LogFormatter;
import com.telenav.kivakit.core.logging.logs.text.formatters.NarrowLogFormatter;
import com.telenav.kivakit.core.logging.logs.text.formatters.WideLogFormatter;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.context.CodeContext;
import com.telenav.kivakit.core.messaging.context.StackTrace;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.kivakit.core.messaging.messages.Triaged;
import com.telenav.kivakit.core.lexakai.DiagramLogging;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.time.Time;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A log entry object containing a message and meta-information about the origin of the message and its nature.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
public class LogEntry implements Triaged
{
    private static final AtomicInteger nextSequenceNumber = new AtomicInteger(1);

    private int sequenceNumber;

    private String threadName;

    @UmlAggregation
    private CodeContext context;

    private String messageType;

    private String formattedMessage;

    @UmlAggregation
    private StackTrace stackTrace;

    private Severity severity;

    private Time created;

    private transient Message message;

    private transient LogFormatter lastFormatter;

    private transient String formattedEntry;

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
        created = Time.now();
        messageType = Classes.simpleName(message.getClass());
        stackTrace = message.stackTrace();
        severity = message.severity();
        sequenceNumber = nextSequenceNumber.getAndIncrement();
    }

    @UmlExcludeMember
    protected LogEntry()
    {
    }

    @KivaKitIncludeProperty
    public CodeContext context()
    {
        return context;
    }

    public Time created()
    {
        return created;
    }

    public String format(LogFormatter formatter, Formatter.Format format)
    {
        assert context != null;
        if (formattedEntry != null && Objects.equal(formatter, lastFormatter))
        {
            return formattedEntry;
        }
        if (formatter != null)
        {
            formattedEntry = formatter.format(this, format);
            lastFormatter = formatter;
            return formattedEntry;
        }
        return NarrowLogFormatter.INSTANCE.format(this, format);
    }

    /**
     * @return The formatted message
     */
    public String formattedMessage()
    {
        return formattedMessage(Formatter.Format.WITH_EXCEPTION);
    }

    /**
     *
     */
    public String formattedMessage(Formatter.Format format)
    {
        if (formattedMessage == null)
        {
            formattedMessage = message.formatted(format);
        }
        return formattedMessage;
    }

    public boolean isSevere()
    {
        return severity().isGreaterThanOrEqualTo(Severity.MEDIUM);
    }

    public Message message()
    {
        return message;
    }

    public String messageType()
    {
        return messageType;
    }

    public int sequenceNumber()
    {
        return sequenceNumber;
    }

    @Override
    @UmlExcludeMember
    public Severity severity()
    {
        return severity;
    }

    public StackTrace stackTrace()
    {
        return stackTrace;
    }

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
