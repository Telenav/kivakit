package com.telenav.kivakit.conversion.core.time.epoch;

import com.telenav.kivakit.core.time.Time;
import org.junit.Test;

import static com.telenav.kivakit.core.ensure.Ensure.ensureEqual;

public class EpochTimeConverterTest
{
    @Test

    public void test()
    {
        var converter = new EpochTimeConverter();
        var now = Time.now();
        converter.convert("" + now.epochMilliseconds());
        ensureEqual(converter.unconvert(now), "" + now.epochMilliseconds());
    }
}
