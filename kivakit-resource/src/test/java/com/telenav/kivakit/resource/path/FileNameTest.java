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

package com.telenav.kivakit.resource.path;

import com.telenav.kivakit.conversion.core.time.LocalDateTimeWithMillisecondsConverter;
import com.telenav.kivakit.conversion.core.time.LocalDateTimeWithSecondsConverter;
import com.telenav.kivakit.core.messaging.listeners.ThrowingListener;
import com.telenav.kivakit.test.UnitTest;
import com.telenav.kivakit.core.time.DayOfWeek;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.time.Time;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZoneId;

@SuppressWarnings("ConstantConditions")
public class FileNameTest extends UnitTest
{
    @Test
    public void localTimeTest()
    {
        var now = Time.now();
        var local = now.localTime();
        trace("local = ${debug}", local);
        var utc = now.utc();
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

        var localMillisecondsConverter = new LocalDateTimeWithMillisecondsConverter(this);
        var timeRepresentation = localMillisecondsConverter.unconvert(localTime);
        trace("Time Representation: ${debug}", timeRepresentation);

        trace("Hour of day:     ${debug}", localTime.hourOfDay());
        Assert.assertEquals(13, localTime.hourOfDay());
        trace("Minutes of day:  ${debug}", localTime.minuteOfDay());
        Assert.assertEquals(801, localTime.minuteOfDay());
        trace("Minutes of Hour: ${debug}", localTime.minuteOfHour());
        Assert.assertEquals(21, localTime.minuteOfHour());
        trace("Day of Year:    ${debug}", localTime.dayOfYear());
        Assert.assertEquals(216, localTime.dayOfYear());
        trace("Week of Year:    ${debug}", localTime.weekOfYear());
        Assert.assertEquals(30, localTime.weekOfYear());
        trace("Day of Week:    ${debug}", localTime.dayOfWeek());
        Assert.assertEquals(DayOfWeek.FRIDAY, localTime.dayOfWeek());
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

        final Time time = localTime.utc();
        trace("Time:      ${debug}", time.asMilliseconds());

        /*
          Converters
         */
        var millisecondsConverter = new LocalDateTimeWithMillisecondsConverter(new ThrowingListener());
        var secondsConverter = new LocalDateTimeWithSecondsConverter(new ThrowingListener());

        String timeRepresentation;

        /*
          Test the local milliseconds
         */
        timeRepresentation = millisecondsConverter.unconvert(localTime);
        trace("Time Representation: ${debug}", timeRepresentation);
        ensure(timeRepresentation.matches("2012\\.08\\.03_1\\.21\\.21\\.123PM.PT"));

        /*
          Test the local seconds
         */
        timeRepresentation = secondsConverter.unconvert(localTime);
        trace("Time Representation: ${debug}", timeRepresentation);
        ensure(timeRepresentation.matches("2012\\.08\\.03_1\\.21\\.21PM.PT"));

        /*
          Test the GMT seconds
         */
        timeRepresentation = secondsConverter.unconvert(localTime);
        trace("Time Representation: ${debug}", timeRepresentation);
        ensure(timeRepresentation.matches("2012\\.08\\.03_[0-9]+\\.21\\.21PM.PT"));
    }
}
