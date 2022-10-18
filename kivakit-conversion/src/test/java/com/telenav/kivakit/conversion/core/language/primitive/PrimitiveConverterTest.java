package com.telenav.kivakit.conversion.core.language.primitive;

import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.messaging.Listener.nullListener;

public class PrimitiveConverterTest extends CoreUnitTest
{
    @Test
    public void testPrimitiveConverters()
    {
        ensureEqual(true, new BooleanConverter(nullListener()).convert("true"));
        ensureEqual(false, new BooleanConverter(nullListener()).convert("false"));
        ensureEqual(3.1415926, new DoubleConverter(nullListener()).convert("3.1415926"));
        ensureEqual(3.1415926f, new FloatConverter(nullListener()).convert("3.1415926f"));
        ensureEqual(31415926, new IntegerConverter(nullListener()).convert("31415926"));
        ensureEqual(31415926L, new LongConverter(nullListener()).convert("31415926"));
        ensureEqual(0x31415926L, new HexadecimalLongConverter(nullListener()).convert("0x31415926"));
        ensureEqual(31415926L, new FormattedLongConverter(nullListener()).convert("31,415,926"));
        ensureEqual(31415926, new FormattedIntegerConverter(nullListener()).convert("31,415,926"));
        ensureEqual(314159.26, new FormattedDoubleConverter(nullListener()).convert("314159.26"));
    }
}
