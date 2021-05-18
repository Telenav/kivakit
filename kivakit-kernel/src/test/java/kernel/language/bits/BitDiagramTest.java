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

package kernel.language.bits;

import com.telenav.kivakit.kernel.language.bits.BitDiagram;
import com.telenav.kivakit.kernel.language.bits.BitDiagram.BitField;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class BitDiagramTest
{
    private final BitDiagram DIAGRAM = new BitDiagram("AAAABBBBC");

    private final BitField A = DIAGRAM.field('A');

    private final BitField B = DIAGRAM.field('B');

    private final BitField C = DIAGRAM.field('C');

    @Test
    public void testExtract()
    {
        final var value = binary("100110011");

        ensureEqual(9, A.extractInt(value));
        ensureEqual(9, B.extractInt(value));
        ensureEqual(true, C.extractBoolean(value));
    }

    @Test
    public void testSet()
    {
        final var value = binary("000010010");
        final var field = binary("0110");
        final var result = binary("000001100");

        ensureEqual(result, B.set(value, field));
    }

    private int binary(final String value)
    {
        return Integer.parseInt(value, 2);
    }
}
