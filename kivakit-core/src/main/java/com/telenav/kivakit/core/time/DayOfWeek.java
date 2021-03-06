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

import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.lexakai.DiagramTime;
import com.telenav.kivakit.core.testing.NoTestRequired;
import com.telenav.kivakit.core.testing.Tested;
import com.telenav.kivakit.interfaces.time.Nanoseconds;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

import static com.telenav.kivakit.core.ensure.Ensure.ensureBetweenInclusive;
import static com.telenav.kivakit.core.time.BaseTime.Topology.CYCLIC;
import static com.telenav.kivakit.core.time.Day.nanosecondsPerDay;
import static com.telenav.kivakit.core.time.HourOfWeek.hourOfWeek;
import static com.telenav.kivakit.interfaces.string.Stringable.Format.USER_LABEL;

/**
 * Typesafe value for day of week. Supports both ISO and Java ordinals.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #isoDayOfWeek(int)} - Constructs from an ISO day of the week (0-6)</li>
 *     <li>{@link #javaDayOfWeek(int)} - Constructs from a Java day of the week (1-7)</li>
 *     <li>{@link #javaDayOfWeek(java.time.DayOfWeek)} - Constructs from a Java day of the week</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asDay()} - Converts to a {@link Day} object</li>
 *     <li>{@link #asIsoOrdinal()} - The ISO number for this day of the week, from 0 to 6</li>
 *     <li>{@link #asJavaOrdinal()} - The Java number for this day of the week, from 1 to 7</li>
 *     <li>{@link #asJavaDayOfWeek()} - This day of the week as a Java day of the week object</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @author matthieun
 * @see java.time.DayOfWeek
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramTime.class)
@LexakaiJavadoc(complete = true)
@Tested
public class DayOfWeek extends BaseTime<DayOfWeek>
{
    public static final DayOfWeek MONDAY = isoDayOfWeek(0);

    public static final DayOfWeek TUESDAY = isoDayOfWeek(1);

    public static final DayOfWeek WEDNESDAY = isoDayOfWeek(2);

    public static final DayOfWeek THURSDAY = isoDayOfWeek(3);

    public static final DayOfWeek FRIDAY = isoDayOfWeek(4);

    public static final DayOfWeek SATURDAY = isoDayOfWeek(5);

    public static final DayOfWeek SUNDAY = isoDayOfWeek(6);

    public static List<DayOfWeek> daysOfWeek()
    {
        return List.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY);
    }

    /**
     * Retrieves the day of the week for an ISO ordinal from 0 to 6
     *
     * @param day The ordinal from 0 to 6
     * @return The day of the week
     */
    @Tested
    public static DayOfWeek isoDayOfWeek(int day)
    {
        return new DayOfWeek(day);
    }

    /**
     * Retrieves the day of the week for Java ordinal value from 1 to 7
     *
     * @param day The ordinal from 1 to 7
     * @return The day of the week
     */
    @Tested
    public static DayOfWeek javaDayOfWeek(java.time.DayOfWeek day)
    {
        return javaDayOfWeek(day.getValue());
    }

    /**
     * The day of the week for Java ordinal value from 1 to 7
     *
     * @param day The value from 1 to 7
     * @return The day of the week
     */
    @Tested
    public static DayOfWeek javaDayOfWeek(int day)
    {
        return isoDayOfWeek(day - 1);
    }

    protected DayOfWeek()
    {
    }

    @NoTestRequired
    protected DayOfWeek(int day)
    {
        super(nanosecondsPerDay.times(day));

        ensureBetweenInclusive(asIsoOrdinal(), 0, 6, "Invalid day of the week: " + this);
    }

    @NoTestRequired
    public Day asDay()
    {
        return Day.isoDayOfWeek(asIsoOrdinal());
    }

    @Tested
    public HourOfWeek asHourOfWeek()
    {
        return hourOfWeek(asIsoOrdinal() * 24);
    }

    /**
     * @return This day of the week as an ISO-8601 ordinal value
     */
    @Tested
    public int asIsoOrdinal()
    {
        return asUnits();
    }

    @Tested
    public java.time.DayOfWeek asJavaDayOfWeek()
    {
        return java.time.DayOfWeek.of(asJavaOrdinal());
    }

    @Tested
    public int asJavaOrdinal()
    {
        return asIsoOrdinal() + 1;
    }

    @Override
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @NoTestRequired
    public String asString(Format format)
    {
        return asJavaDayOfWeek().name();
    }

    public HourOfWeek at(Hour hour)
    {
        return hourOfWeek(this, hour);
    }

    @Override
    @Tested
    public boolean equals(Object object)
    {
        if (object instanceof DayOfWeek)
        {
            DayOfWeek that = (DayOfWeek) object;
            return this.asIsoOrdinal() == that.asIsoOrdinal();
        }
        return false;
    }

    @Override
    @Tested
    public int hashCode()
    {
        return Hash.code(asIsoOrdinal());
    }

    @Override
    @NoTestRequired
    public DayOfWeek maximum()
    {
        return SUNDAY;
    }

    @Override
    @NoTestRequired
    public DayOfWeek minimum()
    {
        return MONDAY;
    }

    @Override
    public Nanoseconds nanosecondsPerUnit()
    {
        return nanosecondsPerDay;
    }

    @Override
    public Duration newDuration(Nanoseconds nanoseconds)
    {
        return Duration.nanoseconds(nanoseconds);
    }

    @Override
    public DayOfWeek next()
    {
        if (this == SUNDAY)
        {
            return null;
        }
        return isoDayOfWeek(asUnits() + 1);
    }

    @Override
    public DayOfWeek onNewTime(Nanoseconds nanoseconds)
    {
        return isoDayOfWeek((int) nanosecondsToUnits(nanoseconds));
    }

    @Override
    @NoTestRequired
    public String toString()
    {
        return asString(USER_LABEL);
    }

    @Override
    protected Topology topology()
    {
        return CYCLIC;
    }
}
