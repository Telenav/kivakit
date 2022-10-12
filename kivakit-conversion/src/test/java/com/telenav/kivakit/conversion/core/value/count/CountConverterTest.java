package com.telenav.kivakit.conversion.core.value.count;

import com.telenav.kivakit.conversion.core.value.CountConverter;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static java.util.Objects.requireNonNull;

public class CountConverterTest extends CoreUnitTest
{
    @Test
    public void testCountConverter()
    {
        var converter = new CountConverter(this);
        ensureEqual(1000, requireNonNull(converter.convert("1,000")).asInt());
    }
}
