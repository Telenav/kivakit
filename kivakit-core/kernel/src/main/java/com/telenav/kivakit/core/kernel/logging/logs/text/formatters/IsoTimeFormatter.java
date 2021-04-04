////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.logging.logs.text.formatters;

import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.logging.logs.text.LogEntryFormatter;
import com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.core.kernel.language.time.LocalTime;
import com.telenav.kivakit.core.kernel.language.time.conversion.BaseFormattedConverter;
import com.telenav.kivakit.core.kernel.language.time.conversion.TimeFormat;
import com.telenav.kivakit.core.kernel.language.types.Classes;

import java.time.format.DateTimeFormatter;

/**
 * A simple log formatter with an ISO-formatted date, including milliseconds.
 *
 * @author pierrem
 */
public class IsoTimeFormatter implements LogEntryFormatter
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private final BaseFormattedConverter converter = new BaseFormattedConverter(LOGGER,
            LocalTime.utcTimeZone(), DateTimeFormatter.ISO_LOCAL_DATE_TIME, TimeFormat.DATE_TIME);

    @Override
    public String format(final LogEntry entry, final MessageFormatter.Format format)
    {
        return converter.toString(entry.message().created().utc()) + " " + entry.context() + " "
                + Classes.simpleName(entry.message().getClass()) + ": " + entry.formattedMessage();
    }
}
