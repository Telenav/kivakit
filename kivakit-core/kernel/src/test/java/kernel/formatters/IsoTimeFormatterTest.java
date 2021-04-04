////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.formatters;

import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.logging.LoggerCodeContext;
import com.telenav.kivakit.core.kernel.logging.loggers.LogServiceLogger;
import com.telenav.kivakit.core.kernel.logging.logs.text.formatters.IsoTimeFormatter;
import com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Information;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

/**
 * Created by bogdantnv on 12/07/16.
 */
public class IsoTimeFormatterTest
{
    private final IsoTimeFormatter isoTimeFormatter = new IsoTimeFormatter();

    @Test
    public void format()
    {
        final LoggerCodeContext context = new LoggerCodeContext("Here");
        final Information message = new Information("This is a test");
        message.created(Time.milliseconds(1468309671357L));
        final var logger = new LogServiceLogger();
        final var entry = new LogEntry(logger, context, Thread.currentThread(), message);
        final String actual = isoTimeFormatter.format(entry, MessageFormatter.Format.WITH_EXCEPTION);
        final var expected = "2016-07-12T07:47:51.357 Here Information: This is a test";
        ensureEqual(expected, actual);
    }
}
