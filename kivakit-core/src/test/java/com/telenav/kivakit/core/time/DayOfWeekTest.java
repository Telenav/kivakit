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

import com.telenav.kivakit.core.test.CoreUnitTest;
import org.junit.Test;

import java.util.HashMap;

import static com.telenav.kivakit.core.time.DayOfWeek.FRIDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.MONDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SATURDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SUNDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.Standard.ISO;
import static com.telenav.kivakit.core.time.DayOfWeek.Standard.JAVA;
import static com.telenav.kivakit.core.time.DayOfWeek.THURSDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.TUESDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.WEDNESDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.dayOfWeek;
import static com.telenav.kivakit.core.time.DayOfWeek.isoDayOfWeek;
import static com.telenav.kivakit.core.time.DayOfWeek.javaDayOfWeek;
import static com.telenav.kivakit.core.value.count.Count._1;
import static com.telenav.kivakit.core.value.count.Count._7;
import static com.telenav.kivakit.core.value.count.Range.rangeInclusive;
import static com.telenav.kivakit.core.value.level.Percent.percent;

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
        ensureEqual(TUESDAY, MONDAY.plus(1));
        ensureEqual(MONDAY, TUESDAY.minus(1));

        ensureThrows(() -> TUESDAY.times(5));
        ensureThrows(() -> TUESDAY.times(5.5));
        ensureThrows(() -> TUESDAY.percent(percent(5)));
        ensureThrows(() -> TUESDAY.percentOf(WEDNESDAY));
    }

    @Test
    public void testDayOfWeek()
    {
        ensureEqual(MONDAY, dayOfWeek(1, JAVA));
        ensureEqual(TUESDAY, dayOfWeek(2, JAVA));
        ensureEqual(WEDNESDAY, dayOfWeek(3, JAVA));
        ensureEqual(THURSDAY, dayOfWeek(4, JAVA));
        ensureEqual(FRIDAY, dayOfWeek(5, JAVA));
        ensureEqual(SATURDAY, dayOfWeek(6, JAVA));
        ensureEqual(SUNDAY, dayOfWeek(7, JAVA));

        ensureEqual(MONDAY, dayOfWeek(0, ISO));
        ensureEqual(TUESDAY, dayOfWeek(1, ISO));
        ensureEqual(WEDNESDAY, dayOfWeek(2, ISO));
        ensureEqual(THURSDAY, dayOfWeek(3, ISO));
        ensureEqual(FRIDAY, dayOfWeek(4, ISO));
        ensureEqual(SATURDAY, dayOfWeek(5, ISO));
        ensureEqual(SUNDAY, dayOfWeek(6, ISO));

        ensureThrows(() -> dayOfWeek(-1, JAVA));
        ensureThrows(() -> dayOfWeek(-1, ISO));
        ensureThrows(() -> dayOfWeek(0, JAVA));
        ensureThrows(() -> dayOfWeek(8, JAVA));
        ensureThrows(() -> dayOfWeek(7, ISO));
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

        rangeInclusive(_1, _7).forEachInt(at -> map.put(DayOfWeek.javaDayOfWeek(at), at));
        rangeInclusive(_1, _7).forEachInt(at -> ensureEqual(map.get(DayOfWeek.javaDayOfWeek(at)), at));
    }

    @Test
    public void testIs()
    {
        ensure(isoDayOfWeek(0).isIso());
        ensure(javaDayOfWeek(1).isJava());
    }

    @Test
    public void testIsoConstants()
    {
        ensureEqual(0, MONDAY.asIso());
        ensureEqual(1, TUESDAY.asIso());
        ensureEqual(2, WEDNESDAY.asIso());
        ensureEqual(3, THURSDAY.asIso());
        ensureEqual(4, FRIDAY.asIso());
        ensureEqual(5, SATURDAY.asIso());
        ensureEqual(6, SUNDAY.asIso());

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
        ensureEqual(1, MONDAY.asJava());
        ensureEqual(2, TUESDAY.asJava());
        ensureEqual(3, WEDNESDAY.asJava());
        ensureEqual(4, THURSDAY.asJava());
        ensureEqual(5, FRIDAY.asJava());
        ensureEqual(6, SATURDAY.asJava());
        ensureEqual(7, SUNDAY.asJava());

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
