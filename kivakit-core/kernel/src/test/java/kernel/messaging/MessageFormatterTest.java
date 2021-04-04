////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.messaging;

import com.telenav.kivakit.core.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.core.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter;
import org.junit.Assert;
import org.junit.Test;

public class MessageFormatterTest
{
    private static class Bean implements AsString
    {
        @KivaKitIncludeProperty
        final
        int x = 5;

        @Override
        public String asString(final StringFormat ignored)
        {
            return "*" + x + "*";
        }
    }

    @Test
    public void test()
    {
        Assert.assertEquals("$", format("$$"));
        Assert.assertEquals("$100 worth of ice cream", format("$$100 worth of $", "ice cream"));
        Assert.assertEquals("x = 9, y = 10", format("x = $, y = $", 9, 10));
        Assert.assertEquals("5 = hello", format("${a} = ${b}", new VariableMap<>().add("a", 5).add("b", "hello")));
        Assert.assertEquals("5", format("${debug}", 5));
        Assert.assertEquals("5", format("$", 5));
        Assert.assertEquals(" 5", format(" ${debug}", 5));
        Assert.assertEquals("5 ", format("${debug} ", 5));
        Assert.assertEquals(" 5 7 ", format(" ${debug} ${debug} ", 5, 7));
        Assert.assertEquals("MessageFormatterTest", format("${class}", MessageFormatterTest.class));
        Assert.assertEquals("enabled", format("${flag}", true));
        Assert.assertEquals("disabled", format("${flag}", false));
        Assert.assertEquals("[MessageFormatterTest.Bean x = 5]",
                format("${object}", new Bean()).replaceAll("≡\\d+ ", ""));
        Assert.assertEquals("*5*", format("${debug}", new Bean()));
    }

    private String format(final String message, final Object... arguments)
    {
        return new MessageFormatter().formatArray(message, arguments);
    }
}
