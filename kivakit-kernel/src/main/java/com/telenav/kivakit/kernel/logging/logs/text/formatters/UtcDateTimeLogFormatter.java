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

package com.telenav.kivakit.kernel.logging.logs.text.formatters;

import com.telenav.kivakit.kernel.language.time.conversion.converters.UtcDateTimeConverter;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.logging.logs.text.LogFormatter;
import com.telenav.kivakit.kernel.messaging.messages.MessageFormatter;

/**
 * A simple log formatter with an ISO-formatted UTC date, including milliseconds.
 *
 * @author pierrem
 */
public class UtcDateTimeLogFormatter implements LogFormatter
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private final UtcDateTimeConverter converter = new UtcDateTimeConverter(LOGGER);

    @Override
    public String format(final LogEntry entry, final MessageFormatter.Format format)
    {
        return converter.unconvert(entry.message().created().utc()) + " " + entry.context() + " "
                + Classes.simpleName(entry.message().getClass()) + ": " + entry.formattedMessage();
    }
}
