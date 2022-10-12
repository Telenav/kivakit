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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.string.Paths.pathOptionalSuffix;
import static com.telenav.kivakit.core.string.Paths.pathWithoutSuffix;
import static com.telenav.kivakit.core.time.LocalTime.localTimeZone;
import static com.telenav.kivakit.core.time.TimeZones.parseShortDisplayName;
import static com.telenav.kivakit.core.time.TimeZones.shortDisplayName;

/**
 * Converts time to and from a format that is a valid filename across Mac, Linux and Windows operating systems. Time
 * zone display names are parsed and formatted manually since Java doesn't support this in the formatting/parsing
 * strings. For example, for local time in California, the format would be 2020.04.01_4.01pm_PST
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class BaseFormattedLocalTimeConverter extends BaseFormattedConverter
{
    /**
     * @param listener The listener to report problems to
     * @param formatter The formatter to use
     */
    public BaseFormattedLocalTimeConverter(Listener listener,
                                           DateTimeFormatter formatter)
    {
        this(listener, formatter, null);
    }

    /**
     * @param listener The listener to report problems to
     * @param formatter The formatter to use
     * @param zone The timezone
     */
    public BaseFormattedLocalTimeConverter(Listener listener,
                                           DateTimeFormatter formatter,
                                           ZoneId zone)
    {
        super(listener, formatter, zone);
    }

    /**
     * True if string conversions should append the short display name for the timezone
     */
    protected boolean appendTimeZone()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String onToString(LocalTime value)
    {
        var timeZone = value.timeZone();
        return formatter().format(Instant.ofEpochMilli(value.milliseconds())
                .atZone(timeZone)) + (appendTimeZone() ? "_" + shortDisplayName(timeZone) : "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected LocalTime onToValue(String value)
    {
        zone(zone(this, value));
        var time = pathWithoutSuffix(value, '_');
        if (time == null)
        {
            time = value;
        }
        return super.onToValue(time);
    }

    private ZoneId zone(Listener listener, String value)
    {
        if (zone() != null)
        {
            return zone();
        }
        var zone = parseShortDisplayName(listener, pathOptionalSuffix(value, '_'));
        if (zone != null)
        {
            return zone;
        }
        return localTimeZone();
    }
}
