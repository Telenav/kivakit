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

package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramTime.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class KivaKitTimeFormats
{
    /** KivaKit date format, which is compatible with all filesystems */
    public static final DateTimeFormatter KIVAKIT_DATE = builder("yyyy.MM.dd")
        .parseDefaulting(HOUR_OF_DAY, 0)
        .parseDefaulting(MINUTE_OF_HOUR, 0)
        .parseDefaulting(SECOND_OF_MINUTE, 0)
        .parseDefaulting(MILLI_OF_SECOND, 0)
        .toFormatter();

    /** KivaKit time format, which is compatible with all filesystems */
    public static final DateTimeFormatter KIVAKIT_TIME = builder("h.mma")
        .parseDefaulting(YEAR, LocalTime.now().year().asUnits())
        .parseDefaulting(MONTH_OF_YEAR, LocalTime.now().month().monthOfYear())
        .parseDefaulting(DAY_OF_MONTH, LocalTime.now().dayOfMonth().asUnits())
        .toFormatter();

    /** KivaKit date and time format */
    public static final DateTimeFormatter KIVAKIT_DATE_TIME = builder("yyyy.MM.dd_hh.mma_z")
        .toFormatter();

    /** KivaKit date and time format */
    public static final DateTimeFormatter KIVAKIT_UTC_DATE_TIME = builder("yyyy.MM.dd_hh.mma")
        .toFormatter();

    /** KivaKit date, time, and seconds format */
    public static final DateTimeFormatter KIVAKIT_DATE_TIME_SECONDS = builder("yyyy.MM.dd_hh.mm.ssa_z")
        .toFormatter();

    /** KivaKit date, time, seconds, and milliseconds format */
    public static final DateTimeFormatter KIVAKIT_DATE_TIME_MILLISECONDS = builder("yyyy.MM.dd_hh.mm.ss.SSSa_z")
        .toFormatter();

    /**
     * Returns a case-sensitive {@link DateTimeFormatterBuilder} for the given pattern
     */
    private static DateTimeFormatterBuilder builder(String pattern)
    {
        return new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern(pattern);
    }
}
