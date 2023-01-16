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
import java.time.temporal.ChronoField;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * @author jonathanl (shibo)
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramTime.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class TimeFormats
{
    /** KivaKit date format, which is compatible with all filesystems */
    public static final DateTimeFormatter KIVAKIT_DATE = builder("yyyy.MM.dd")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
            .toFormatter();

    /** KivaKit time format, which is compatible with all filesystems */
    public static final DateTimeFormatter KIVAKIT_TIME = builder("h.mma")
            .parseDefaulting(ChronoField.YEAR, LocalTime.now().year().asUnits())
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, LocalTime.now().month().monthOfYear())
            .parseDefaulting(ChronoField.DAY_OF_MONTH, LocalTime.now().dayOfMonth().asUnits())
            .toFormatter();

    /** KivaKit date and time format */
    public static final DateTimeFormatter KIVAKIT_DATE_TIME = builder("yyyy.MM.dd_h.mma").toFormatter();

    /** KivaKit date, time, and seconds format */
    public static final DateTimeFormatter KIVAKIT_DATE_TIME_SECONDS = builder("yyyy.MM.dd_h.mm.ssa").toFormatter();

    /** KivaKit date, time, seconds, and milliseconds format */
    public static final DateTimeFormatter KIVAKIT_DATE_TIME_MILLISECONDS = builder("yyyy.MM.dd_h.mm.ss.SSSa").toFormatter();

    /**
     * Returns a case sensitive {@link DateTimeFormatterBuilder} for the given pattern
     */
    private static DateTimeFormatterBuilder builder(String pattern)
    {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern(pattern);
    }
}
