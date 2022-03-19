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
import static com.telenav.kivakit.core.time.DayOfWeek.forIsoOrdinal;
import static com.telenav.kivakit.core.time.DayOfWeek.forJavaDayOfWeek;

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
        ensureEqual(0, MONDAY.asIsoOrdinal());
    }

    @Test
    public void asIsoConstant1()
    {
        ensureEqual(1, TUESDAY.asIsoOrdinal());
    }

    @Test
    public void asIsoConstant2()
    {
        ensureEqual(2, WEDNESDAY.asIsoOrdinal());
    }

    @Test
    public void asIsoConstant3()
    {
        ensureEqual(3, THURSDAY.asIsoOrdinal());
    }

    @Test
    public void asIsoConstant4()
    {
        ensureEqual(4, FRIDAY.asIsoOrdinal());
    }

    @Test
    public void asIsoConstant5()
    {
        ensureEqual(5, SATURDAY.asIsoOrdinal());
    }

    @Test
    public void asIsoConstant6()
    {
        ensureEqual(6, SUNDAY.asIsoOrdinal());
    }

    @Test
    public void forIsoConstant0()
    {
        ensureEqual(MONDAY, forIsoOrdinal(0));
    }

    @Test
    public void forIsoConstant1()
    {
        ensureEqual(TUESDAY, forIsoOrdinal(1));
    }

    @Test
    public void forIsoConstant2()
    {
        ensureEqual(WEDNESDAY, forIsoOrdinal(2));
    }

    @Test
    public void forIsoConstant3()
    {
        ensureEqual(THURSDAY, forIsoOrdinal(3));
    }

    @Test
    public void forIsoConstant4()
    {
        ensureEqual(FRIDAY, forIsoOrdinal(4));
    }

    @Test
    public void forIsoConstant5()
    {
        ensureEqual(SATURDAY, forIsoOrdinal(5));
    }

    @Test
    public void forIsoConstant6()
    {
        ensureEqual(SUNDAY, forIsoOrdinal(6));
    }

    @Test
    public void forIsoConstant7()
    {
        ensureThrows(() -> forIsoOrdinal(7));
    }

    @Test
    public void forIsoConstant8()
    {
        ensureThrows(() -> forIsoOrdinal(8));
    }

    @Test
    public void forIsoConstantMinus1()
    {
        ensureThrows(() -> forIsoOrdinal(-1));
    }

    @Test
    public void forJavaDayOfWeekFRIDAY()
    {
        ensureEqual(FRIDAY, forJavaDayOfWeek(java.time.DayOfWeek.FRIDAY));
    }

    @Test
    public void forJavaDayOfWeekMONDAY()
    {
        ensureEqual(MONDAY, forJavaDayOfWeek(java.time.DayOfWeek.MONDAY));
    }

    @Test
    public void forJavaDayOfWeekSATURDAY()
    {
        ensureEqual(SATURDAY, forJavaDayOfWeek(java.time.DayOfWeek.SATURDAY));
    }

    @Test
    public void forJavaDayOfWeekSUNDAY()
    {
        ensureEqual(SUNDAY, forJavaDayOfWeek(java.time.DayOfWeek.SUNDAY));
    }

    @Test
    public void forJavaDayOfWeekTHURSDAY()
    {
        ensureEqual(THURSDAY, forJavaDayOfWeek(java.time.DayOfWeek.THURSDAY));
    }

    @Test
    public void forJavaDayOfWeekTUESDAY()
    {
        ensureEqual(TUESDAY, forJavaDayOfWeek(java.time.DayOfWeek.TUESDAY));
    }

    @Test
    public void forJavaDayOfWeekWEDNESDAY()
    {
        ensureEqual(WEDNESDAY, forJavaDayOfWeek(java.time.DayOfWeek.WEDNESDAY));
    }

    @Test
    public void getJodaTimeConstant1()
    {
        ensureEqual(1, MONDAY.asJavaOrdinal());
    }

    @Test
    public void getJodaTimeConstant2()
    {
        ensureEqual(2, TUESDAY.asJavaOrdinal());
    }

    @Test
    public void getJodaTimeConstant3()
    {
        ensureEqual(3, WEDNESDAY.asJavaOrdinal());
    }

    @Test
    public void getJodaTimeConstant4()
    {
        ensureEqual(4, THURSDAY.asJavaOrdinal());
    }

    @Test
    public void getJodaTimeConstant5()
    {
        ensureEqual(5, FRIDAY.asJavaOrdinal());
    }

    @Test
    public void getJodaTimeConstant6()
    {
        ensureEqual(6, SATURDAY.asJavaOrdinal());
    }

    @Test
    public void getJodaTimeConstant7()
    {
        ensureEqual(7, SUNDAY.asJavaOrdinal());
    }
}
