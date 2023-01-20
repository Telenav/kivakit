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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.time.LocalTime.localTime;

/**
 * Converts between strings and {@link LocalTime} values using a {@link DateTimeFormatter}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class BaseLocalTimeConverter extends BaseStringConverter<LocalTime>
{
    private final DateTimeFormatter formatter;

    /**
     * @param listener The listener to report problems to
     * @param formatter The formatter to use
     */
    public BaseLocalTimeConverter(Listener listener,
                                  DateTimeFormatter formatter)
    {
        this(listener, formatter, ZoneId.systemDefault());
    }

    /**
     * @param listener The listener to report problems to
     * @param formatter The formatter to use
     * @param zone The timezone
     */
    public BaseLocalTimeConverter(Listener listener,
                                  DateTimeFormatter formatter,
                                  ZoneId zone)
    {
        super(listener, LocalTime.class);

        this.formatter = formatter.withZone(zone);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String onToString(LocalTime time)
    {
        var instant = Instant.ofEpochMilli(time.epochMilliseconds());
        return formatter.format(instant);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected LocalTime onToValue(String value)
    {
        var zonedDateTime = formatter.parse(value, ZonedDateTime::from);
        return localTime(zonedDateTime);
    }
}
