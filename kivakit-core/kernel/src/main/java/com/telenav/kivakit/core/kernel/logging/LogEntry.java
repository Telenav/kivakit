////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging;

import com.telenav.kivakit.core.kernel.language.objects.Objects;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.core.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.core.kernel.language.threading.context.StackTrace;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.types.Classes;
import com.telenav.kivakit.core.kernel.logging.logs.text.LogEntryFormatter;
import com.telenav.kivakit.core.kernel.logging.logs.text.formatters.ColumnarFormatter;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.core.kernel.messaging.messages.Severity;
import com.telenav.kivakit.core.kernel.messaging.messages.Triaged;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLogging;
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
