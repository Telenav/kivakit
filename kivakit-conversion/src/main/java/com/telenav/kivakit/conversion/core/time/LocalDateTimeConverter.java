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
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.time.TimeFormats.KIVAKIT_DATE;
import static com.telenav.kivakit.core.time.TimeFormats.KIVAKIT_DATE_TIME;
import static com.telenav.kivakit.core.time.TimeFormats.KIVAKIT_TIME;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class LocalDateTimeConverter extends BaseFormattedLocalTimeConverter
{
    /**
     * Returns a converter that converts to/from kivakit date format: "yyyy.MM.dd"
     */
    public static LocalDateTimeConverter kivakitDateConverter(Listener listener, ZoneId zone)
    {
        return new LocalDateTimeConverter(listener, KIVAKIT_DATE, zone);
    }

    /**
     * Returns a converter that converts to/from kivakit date/time format: yyyy.MM.dd_h.mma
     */
    public static LocalDateTimeConverter kivakitDateTimeConverter(Listener listener, ZoneId zone)
    {
        return new LocalDateTimeConverter(listener, KIVAKIT_DATE_TIME, zone);
    }

    /**
     * Returns a converter that converts to/from kivakit time format: "h.mma"
     */
    public static LocalDateTimeConverter kivakitTimeConverter(Listener listener, ZoneId zone)
    {
        return new LocalDateTimeConverter(listener, KIVAKIT_TIME, zone);
    }

    /**
     * @param listener The listener to report problems to
     */
    public LocalDateTimeConverter(Listener listener)
    {
        this(listener, null);
    }

    public LocalDateTimeConverter(Listener listener, ZoneId zone)
    {
        super(listener, KIVAKIT_DATE_TIME, zone);
    }

    public LocalDateTimeConverter(Listener listener, DateTimeFormatter formatter, ZoneId zone)
    {
        super(listener, formatter, zone);
    }
}
