package com.telenav.kivakit.network.http;

import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static com.telenav.kivakit.core.messaging.Listener.nullListener;
import static org.junit.Assert.assertEquals;

public class HttpDateTimeConverterTest extends UnitTest
{
    @Test
    public void testConvert()
    {
        var converter = new HttpDateTimeConverter(nullListener());
        var time = converter.convert("Mon, 19 Jul 2021 13:05:31 GMT");

        var calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.set(Calendar.YEAR, 2021);
        calendar.set(Calendar.MONTH, 6);
        calendar.set(Calendar.DAY_OF_MONTH, 19);
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND, 31);
        calendar.set(Calendar.MILLISECOND, 0);

        assert time != null;
        assertEquals(calendar.getTimeInMillis(), time.milliseconds());
    }
}
