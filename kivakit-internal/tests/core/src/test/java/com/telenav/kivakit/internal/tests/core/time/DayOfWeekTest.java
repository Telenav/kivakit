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

package com.telenav.kivakit.internal.tests.core.time;

import com.telenav.kivakit.core.time.DayOfWeek;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import java.util.HashMap;

import static com.telenav.kivakit.core.time.DayOfWeek.FRIDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.MONDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SATURDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SUNDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.THURSDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.TUESDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.WEDNESDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.isoDayOfWeek;
import static com.telenav.kivakit.core.time.DayOfWeek.javaDayOfWeek;
import static com.telenav.kivakit.core.value.count.Count._1;
import static com.telenav.kivakit.core.value.count.Count._7;
import static com.telenav.kivakit.core.value.count.Range.rangeInclusive;

/**
 * Unit test for {@link DayOfWeek}
 *
 * @author jonathanl (shibo)
 * @author bogdantv
 */
public class DayOfWeekTest extends CoreUnitTest
{
    @Test
    public void testArithmetic()
    {
        ensureEqual(TUESDAY, MONDAY.plusUnits(1));
        ensureEqual(MONDAY, TUESDAY.minusUnits(1));
    }

    @Test
    public void testDayOfWeek()
    {
        ensureEqual(MONDAY, javaDayOfWeek(1));
        ensureEqual(TUESDAY, javaDayOfWeek(2));
        ensureEqual(WEDNESDAY, javaDayOfWeek(3));
        ensureEqual(THURSDAY, javaDayOfWeek(4));
        ensureEqual(FRIDAY, javaDayOfWeek(5));
        ensureEqual(SATURDAY, javaDayOfWeek(6));
        ensureEqual(SUNDAY, javaDayOfWeek(7));

        ensureEqual(MONDAY, isoDayOfWeek(0));
        ensureEqual(TUESDAY, isoDayOfWeek(1));
        ensureEqual(WEDNESDAY, isoDayOfWeek(2));
        ensureEqual(THURSDAY, isoDayOfWeek(3));
        ensureEqual(FRIDAY, isoDayOfWeek(4));
        ensureEqual(SATURDAY, isoDayOfWeek(5));
        ensureEqual(SUNDAY, isoDayOfWeek(6));

        ensureThrows(() -> isoDayOfWeek(-1));
        ensureThrows(() -> isoDayOfWeek(-1));
        ensureThrows(() -> javaDayOfWeek(0));
        ensureThrows(() -> javaDayOfWeek(8));
        ensureThrows(() -> isoDayOfWeek(7));
    }

    @Test
    public void testEquals()
    {
        ensureEqual(MONDAY, isoDayOfWeek(0));
        ensureEqual(MONDAY, javaDayOfWeek(1));

        ensureEqual(TUESDAY, isoDayOfWeek(1));
        ensureEqual(TUESDAY, javaDayOfWeek(2));

        ensureEqual(WEDNESDAY, isoDayOfWeek(2));
        ensureEqual(WEDNESDAY, javaDayOfWeek(3));

        ensureEqual(THURSDAY, isoDayOfWeek(3));
        ensureEqual(THURSDAY, javaDayOfWeek(4));

        ensureEqual(FRIDAY, isoDayOfWeek(4));
        ensureEqual(FRIDAY, javaDayOfWeek(5));

        ensureEqual(SATURDAY, isoDayOfWeek(5));
        ensureEqual(SATURDAY, javaDayOfWeek(6));

        ensureEqual(SUNDAY, isoDayOfWeek(6));
        ensureEqual(SUNDAY, javaDayOfWeek(7));

        ensureNotEqual(TUESDAY, isoDayOfWeek(0));
        ensureNotEqual(TUESDAY, javaDayOfWeek(1));

        var map = new HashMap<DayOfWeek, Integer>();

        rangeInclusive(_1, _7).forEachInt(at -> map.put(javaDayOfWeek(at), at));
        rangeInclusive(_1, _7).forEachInt(at -> ensureEqual(map.get(javaDayOfWeek(at)), at));
    }

    @Test
    public void testIsoConstants()
    {
        ensureEqual(0, MONDAY.asIsoOrdinal());
        ensureEqual(1, TUESDAY.asIsoOrdinal());
        ensureEqual(2, WEDNESDAY.asIsoOrdinal());
        ensureEqual(3, THURSDAY.asIsoOrdinal());
        ensureEqual(4, FRIDAY.asIsoOrdinal());
        ensureEqual(5, SATURDAY.asIsoOrdinal());
        ensureEqual(6, SUNDAY.asIsoOrdinal());

        ensureEqual(MONDAY, isoDayOfWeek(0));
        ensureEqual(TUESDAY, isoDayOfWeek(1));
        ensureEqual(WEDNESDAY, isoDayOfWeek(2));
        ensureEqual(THURSDAY, isoDayOfWeek(3));
        ensureEqual(FRIDAY, isoDayOfWeek(4));
        ensureEqual(SATURDAY, isoDayOfWeek(5));
        ensureEqual(SUNDAY, isoDayOfWeek(6));

        ensureThrows(() -> isoDayOfWeek(7));
        ensureThrows(() -> isoDayOfWeek(8));
        ensureThrows(() -> isoDayOfWeek(-1));
    }

    @Test
    public void testJavaConstants()
    {
        ensureEqual(1, MONDAY.asJavaOrdinal());
        ensureEqual(2, TUESDAY.asJavaOrdinal());
        ensureEqual(3, WEDNESDAY.asJavaOrdinal());
        ensureEqual(4, THURSDAY.asJavaOrdinal());
        ensureEqual(5, FRIDAY.asJavaOrdinal());
        ensureEqual(6, SATURDAY.asJavaOrdinal());
        ensureEqual(7, SUNDAY.asJavaOrdinal());

        ensureEqual(MONDAY, javaDayOfWeek(1));
        ensureEqual(TUESDAY, javaDayOfWeek(2));
        ensureEqual(WEDNESDAY, javaDayOfWeek(3));
        ensureEqual(THURSDAY, javaDayOfWeek(4));
        ensureEqual(FRIDAY, javaDayOfWeek(5));
        ensureEqual(SATURDAY, javaDayOfWeek(6));
        ensureEqual(SUNDAY, javaDayOfWeek(7));

        ensureThrows(() -> javaDayOfWeek(8));
        ensureThrows(() -> javaDayOfWeek(0));
        ensureThrows(() -> javaDayOfWeek(-1));
    }

    @Test
    public void testJavaDayOfWeek()
    {
        ensureEqual(MONDAY, javaDayOfWeek(java.time.DayOfWeek.MONDAY));
        ensureEqual(TUESDAY, javaDayOfWeek(java.time.DayOfWeek.TUESDAY));
        ensureEqual(WEDNESDAY, javaDayOfWeek(java.time.DayOfWeek.WEDNESDAY));
        ensureEqual(THURSDAY, javaDayOfWeek(java.time.DayOfWeek.THURSDAY));
        ensureEqual(FRIDAY, javaDayOfWeek(java.time.DayOfWeek.FRIDAY));
        ensureEqual(SATURDAY, javaDayOfWeek(java.time.DayOfWeek.SATURDAY));
        ensureEqual(SUNDAY, javaDayOfWeek(java.time.DayOfWeek.SUNDAY));

        ensureEqual(MONDAY.asJavaDayOfWeek(), java.time.DayOfWeek.MONDAY);
        ensureEqual(TUESDAY.asJavaDayOfWeek(), java.time.DayOfWeek.TUESDAY);
        ensureEqual(WEDNESDAY.asJavaDayOfWeek(), java.time.DayOfWeek.WEDNESDAY);
        ensureEqual(THURSDAY.asJavaDayOfWeek(), java.time.DayOfWeek.THURSDAY);
        ensureEqual(FRIDAY.asJavaDayOfWeek(), java.time.DayOfWeek.FRIDAY);
        ensureEqual(SATURDAY.asJavaDayOfWeek(), java.time.DayOfWeek.SATURDAY);
        ensureEqual(SUNDAY.asJavaDayOfWeek(), java.time.DayOfWeek.SUNDAY);
    }
}
