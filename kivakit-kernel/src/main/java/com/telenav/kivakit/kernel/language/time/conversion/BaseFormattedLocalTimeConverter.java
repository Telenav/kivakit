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

import com.telenav.kivakit.kernel.language.strings.Paths;
import com.telenav.kivakit.kernel.language.time.LocalTime;
import com.telenav.kivakit.kernel.language.time.TimeZones;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Converts time to and from a format that is a valid filename across Mac, Linux and Windows operating systems. Time
 * zone display names are parsed and formatted manually since Java doesn't support this in the formatting/parsing
 * strings. For example, for local time in California, the format would be 2020.04.01_4.01pm_PST
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class BaseFormattedLocalTimeConverter extends BaseFormattedConverter
{
    public BaseFormattedLocalTimeConverter(final Listener listener, final DateTimeFormatter formatter)
    {
        this(listener, formatter, null);
    }

    public BaseFormattedLocalTimeConverter(final Listener listener, final DateTimeFormatter formatter,
                                           final ZoneId zone)
    {
        super(listener, formatter, zone);
    }

    protected boolean addTimeZone()
    {
        return true;
    }

    @Override
    protected String onToString(final LocalTime value)
    {
        final var timeZone = value.timeZone();
        return formatter().format(Instant.ofEpochMilli(value.asMilliseconds())
                .atZone(timeZone)) + (addTimeZone() ? "_" + TimeZones.displayName(timeZone) : "");
    }

    @Override
    protected LocalTime onToValue(final String value)
    {
        zone(zone(value));
        var time = Paths.withoutSuffix(value, '_');
        if (time == null)
        {
            time = value;
        }
        return super.onToValue(time);
    }

    @SuppressWarnings("ConstantConditions")
    private ZoneId zone(final String value)
    {
        if (zone() != null)
        {
            return zone();
        }
        final var zone = TimeZones.forDisplayName(Paths.optionalSuffix(value, '_'));
        if (zone != null)
        {
            return zone;
        }
        return LocalTime.localTimeZone();
    }
}
