package com.telenav.kivakit.network.core.converters;

import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class HttpDateTimeConverterTest extends UnitTest
{
    @Test
    public void testConvert()
    {
        var converter = new HttpDateTimeConverter(Listener.none());
        var time = converter.convert("Mon, 19 Jul 2021 13:05:31 GMT");

        var calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(Calendar.YEAR, 2021);
        calendar.set(Calendar.MONTH, 6);
        calendar.set(Calendar.DAY_OF_MONTH, 19);
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND, 31);
        calendar.set(Calendar.MILLISECOND, 0);

        assertEquals(calendar.getTimeInMillis(), time.asMilliseconds());
    }
}
