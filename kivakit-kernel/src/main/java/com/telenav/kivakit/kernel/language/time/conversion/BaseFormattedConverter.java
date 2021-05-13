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

package com.telenav.kivakit.kernel.language.time.conversion;

import com.telenav.kivakit.kernel.language.time.LocalTime;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Convert to/from local time
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class BaseFormattedConverter extends BaseStringConverter<LocalTime>
{
    /** The date time formatter */
    private final DateTimeFormatter formatter;

    /** The local time zone */
    private ZoneId zone;

    /** Which kind of formatting is going on */
    private final TimeFormat format;

    public BaseFormattedConverter(final Listener listener, final ZoneId zone,
                                  final DateTimeFormatter formatter, final TimeFormat format)
    {
        super(listener);
        this.zone = zone;
        this.format = format;
        this.formatter = formatter;
    }

    public DateTimeFormatter formatter()
    {
        return formatter.withZone(zone);
    }

    @Override
    protected LocalTime onConvertToObject(final String value)
    {
        switch (format)
        {
            case DATE:
            {
                final var date = LocalDate.parse(value, formatter);
                final var instant = date.atStartOfDay(zone).toInstant();
                return LocalTime.milliseconds(zone, instant.toEpochMilli());
            }

            case TIME:
            {
                final var dateTime = java.time.LocalTime.parse(value, formatter);
                final var instant = dateTime.atDate(LocalDate.EPOCH).atZone(zone).toInstant();
                return LocalTime.milliseconds(zone, instant.toEpochMilli());
            }

            case DATE_TIME:
            {
                final var dateTime = LocalDateTime.parse(value, formatter);
                final var instant = dateTime.atZone(zone).toInstant();
                return LocalTime.milliseconds(zone, instant.toEpochMilli());
            }

            default:
                return null;
        }
    }

    @Override
    protected String onConvertToString(final LocalTime value)
    {
        return formatter().format(value.javaLocalDateTime());
    }

    protected void zone(final ZoneId zone)
    {
        this.zone = zone;
    }
}
