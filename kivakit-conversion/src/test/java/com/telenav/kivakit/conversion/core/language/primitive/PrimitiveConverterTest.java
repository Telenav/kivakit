package com.telenav.kivakit.conversion.core.language.primitive;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

public class PrimitiveConverterTest extends CoreUnitTest
{
    @Test
    public void testPrimitiveConverters()
    {
        ensureEqual(true, new BooleanConverter(Listener.nullListener()).convert("true"));
        ensureEqual(false, new BooleanConverter(Listener.nullListener()).convert("false"));
        ensureEqual(3.1415926, new DoubleConverter(Listener.nullListener()).convert("3.1415926"));
        ensureEqual(3.1415926f, new FloatConverter(Listener.nullListener()).convert("3.1415926f"));
        ensureEqual(31415926, new IntegerConverter(Listener.nullListener()).convert("31415926"));
        ensureEqual(31415926L, new LongConverter(Listener.nullListener()).convert("31415926"));
        ensureEqual(0x31415926L, new HexadecimalLongConverter(Listener.nullListener()).convert("0x31415926"));
        ensureEqual(31415926L, new FormattedLongConverter(Listener.nullListener()).convert("31,415,926"));
        ensureEqual(31415926, new FormattedIntegerConverter(Listener.nullListener()).convert("31,415,926"));
        ensureEqual(314159.26, new FormattedDoubleConverter(Listener.nullListener()).convert("314159.26"));
    }
}
