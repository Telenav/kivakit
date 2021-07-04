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

package kernel.time;

import com.telenav.kivakit.kernel.data.validation.ensure.reporters.ValidationFailure;
import com.telenav.kivakit.kernel.language.time.DayOfWeek;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

/**
 * Created by bogdantnv on 12/07/16.
 */
public class DayOfWeekTest
{
    @Test
    public void asIsoConstant0()
    {
        ensureEqual(0, DayOfWeek.MONDAY.asIsoConstant());
    }

    @Test
    public void asIsoConstant1()
    {
        ensureEqual(1, DayOfWeek.TUESDAY.asIsoConstant());
    }

    @Test
    public void asIsoConstant2()
    {
        ensureEqual(2, DayOfWeek.WEDNESDAY.asIsoConstant());
    }

    @Test
    public void asIsoConstant3()
    {
        ensureEqual(3, DayOfWeek.THURSDAY.asIsoConstant());
    }

    @Test
    public void asIsoConstant4()
    {
        ensureEqual(4, DayOfWeek.FRIDAY.asIsoConstant());
    }

    @Test
    public void asIsoConstant5()
    {
        ensureEqual(5, DayOfWeek.SATURDAY.asIsoConstant());
    }

    @Test
    public void asIsoConstant6()
    {
        ensureEqual(6, DayOfWeek.SUNDAY.asIsoConstant());
    }

    @Test
    public void forIsoConstant0()
    {
        ensureEqual(DayOfWeek.MONDAY, DayOfWeek.forIsoConstant(0));
    }

    @Test
    public void forIsoConstant1()
    {
        ensureEqual(DayOfWeek.TUESDAY, DayOfWeek.forIsoConstant(1));
    }

    @Test
    public void forIsoConstant2()
    {
        ensureEqual(DayOfWeek.WEDNESDAY, DayOfWeek.forIsoConstant(2));
    }

    @Test
    public void forIsoConstant3()
    {
        ensureEqual(DayOfWeek.THURSDAY, DayOfWeek.forIsoConstant(3));
    }

    @Test
    public void forIsoConstant4()
    {
        ensureEqual(DayOfWeek.FRIDAY, DayOfWeek.forIsoConstant(4));
    }

    @Test
    public void forIsoConstant5()
    {
        ensureEqual(DayOfWeek.SATURDAY, DayOfWeek.forIsoConstant(5));
    }

    @Test
    public void forIsoConstant6()
    {
        ensureEqual(DayOfWeek.SUNDAY, DayOfWeek.forIsoConstant(6));
    }

    @Test(expected = ValidationFailure.class)
    public void forIsoConstant7()
    {
        DayOfWeek.forIsoConstant(7);
    }

    @Test(expected = ValidationFailure.class)
    public void forIsoConstant8()
    {
        DayOfWeek.forIsoConstant(8);
    }

    @Test(expected = ValidationFailure.class)
    public void forIsoConstantMinus1()
    {
        DayOfWeek.forIsoConstant(-1);
    }

    @Test
    public void forJavaDayOfWeekFRIDAY()
    {
        ensureEqual(DayOfWeek.FRIDAY, DayOfWeek.forJavaDayOfWeek(java.time.DayOfWeek.FRIDAY));
    }

    @Test
    public void forJavaDayOfWeekMONDAY()
    {
        ensureEqual(DayOfWeek.MONDAY, DayOfWeek.forJavaDayOfWeek(java.time.DayOfWeek.MONDAY));
    }

    @Test
    public void forJavaDayOfWeekSATURDAY()
    {
        ensureEqual(DayOfWeek.SATURDAY, DayOfWeek.forJavaDayOfWeek(java.time.DayOfWeek.SATURDAY));
    }

    @Test
    public void forJavaDayOfWeekSUNDAY()
    {
        ensureEqual(DayOfWeek.SUNDAY, DayOfWeek.forJavaDayOfWeek(java.time.DayOfWeek.SUNDAY));
    }

    @Test
    public void forJavaDayOfWeekTHURSDAY()
    {
        ensureEqual(DayOfWeek.THURSDAY, DayOfWeek.forJavaDayOfWeek(java.time.DayOfWeek.THURSDAY));
    }

    @Test
    public void forJavaDayOfWeekTUESDAY()
    {
        ensureEqual(DayOfWeek.TUESDAY, DayOfWeek.forJavaDayOfWeek(java.time.DayOfWeek.TUESDAY));
    }

    @Test
    public void forJavaDayOfWeekWEDNESDAY()
    {
        ensureEqual(DayOfWeek.WEDNESDAY, DayOfWeek.forJavaDayOfWeek(java.time.DayOfWeek.WEDNESDAY));
    }

    @Test
    public void getJodaTimeConstant1()
    {
        ensureEqual(1, DayOfWeek.MONDAY.jodaTimeConstant());
    }

    @Test
    public void getJodaTimeConstant2()
    {
        ensureEqual(2, DayOfWeek.TUESDAY.jodaTimeConstant());
    }

    @Test
    public void getJodaTimeConstant3()
    {
        ensureEqual(3, DayOfWeek.WEDNESDAY.jodaTimeConstant());
    }

    @Test
    public void getJodaTimeConstant4()
    {
        ensureEqual(4, DayOfWeek.THURSDAY.jodaTimeConstant());
    }

    @Test
    public void getJodaTimeConstant5()
    {
        ensureEqual(5, DayOfWeek.FRIDAY.jodaTimeConstant());
    }

    @Test
    public void getJodaTimeConstant6()
    {
        ensureEqual(6, DayOfWeek.SATURDAY.jodaTimeConstant());
    }

    @Test
    public void getJodaTimeConstant7()
    {
        ensureEqual(7, DayOfWeek.SUNDAY.jodaTimeConstant());
    }
}
