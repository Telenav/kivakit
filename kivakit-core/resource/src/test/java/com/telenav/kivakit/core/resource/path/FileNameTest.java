////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.path;

import com.telenav.kivakit.core.kernel.language.time.DayOfWeek;
import com.telenav.kivakit.core.kernel.language.time.LocalTime;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.time.conversion.converters.LocalDateTimeWithMillisecondsConverter;
import com.telenav.kivakit.core.kernel.language.time.conversion.converters.LocalDateTimeWithSecondsConverter;
import com.telenav.kivakit.core.kernel.messaging.listeners.ThrowingListener;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZoneId;

@SuppressWarnings("ConstantConditions")
public class FileNameTest extends UnitTest
{
    @Test
    public void localTimeTest()
    {
        final var now = Time.now();
        final var local = now.localTime();
        trace("local = ${debug}", local);
        final var utc = now.utc();
        trace("utx = ${debug}", utc);
        ensureEqual(local.asMilliseconds(), utc.asMilliseconds());
    }

    @Test
    public void timeFieldsTest()
    {
        final var timeZone = ZoneId.of("America/Los_Angeles");
        trace("TimeZone:  ${debug}", timeZone.toString());

        final var localTime = LocalTime.milliseconds(timeZone, 1344025281123L);
        trace("LocalTime: ${debug}", localTime.asMilliseconds());

        final var localMillisecondsConverter = new LocalDateTimeWithMillisecondsConverter(this);
        final var timeRepresentation = localMillisecondsConverter.toString(localTime);
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
        final var timeZone = ZoneId.of("America/Los_Angeles");
        trace("TimeZone:  ${debug}", timeZone.toString());

        final var localTime = LocalTime.milliseconds(timeZone, 1344025281123L);
        trace("LocalTime: ${debug}", localTime.asMilliseconds());

        final Time time = localTime.utc();
        trace("Time:      ${debug}", time.asMilliseconds());

        /*
          Converters
         */
        final var millisecondsConverter = new LocalDateTimeWithMillisecondsConverter(new ThrowingListener());
        final var secondsConverter = new LocalDateTimeWithSecondsConverter(new ThrowingListener());

        String timeRepresentation;

        /*
          Test the local milliseconds
         */
        timeRepresentation = millisecondsConverter.toString(localTime);
        trace("Time Representation: ${debug}", timeRepresentation);
        ensure(timeRepresentation.matches("2012\\.08\\.03_1\\.21\\.21\\.123PM.PT"));

        /*
          Test the local seconds
         */
        timeRepresentation = secondsConverter.toString(localTime);
        trace("Time Representation: ${debug}", timeRepresentation);
        ensure(timeRepresentation.matches("2012\\.08\\.03_1\\.21\\.21PM.PT"));

        /*
          Test the GMT seconds
         */
        timeRepresentation = secondsConverter.toString(localTime);
        trace("Time Representation: ${debug}", timeRepresentation);
        ensure(timeRepresentation.matches("2012\\.08\\.03_[0-9]+\\.21\\.21PM.PT"));
    }
}
