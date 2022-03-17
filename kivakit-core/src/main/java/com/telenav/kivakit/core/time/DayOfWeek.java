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

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.lexakai.DiagramTime;
import com.telenav.kivakit.core.value.count.AbstractCount;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * Typesafe value for day of week. The value stored in the {@link AbstractCount} superclass is the Java day of the week
 * ordinal (see java.time.{@link java.time.DayOfWeek}), where MONDAY is 1 and SUNDAY is 7. The ISO representation is
 * also supported, where MONDAY is 0 and SUNDAY is 6.
 *
 * @author jonathanl (shibo)
 * @author matthieun
 * @see java.time.DayOfWeek
 */
@UmlClassDiagram(diagram = DiagramTime.class)
@LexakaiJavadoc(complete = true)
public class DayOfWeek extends AbstractCount<DayOfWeek>
{
    public static final DayOfWeek MONDAY = javaDayOfWeek(java.time.DayOfWeek.MONDAY);

    public static final DayOfWeek TUESDAY = javaDayOfWeek(java.time.DayOfWeek.TUESDAY);

    public static final DayOfWeek WEDNESDAY = javaDayOfWeek(java.time.DayOfWeek.WEDNESDAY);

    public static final DayOfWeek THURSDAY = javaDayOfWeek(java.time.DayOfWeek.THURSDAY);

    public static final DayOfWeek FRIDAY = javaDayOfWeek(java.time.DayOfWeek.FRIDAY);

    public static final DayOfWeek SATURDAY = javaDayOfWeek(java.time.DayOfWeek.SATURDAY);

    public static final DayOfWeek SUNDAY = javaDayOfWeek(java.time.DayOfWeek.SUNDAY);

    /**
     * The day of the week for an ISO ordinal value from 0 to 6
     *
     * @param isoOrdinal THe value from 0 to 6
     * @return The day of the week
     */
    public static DayOfWeek forIsoOrdinal(int isoOrdinal)
    {
        return javaDayOfWeek(isoOrdinal + 1);
    }

    public static DayOfWeek forJavaDayOfWeek(java.time.DayOfWeek dayOfWeek)
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
                return fail();
        }
    }

    /**
     * The day of the week for Java ordinal value from 1 to 7
     *
     * @param javaOrdinal THe value from 0 to 6
     * @return The day of the week
     */
    public static DayOfWeek forJavaOrdinal(int javaOrdinal)
    {
        switch (javaOrdinal)
        {
            case 1:
                return MONDAY;

            case 2:
                return TUESDAY;

            case 3:
                return WEDNESDAY;

            case 4:
                return THURSDAY;

            case 5:
                return FRIDAY;

            case 6:
                return SATURDAY;

            case 7:
                return SUNDAY;

            default:
                throw new IllegalArgumentException("Day of week (Java) " + javaOrdinal + " not found");
        }
    }

    public static DayOfWeek isoDayOfWeek(int day)
    {
        return javaDayOfWeek(day + 1);
    }

    public static DayOfWeek javaDayOfWeek(java.time.DayOfWeek day)
    {
        return new DayOfWeek(day.getValue());
    }

    public static DayOfWeek javaDayOfWeek(int day)
    {
        return new DayOfWeek(day);
    }

    public DayOfWeek(int javaDayOfWeek)
    {
        super(Ensure.ensureBetweenInclusive(javaDayOfWeek, 1, 7, "Invalid day of the week: "
                + "java = " + javaDayOfWeek
                + ", iso = " + (javaDayOfWeek - 1)));
    }

    /**
     * @return This day of the week as an ISO-8601 ordinal value
     */
    public int asIsoOrdinal()
    {
        return asJavaOrdinal() - 1;
    }

    public java.time.DayOfWeek asJavaDayOfWeek()
    {
        return java.time.DayOfWeek.of(asJavaOrdinal());
    }

    public int asJavaOrdinal()
    {
        return asInt();
    }

    @Override
    public DayOfWeek maximum()
    {
        return SUNDAY;
    }

    @Override
    public DayOfWeek minimum()
    {
        return MONDAY;
    }

    @Override
    public String toString()
    {
        return asJavaDayOfWeek().name();
    }

    @Override
    protected DayOfWeek newInstance(final long javaOrdinal)
    {
        return forJavaOrdinal((int) javaOrdinal);
    }
}
