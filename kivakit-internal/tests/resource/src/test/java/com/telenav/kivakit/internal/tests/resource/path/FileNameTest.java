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

package com.telenav.kivakit.internal.tests.resource.path;

import com.telenav.kivakit.conversion.core.time.kivakit.KivaKitLocalDateTimeWithMillisecondsConverter;
import com.telenav.kivakit.conversion.core.time.kivakit.KivaKitLocalDateTimeWithSecondsConverter;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.testing.UnitTest;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZoneId;

import static com.telenav.kivakit.core.time.DayOfWeek.FRIDAY;
import static com.telenav.kivakit.core.time.Time.now;

public class FileNameTest extends UnitTest
{
    @Test
    public void localTimeTest()
    {
        var now = now();
        var local = now.asLocalTime();
        trace("local = ${debug}", local);
        var utc = now.asUtc();
        trace("utx = ${debug}", utc);
        ensureEqual(local.asMilliseconds(), utc.asMilliseconds());
    }

    @Test
    public void timeFieldsTest()
    {
        var timeZone = ZoneId.of("America/Los_Angeles");
        trace("TimeZone:  ${debug}", timeZone.toString());

        var localTime = LocalTime.milliseconds(timeZone, 1344025281123L);
        trace("LocalTime: ${debug}", localTime.asMilliseconds());

        var localMillisecondsConverter = new KivaKitLocalDateTimeWithMillisecondsConverter(this);
        var timeRepresentation = localMillisecondsConverter.unconvert(localTime);
        trace("Time Representation: ${debug}", timeRepresentation);

        trace("Hour of day:     ${debug}", localTime.hourOfDay());
        Assert.assertEquals(13, localTime.hourOfDay().asMilitaryHour());
        trace("Minutes of day:  ${debug}", localTime.minuteOfDay());
        Assert.assertEquals(801, localTime.minuteOfDay());
        trace("Minutes of Hour: ${debug}", localTime.minuteOfHour());
        Assert.assertEquals(21, localTime.minuteOfHour().asUnits());
        trace("Day of Year:    ${debug}", localTime.dayOfYear());
        Assert.assertEquals(216, localTime.dayOfYear().asUnits());
        trace("Week of Year:    ${debug}", localTime.weekOfYear());
        Assert.assertEquals(30, localTime.weekOfYear());
        trace("Day of Week:    ${debug}", localTime.dayOfWeek());
        Assert.assertEquals(FRIDAY, localTime.dayOfWeek());
    }

    @Test
    public void timeZoneTest()
    {
        /*
          Test time
         */
        var timeZone = ZoneId.of("America/Los_Angeles");
        trace("TimeZone:  ${debug}", timeZone.toString());

        var localTime = LocalTime.milliseconds(timeZone, 1344025281123L);
        trace("LocalTime: ${debug}", localTime.asMilliseconds());

        Time time = localTime.asUtc();
        trace("Time:      ${debug}", time.asMilliseconds());

        /*
          Converters
         */
        var millisecondsConverter =
            new KivaKitLocalDateTimeWithMillisecondsConverter(timeZone);
        var secondsConverter =
            new KivaKitLocalDateTimeWithSecondsConverter(timeZone);

        String timeRepresentation;

        /*
          Test the local milliseconds
         */
        timeRepresentation = millisecondsConverter.unconvert(localTime);
        trace("Time Representation: ${debug}", timeRepresentation);
        ensure(timeRepresentation.matches("2012\\.08\\.03_1\\.21\\.21\\.123PM.PDT"));

        /*
          Test the local seconds
         */
        timeRepresentation = secondsConverter.unconvert(localTime);
        trace("Time Representation: ${debug}", timeRepresentation);
        ensure(timeRepresentation.matches("2012\\.08\\.03_1\\.21\\.21PM.PDT"));

        /*
          Test the GMT seconds
         */
        timeRepresentation = secondsConverter.unconvert(localTime);
        trace("Time Representation: ${debug}", timeRepresentation);
        ensure(timeRepresentation.matches("2012\\.08\\.03_[0-9]+\\.21\\.21PM.PDT"));
    }
}
