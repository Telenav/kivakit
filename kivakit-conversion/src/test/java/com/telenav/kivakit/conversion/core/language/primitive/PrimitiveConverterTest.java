package com.telenav.kivakit.conversion.core.language.primitive;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.test.support.CoreUnitTest;
import org.junit.Test;

public class PrimitiveConverterTest extends CoreUnitTest
{
    @Test
    public void testPrimitiveConverters()
    {
        ensureEqual(true, new BooleanConverter(Listener.emptyListener()).convert("true"));
        ensureEqual(false, new BooleanConverter(Listener.emptyListener()).convert("false"));
        ensureEqual(3.1415926, new DoubleConverter(Listener.emptyListener()).convert("3.1415926"));
        ensureEqual(3.1415926f, new FloatConverter(Listener.emptyListener()).convert("3.1415926f"));
        ensureEqual(31415926, new IntegerConverter(Listener.emptyListener()).convert("31415926"));
        ensureEqual(31415926L, new LongConverter(Listener.emptyListener()).convert("31415926"));
        ensureEqual(0x31415926L, new HexadecimalLongConverter(Listener.emptyListener()).convert("0x31415926"));
        ensureEqual(31415926L, new FormattedLongConverter(Listener.emptyListener()).convert("31,415,926"));
        ensureEqual(31415926, new FormattedIntegerConverter(Listener.emptyListener()).convert("31,415,926"));
        ensureEqual(314159.26, new FormattedDoubleConverter(Listener.emptyListener()).convert("314159.26"));
    }
}
