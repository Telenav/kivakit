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

package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.project.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Convert to/from local time
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
public class BaseFormattedConverter extends BaseStringConverter<LocalTime>
{
    /** The date time formatter */
    private DateTimeFormatter formatter;

    /** The local time zone */
    private ZoneId zone;

    public BaseFormattedConverter(Listener listener,
                                  DateTimeFormatter formatter,
                                  ZoneId zone)
    {
        super(listener);
        this.zone = zone;
        this.formatter = formatter;
    }

    public DateTimeFormatter formatter()
    {
        return formatter.withZone(zone);
    }

    public void formatter(DateTimeFormatter formatter)
    {
        this.formatter = formatter;
    }

    public ZoneId zone()
    {
        return zone;
    }

    public void zone(ZoneId zone)
    {
        this.zone = zone;
    }

    @Override
    protected String onToString(LocalTime value)
    {
        return formatter().format(value.javaLocalDateTime());
    }

    @Override
    protected LocalTime onToValue(String value)
    {
        var parsed = formatter().parse(value);
        var time = Instant.from(parsed);
        return LocalTime.milliseconds(zone, time.toEpochMilli());
    }
}
