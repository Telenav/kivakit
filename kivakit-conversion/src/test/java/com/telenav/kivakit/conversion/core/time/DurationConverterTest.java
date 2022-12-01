package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.messaging.Listener.nullListener;
import static com.telenav.kivakit.core.time.Duration.days;
import static com.telenav.kivakit.core.time.Duration.hours;
import static com.telenav.kivakit.core.time.Duration.milliseconds;
import static com.telenav.kivakit.core.time.Duration.minutes;
import static com.telenav.kivakit.core.time.Duration.seconds;

public class DurationConverterTest extends CoreUnitTest
{
    @Test
    public void testConvert()
    {
        var converter = new DurationConverter(this);

        ensureEqual(converter.convert("5 days"), days(5));
        ensureEqual(converter.convert("1 day"), days(1));
        ensureEqual(converter.convert("5d"), days(5));

        ensureEqual(converter.convert("5 hours"), hours(5));
        ensureEqual(converter.convert("1 hour"), hours(1));
        ensureEqual(converter.convert("5h"), hours(5));

        ensureEqual(converter.convert("5 minutes"), minutes(5));
        ensureEqual(converter.convert("1 minute"), minutes(1));
        ensureEqual(converter.convert("5m"), minutes(5));

        ensureEqual(converter.convert("5 seconds"), seconds(5));
        ensureEqual(converter.convert("1 second"), seconds(1));
        ensureEqual(converter.convert("5s"), seconds(5));

        ensureEqual(converter.convert("5 milliseconds"), milliseconds(5));
        ensureEqual(converter.convert("1 millisecond"), milliseconds(1));
        ensureEqual(converter.convert("5ms"), milliseconds(5));
    }

    @Test
    public void testSecondsConverter()
    {
        ensureEqual(seconds(5), new SecondsConverter(nullListener()).convert("5"));
    }
}
