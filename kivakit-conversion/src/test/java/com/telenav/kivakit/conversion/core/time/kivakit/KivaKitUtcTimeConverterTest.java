package com.telenav.kivakit.conversion.core.time.kivakit;

import com.telenav.kivakit.conversion.core.time.kivakit.KivaKitUtcTimeConverter;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

public class KivaKitUtcTimeConverterTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        // yyyy.MM.dd_h.mma
        var converter = new KivaKitUtcTimeConverter();
        var input = "2023.01.19_06.07AM";
        var time = converter.convert(input);
        ensureEqual(converter.unconvert(time), input);
    }
}
