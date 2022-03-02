package com.telenav.kivakit.conversion.core.language.primitive;

import com.telenav.kivakit.conversion.core.language.primitive.BooleanConverter;
import com.telenav.kivakit.conversion.core.language.primitive.DoubleConverter;
import com.telenav.kivakit.conversion.core.language.primitive.FloatConverter;
import com.telenav.kivakit.conversion.core.language.primitive.FormattedDoubleConverter;
import com.telenav.kivakit.conversion.core.language.primitive.FormattedIntegerConverter;
import com.telenav.kivakit.conversion.core.language.primitive.FormattedLongConverter;
import com.telenav.kivakit.conversion.core.language.primitive.HexadecimalLongConverter;
import com.telenav.kivakit.conversion.core.language.primitive.IntegerConverter;
import com.telenav.kivakit.conversion.core.language.primitive.LongConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class PrimitiveConverterTest extends UnitTest
{
    @Test
    public void testPrimitiveConverters()
    {
        ensureEqual(true, new BooleanConverter(Listener.none()).convert("true"));
        ensureEqual(false, new BooleanConverter(Listener.none()).convert("false"));
        ensureEqual(3.1415926, new DoubleConverter(Listener.none()).convert("3.1415926"));
        ensureEqual(3.1415926f, new FloatConverter(Listener.none()).convert("3.1415926f"));
        ensureEqual(31415926, new IntegerConverter(Listener.none()).convert("31415926"));
        ensureEqual(31415926L, new LongConverter(Listener.none()).convert("31415926"));
        ensureEqual(0x31415926L, new HexadecimalLongConverter(Listener.none()).convert("0x31415926"));
        ensureEqual(31415926L, new FormattedLongConverter(Listener.none()).convert("31,415,926"));
        ensureEqual(31415926, new FormattedIntegerConverter(Listener.none()).convert("31,415,926"));
        ensureEqual(314159.26, new FormattedDoubleConverter(Listener.none()).convert("314159.26"));
    }
}
