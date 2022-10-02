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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Convert to/from local time
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class BaseFormattedConverter extends BaseStringConverter<LocalTime>
{
    /** The date time formatter */
    private DateTimeFormatter formatter;

    /** The local time zone */
    private ZoneId zone;

    /**
     * @param listener The listener to report problems to
     * @param formatter The formatter to use
     * @param zone The timezone
     */
    public BaseFormattedConverter(Listener listener,
                                  DateTimeFormatter formatter,
                                  ZoneId zone)
    {
        super(listener);
        this.zone = zone;
        this.formatter = formatter;
    }

    /**
     * Returns the formatter in this timezone
     */
    public DateTimeFormatter formatter()
    {
        return formatter.withZone(zone);
    }

    /**
     * Sets the formatter
     */
    public void formatter(DateTimeFormatter formatter)
    {
        this.formatter = formatter;
    }

    /**
     * Returns the zone id
     */
    public ZoneId zone()
    {
        return zone;
    }

    /**
     * Sets the zone id
     */
    public void zone(ZoneId zone)
    {
        this.zone = zone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String onToString(LocalTime value)
    {
        return formatter().format(value.asJavaLocalDateTime());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected LocalTime onToValue(String value)
    {
        var parsed = formatter().parse(value);
        var time = Instant.from(parsed);
        return LocalTime.milliseconds(zone, time.toEpochMilli());
    }
}
