////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.primitive;

import com.telenav.kivakit.kernel.language.primitives.Ints;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

/**
 * @author jonathanl (shibo)
 */
public class IntsTest
{
    @Test
    public void testParse()
    {
        for (int i = -100_000; i < 100_000; i++)
        {
            ensureEqual(Ints.parse("" + i, -1), i);
        }
        ensureEqual(Ints.parse(null, -1), -1);
        ensureEqual(Ints.parse("", -1), -1);
        ensureEqual(Ints.parse("99", -1), 99);
        ensureEqual(Ints.parse("99", -1), 99);
        ensureEqual(Ints.parse("abc", -1), -1);

        ensureEqual(Ints.parseNaturalNumber(null), Ints.INVALID);
        ensureEqual(Ints.parseNaturalNumber(""), Ints.INVALID);
        ensureEqual(Ints.parseNaturalNumber("123"), 123);
        ensureEqual(Ints.parseNaturalNumber("-123"), Ints.INVALID);
    }
}
