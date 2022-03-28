package com.telenav.kivakit.conversion.core.value.count;

import com.telenav.kivakit.conversion.core.value.CountConverter;
import com.telenav.kivakit.core.test.CoreUnitTest;
import org.junit.Test;

import java.util.Objects;

public class CountConverterTest extends CoreUnitTest
{
    @Test
    public void testCountConverter()
    {
        var converter = new CountConverter(this);
        ensureEqual(1000, Objects.requireNonNull(converter.convert("1,000")).asInt());
    }
}
