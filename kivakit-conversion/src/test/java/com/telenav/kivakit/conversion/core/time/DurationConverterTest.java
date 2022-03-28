package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.test.CoreUnitTest;
import com.telenav.kivakit.core.time.Duration;
import org.junit.Test;

public class DurationConverterTest extends CoreUnitTest
{
    @Test
    public void testConvert()
    {
        var converter = new DurationConverter(this);

        ensureEqual(converter.convert("5 days"), Duration.days(5));
        ensureEqual(converter.convert("1 day"), Duration.days(1));
        ensureEqual(converter.convert("5d"), Duration.days(5));

        ensureEqual(converter.convert("5 hours"), Duration.hours(5));
        ensureEqual(converter.convert("1 hour"), Duration.hours(1));
        ensureEqual(converter.convert("5h"), Duration.hours(5));

        ensureEqual(converter.convert("5 minutes"), Duration.minutes(5));
        ensureEqual(converter.convert("1 minute"), Duration.minutes(1));
        ensureEqual(converter.convert("5m"), Duration.minutes(5));

        ensureEqual(converter.convert("5 seconds"), Duration.seconds(5));
        ensureEqual(converter.convert("1 second"), Duration.seconds(1));
        ensureEqual(converter.convert("5s"), Duration.seconds(5));

        ensureEqual(converter.convert("5 milliseconds"), Duration.milliseconds(5));
        ensureEqual(converter.convert("1 millisecond"), Duration.milliseconds(1));
        ensureEqual(converter.convert("5ms"), Duration.milliseconds(5));
    }

    @Test
    public void testSecondsConverter()
    {
        ensureEqual(Duration.seconds(5), new SecondsConverter(Listener.none()).convert("5"));
    }
}
