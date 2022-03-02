////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.messaging;

import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.interfaces.string.Stringable;
import org.junit.Assert;
import org.junit.Test;

public class MessageFormatterTest extends UnitTest
{
    private static class Bean implements Stringable
    {
        @KivaKitIncludeProperty
        final
        int x = 5;

        @Override
        public String asString(Format ignored)
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

    private String format(String message, Object... arguments)
    {
        return Formatter.formatArray(message, arguments);
    }
}
