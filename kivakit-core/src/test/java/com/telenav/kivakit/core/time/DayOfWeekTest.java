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

import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.time.DayOfWeek.FRIDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.MONDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SATURDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SUNDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.THURSDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.TUESDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.WEDNESDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.isoDayOfWeek;
import static com.telenav.kivakit.core.time.DayOfWeek.javaDayOfWeek;

/**
 * Unit test for {@link DayOfWeek}
 *
 * @author jonathanl (shibo)
 * @author bogdantv
 */
public class DayOfWeekTest extends UnitTest
{
    @Test
    public void asIsoConstant0()
    {
        ensureEqual(0, MONDAY.asIso());
    }

    @Test
    public void asIsoConstant1()
    {
        ensureEqual(1, TUESDAY.asIso());
    }

    @Test
    public void asIsoConstant2()
    {
        ensureEqual(2, WEDNESDAY.asIso());
    }

    @Test
    public void asIsoConstant3()
    {
        ensureEqual(3, THURSDAY.asIso());
    }

    @Test
    public void asIsoConstant4()
    {
        ensureEqual(4, FRIDAY.asIso());
    }

    @Test
    public void asIsoConstant5()
    {
        ensureEqual(5, SATURDAY.asIso());
    }

    @Test
    public void asIsoConstant6()
    {
        ensureEqual(6, SUNDAY.asIso());
    }

    @Test
    public void forIsoConstant0()
    {
        ensureEqual(MONDAY, isoDayOfWeek(0));
    }

    @Test
    public void forIsoConstant1()
    {
        ensureEqual(TUESDAY, isoDayOfWeek(1));
    }

    @Test
    public void forIsoConstant2()
    {
        ensureEqual(WEDNESDAY, isoDayOfWeek(2));
    }

    @Test
    public void forIsoConstant3()
    {
        ensureEqual(THURSDAY, isoDayOfWeek(3));
    }

    @Test
    public void forIsoConstant4()
    {
        ensureEqual(FRIDAY, isoDayOfWeek(4));
    }

    @Test
    public void forIsoConstant5()
    {
        ensureEqual(SATURDAY, isoDayOfWeek(5));
    }

    @Test
    public void forIsoConstant6()
    {
        ensureEqual(SUNDAY, isoDayOfWeek(6));
    }

    @Test
    public void forIsoConstant7()
    {
        ensureThrows(() -> isoDayOfWeek(7));
    }

    @Test
    public void forIsoConstant8()
    {
        ensureThrows(() -> isoDayOfWeek(8));
    }

    @Test
    public void forIsoConstantMinus1()
    {
        ensureThrows(() -> isoDayOfWeek(-1));
    }

    @Test
    public void javaDayOfWeekFRIDAY()
    {
        ensureEqual(FRIDAY, javaDayOfWeek(java.time.DayOfWeek.FRIDAY));
    }

    @Test
    public void javaDayOfWeekMONDAY()
    {
        ensureEqual(MONDAY, javaDayOfWeek(java.time.DayOfWeek.MONDAY));
    }

    @Test
    public void javaDayOfWeekSATURDAY()
    {
        ensureEqual(SATURDAY, javaDayOfWeek(java.time.DayOfWeek.SATURDAY));
    }

    @Test
    public void javaDayOfWeekSUNDAY()
    {
        ensureEqual(SUNDAY, javaDayOfWeek(java.time.DayOfWeek.SUNDAY));
    }

    @Test
    public void javaDayOfWeekTHURSDAY()
    {
        ensureEqual(THURSDAY, javaDayOfWeek(java.time.DayOfWeek.THURSDAY));
    }

    @Test
    public void javaDayOfWeekTUESDAY()
    {
        ensureEqual(TUESDAY, javaDayOfWeek(java.time.DayOfWeek.TUESDAY));
    }

    @Test
    public void javaDayOfWeekWEDNESDAY()
    {
        ensureEqual(WEDNESDAY, javaDayOfWeek(java.time.DayOfWeek.WEDNESDAY));
    }

    @Test
    public void getJodaTimeConstant1()
    {
        ensureEqual(1, MONDAY.asJava());
    }

    @Test
    public void getJodaTimeConstant2()
    {
        ensureEqual(2, TUESDAY.asJava());
    }

    @Test
    public void getJodaTimeConstant3()
    {
        ensureEqual(3, WEDNESDAY.asJava());
    }

    @Test
    public void getJodaTimeConstant4()
    {
        ensureEqual(4, THURSDAY.asJava());
    }

    @Test
    public void getJodaTimeConstant5()
    {
        ensureEqual(5, FRIDAY.asJava());
    }

    @Test
    public void getJodaTimeConstant6()
    {
        ensureEqual(6, SATURDAY.asJava());
    }

    @Test
    public void getJodaTimeConstant7()
    {
        ensureEqual(7, SUNDAY.asJava());
    }
}
