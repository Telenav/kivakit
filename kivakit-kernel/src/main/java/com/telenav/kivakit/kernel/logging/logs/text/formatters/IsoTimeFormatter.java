////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.logging.logs.text.formatters;

import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.logging.logs.text.LogEntryFormatter;
import com.telenav.kivakit.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.kernel.language.time.LocalTime;
import com.telenav.kivakit.kernel.language.time.conversion.BaseFormattedConverter;
import com.telenav.kivakit.kernel.language.time.conversion.TimeFormat;
import com.telenav.kivakit.kernel.language.types.Classes;

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
