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

package com.telenav.kivakit.internal.tests.core.bits;

import com.telenav.kivakit.core.bits.BitDiagram;
import com.telenav.kivakit.core.bits.BitDiagram.BitField;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static java.lang.Integer.parseInt;

/**
 * Test access to bit fields.
 *
 * @author jonathanl (shibo)
 */
public class BitDiagramTest extends CoreUnitTest
{
    @SuppressWarnings("SpellCheckingInspection")
    private final BitDiagram DIAGRAM = new BitDiagram("AAAA BBBB ?? C");

    private final BitField A = DIAGRAM.field('A');

    private final BitField B = DIAGRAM.field('B');

    private final BitField C = DIAGRAM.field('C');

    @Test
    public void testGet()
    {
                                    // AAAABBBB??C
        var value = binary("10011001001");

        ensureEqual(9, A.getInt(value));
        ensureEqual(9, B.getInt(value));
        ensureEqual(true, C.getBoolean(value));
    }

    @Test
    public void testSet()
    {
                                    // AAAABBBB??C
        var value = binary("00001001000");
        var field = binary("0110");
                                     // AAAABBBB??C
        var result = binary("00000110000");

        ensureEqual(result, B.set(value, field));
    }

    private int binary(String value)
    {
        return parseInt(value, 2);
    }
}
