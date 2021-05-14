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

package com.telenav.kivakit.kernel.language.time.conversion.converters;

import com.telenav.kivakit.kernel.language.time.LocalTime;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class HumanizedLocalDateTimeConverter extends BaseStringConverter<LocalTime>
{
    private static final Pattern HUMANIZED_DATE = Pattern.compile("("
            + "(?<yesterday>yesterday)"
            + "|(?<today>today)"
            + "|(?<day>monday|tuesday|wednesday|thursday|friday|saturday|sunday)"
            + "|(?<date>[0-9]{4}\\.[0-9]{1,2}\\.[0-9]{1,2})"
            + ")"
            + "[- ](?<time>.+)", Pattern.CASE_INSENSITIVE);

    private static final Map<String, Integer> dayOrdinal = new HashMap<>();

    static
    {
        dayOrdinal.put("monday", 0);
        dayOrdinal.put("tuesday", 1);
        dayOrdinal.put("wednesday", 2);
        dayOrdinal.put("thursday", 3);
        dayOrdinal.put("friday", 4);
        dayOrdinal.put("saturday", 5);
        dayOrdinal.put("sunday", 6);
    }

    /**
     * @param listener The conversion listener
     */
    public HumanizedLocalDateTimeConverter(final Listener listener)
    {
        super(listener);
    }

    @Override
    protected LocalTime onConvertToObject(final String value)
    {
        final var matcher = HUMANIZED_DATE.matcher(value);
        if (matcher.matches())
        {
            final var localTime = new LocalTimeConverter(Listener.none(), LocalTime.localTimeZone())
                    .convert(matcher.group("time"));
            if (localTime != null)
            {
                final var yesterday = matcher.group("yesterday");
                final var today = matcher.group("today");
                final var day = matcher.group("day");
                final var date = matcher.group("date");

                final var now = LocalTime.now();
                if (today != null)
                {
                    return localTime.withEpochDay(now.epochDay());
                }
                if (yesterday != null)
                {
                    return localTime.withEpochDay(now.epochDay() - 1);
                }
                if (day != null)
                {
                    final var dayOfWeek = dayOrdinal.get(day.toLowerCase());
                    final var nowDayOfWeek = now.dayOfWeek().ordinal();
                    var daysAgo = nowDayOfWeek - dayOfWeek;
                    if (daysAgo < 0)
                    {
                        daysAgo += 7;
                    }
                    return localTime.withEpochDay(now.epochDay() - daysAgo);
                }
                if (date != null)
                {
                    final var localDate = new LocalDateConverter(Listener.none()).convert(date);
                    if (localDate != null)
                    {
                        return localTime.withEpochDay(localDate.epochDay());
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected String onConvertToString(final LocalTime time)
    {
        return humanizedDate(time) + " " + new LocalTimeConverter(Listener.none(), time.timeZone()).toString(time);
    }

    private String humanizedDate(final LocalTime time)
    {
        final var now = Time.now().localTime();
        final var nowYear = now.year();
        final var nowDayOfYear = now.dayOfYear();

        if (nowYear == time.year())
        {
            if (nowDayOfYear == time.dayOfYear())
            {
                return "Today";
            }
            if (nowDayOfYear == time.dayOfYear() + 1)
            {
                return "Yesterday";
            }
            if (nowDayOfYear - time.dayOfYear() <= 7)
            {
                return time.dayOfWeek().toString();
            }
        }

        return new LocalDateConverter(Listener.none()).toString(time);
    }
}
