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

package com.telenav.kivakit.core.kernel.language.time;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * Enum for a Joda day of week
 *
 * @author matthieun
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public enum DayOfWeek
{
    MONDAY(java.time.DayOfWeek.MONDAY),
    TUESDAY(java.time.DayOfWeek.TUESDAY),
    WEDNESDAY(java.time.DayOfWeek.WEDNESDAY),
    THURSDAY(java.time.DayOfWeek.THURSDAY),
    FRIDAY(java.time.DayOfWeek.FRIDAY),
    SATURDAY(java.time.DayOfWeek.SATURDAY),
    SUNDAY(java.time.DayOfWeek.SUNDAY);

    public static DayOfWeek forHistoricalConstant(final int historicalConstant)
    {
        switch (historicalConstant)
        {
            case 1:
                return SUNDAY;
            case 2:
                return MONDAY;
            case 3:
                return TUESDAY;
            case 4:
                return WEDNESDAY;
            case 5:
                return THURSDAY;
            case 6:
                return FRIDAY;
            case 7:
                return SATURDAY;
            default:
                return Ensure.fail("Historical day of week " + historicalConstant + " is not valid");
        }
    }

    public static DayOfWeek forIsoConstant(final int isoConstant)
    {
        switch (isoConstant)
        {
            case 0:
                return MONDAY;
            case 1:
                return TUESDAY;
            case 2:
                return WEDNESDAY;
            case 3:
                return THURSDAY;
            case 4:
                return FRIDAY;
            case 5:
                return SATURDAY;
            case 6:
                return SUNDAY;
            default:
                return Ensure.fail("Day of week (ISO) " + isoConstant + " not found");
        }
    }

    public static DayOfWeek forJavaDayOfWeek(final java.time.DayOfWeek dayOfWeek)
    {
        switch (dayOfWeek)
        {
            case SUNDAY:
                return SUNDAY;
            case MONDAY:
                return MONDAY;
            case TUESDAY:
                return TUESDAY;
            case WEDNESDAY:
                return WEDNESDAY;
            case THURSDAY:
                return THURSDAY;
            case FRIDAY:
                return FRIDAY;
            case SATURDAY:
                return SATURDAY;
            default:
                return Ensure.fail("Historical day of week " + dayOfWeek + " is not valid");
        }
    }

    private final java.time.DayOfWeek internalDayOfWeek;

    DayOfWeek(final java.time.DayOfWeek dayOfWeek)
    {
        internalDayOfWeek = dayOfWeek;
    }

    public int asHistoricalConstant()
    {
        switch (this)
        {
            case SUNDAY:
                return 1;

            case MONDAY:
                return 2;

            case TUESDAY:
                return 3;

            case WEDNESDAY:
                return 4;

            case THURSDAY:
                return 5;

            case FRIDAY:
                return 6;

            case SATURDAY:
                return 7;

            default:
                Ensure.fail("Historical day of week " + this + " is not valid");
                return -1;
        }
    }

    /**
     * @return This day of the week as an ISO-8601 constant
     */
    public int asIsoConstant()
    {
        switch (internalDayOfWeek)
        {
            case MONDAY:
                return 0;
            case TUESDAY:
                return 1;
            case WEDNESDAY:
                return 2;
            case THURSDAY:
                return 3;
            case FRIDAY:
                return 4;
            case SATURDAY:
                return 5;
            case SUNDAY:
                return 6;
            default:
                Ensure.fail("Day of week " + this + " is not valid");
                return -1;
        }
    }

    public int jodaTimeConstant()
    {
        return asIsoConstant() + 1;
    }

    @Override
    public String toString()
    {
        return name();
    }
}
