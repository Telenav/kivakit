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

package com.telenav.kivakit.kernel.language.time.conversion;

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.language.time.LocalTime;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Convert to/from local time
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class BaseFormattedConverter extends BaseStringConverter<LocalTime>
{
    /** The date time formatter */
    private DateTimeFormatter formatter;

    /** The local time zone */
    private ZoneId zone;

    public BaseFormattedConverter(final Listener listener,
                                  final DateTimeFormatter formatter,
                                  final ZoneId zone)
    {
        super(listener);
        this.zone = zone;
        this.formatter = formatter;
    }

    public DateTimeFormatter formatter()
    {
        return formatter.withZone(zone);
    }

    public void formatter(final DateTimeFormatter formatter)
    {
        this.formatter = formatter;
    }

    public ZoneId zone()
    {
        return zone;
    }

    public void zone(final ZoneId zone)
    {
        this.zone = zone;
    }

    @Override
    protected String onToString(final LocalTime value)
    {
        return formatter().format(value.javaLocalDateTime());
    }

    @Override
    protected LocalTime onToValue(final String value)
    {
        final var parsed = formatter().parse(value);
        final var time = Instant.from(parsed);
        return LocalTime.milliseconds(zone, time.toEpochMilli());
    }
}
