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

package com.telenav.kivakit.kernel.logging;

import com.telenav.kivakit.kernel.language.objects.Objects;
import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.kernel.language.threading.context.StackTrace;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.logging.logs.text.LogEntryFormatter;
import com.telenav.kivakit.kernel.logging.logs.text.formatters.ColumnarFormatter;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.kernel.messaging.messages.Severity;
import com.telenav.kivakit.kernel.messaging.messages.Triaged;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLogging;
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

    @KivaKitIncludeProperty
    private int sequenceNumber;

    @KivaKitIncludeProperty
    private String threadName;

    @KivaKitIncludeProperty
    @UmlAggregation
    private CodeContext context;

    @KivaKitIncludeProperty
    private String messageType;

    @KivaKitIncludeProperty
    private String formattedMessage;

    @UmlAggregation
    private StackTrace stackTrace;

    @KivaKitIncludeProperty
    private Severity severity;

    @KivaKitIncludeProperty
    private Time created;

    private transient Message message;

    private transient LogEntryFormatter lastFormatter;

    private transient String formattedEntry;

    @UmlExcludeMember
    public LogEntry(final Logger logger, final LoggerCodeContext context, final Thread thread, final Message message)
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

    public String format(final LogEntryFormatter formatter, final MessageFormatter.Format format)
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
        return ColumnarFormatter.DEFAULT.format(this, format);
    }

    /**
     * @return The formatted message
     */
    public String formattedMessage()
    {
        return formattedMessage(MessageFormatter.Format.WITH_EXCEPTION);
    }

    /**
     *
     */
    public String formattedMessage(final MessageFormatter.Format format)
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

    @KivaKitIncludeProperty
    public Message message()
    {
        return message;
    }

    public String messageType()
    {
        return messageType;
    }

    @KivaKitIncludeProperty
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
        return new ObjectFormatter(this).toString();
    }
}
