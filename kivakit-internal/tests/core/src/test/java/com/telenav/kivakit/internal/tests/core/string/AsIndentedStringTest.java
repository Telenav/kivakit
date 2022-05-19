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

package com.telenav.kivakit.internal.tests.core.string;

import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;import com.telenav.kivakit.core.string.AsIndentedString;
import com.telenav.kivakit.core.string.AsStringIndenter;
import com.telenav.kivakit.internal.test.support.CoreUnitTest;
import com.telenav.kivakit.core.value.count.Maximum;
import org.junit.Test;

/**
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class AsIndentedStringTest extends CoreUnitTest implements AsIndentedString
{
    static class Bar implements AsIndentedString
    {
        @KivaKitIncludeProperty
        private final int y = 5;

        @KivaKitIncludeProperty
        private final int z = 7;

        @Override
        public String toString()
        {
            return "Bar";
        }
    }

    static class Foo implements AsIndentedString
    {
        @KivaKitIncludeProperty
        private final Bar bar = new Bar();

        @KivaKitIncludeProperty
        private final int y = 1;

        @KivaKitIncludeProperty
        private final int z = 3;

        @Override
        public String toString()
        {
            return "Foo";
        }
    }

    @KivaKitIncludeProperty
    private final Foo foo = new Foo();

    @KivaKitIncludeProperty
    private final int x = 9;

    @Test
    public void test()
    {
        var foo = new Foo();
        ensureEqual(
                "bar:\n" +
                        "  y: 5\n" +
                        "  z: 7\n" +
                        "y: 1\n" +
                        "z: 3", foo.asString());
    }

    @Test
    public void testPrune()
    {
        var indenter = new AsStringIndenter(Format.TEXT)
                .levels(Maximum._4)
                .pruneAt(Bar.class);
        asString(Format.TEXT, indenter);
        ensureEqual("foo:\n" +
                "  bar: Bar\n" +
                "  y: 1\n" +
                "  z: 3\n" +
                "x: 9", indenter.toString());
    }
}
