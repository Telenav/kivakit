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
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.time.Time;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.time.Duration.days;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
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
    public HumanizedLocalDateTimeConverter(Listener listener)
    {
        super(listener);
    }

    @Override
    protected String onToString(LocalTime time)
    {
        return humanizedDate(time) + " " + new LocalTimeConverter(Listener.emptyListener(), time.timeZone()).unconvert(time);
    }

    @Override
    protected LocalTime onToValue(String value)
    {
        var matcher = HUMANIZED_DATE.matcher(value);
        if (matcher.matches())
        {
            var localTime = new LocalTimeConverter(Listener.emptyListener(), LocalTime.localTimeZone())
                    .convert(matcher.group("time"));
            if (localTime != null)
            {
                var yesterday = matcher.group("yesterday");
                var today = matcher.group("today");
                var day = matcher.group("day");
                var date = matcher.group("date");

                var now = LocalTime.now();
                if (today != null)
                {
                    return localTime.withUnixEpochDay(now.dayOfUnixEpoch());
                }
                if (yesterday != null)
                {
                    return localTime.withUnixEpochDay(now.dayOfUnixEpoch().decremented());
                }
                if (day != null)
                {
                    var dayOfWeek = dayOrdinal.get(day.toLowerCase());
                    var nowDayOfWeek = now.dayOfWeek().asUnits();
                    var daysAgo = nowDayOfWeek - dayOfWeek;
                    if (daysAgo < 0)
                    {
                        daysAgo += 7;
                    }
                    return localTime.withUnixEpochDay(now.dayOfUnixEpoch().minusUnits(daysAgo));
                }
                if (date != null)
                {
                    var localDate = new LocalDateConverter(Listener.emptyListener()).convert(date);
                    if (localDate != null)
                    {
                        return localTime.withUnixEpochDay(localDate.dayOfUnixEpoch());
                    }
                }
            }
        }
        return null;
    }

    private String humanizedDate(LocalTime time)
    {
        var now = Time.now().asLocalTime();
        var nowYear = now.year();
        var nowDayOfYear = now.dayOfYear();

        if (nowYear.equals(time.year()))
        {
            if (nowDayOfYear.equals(time.dayOfYear()))
            {
                return "Today";
            }
            if (nowDayOfYear.equals(time.dayOfYear().incremented()))
            {
                return "Yesterday";
            }
            if (nowDayOfYear.minus(time.dayOfYear()).isLessThanOrEqualTo(days(7)))
            {
                return time.dayOfWeek().toString();
            }
        }

        return new LocalDateConverter(Listener.emptyListener()).unconvert(time);
    }
}
