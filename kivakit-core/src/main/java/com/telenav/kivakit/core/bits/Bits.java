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

package com.telenav.kivakit.core.bits;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramBits;
import com.telenav.kivakit.core.value.count.Countable;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Utility methods for manipulating bits.
 *
 * <p><b>Functions</b></p>
 *
 * <ul>
 *     <li>{@link #oneBits(Countable)} - Composes a long value with the given number of one bits</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramBits.class)
@LexakaiJavadoc(complete = true)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class Bits
{
    /**
     * @return A long value containting the given number of one bits, starting from the least-significant bit.
     * For example, if count is 5, the return value would be 0b11111.
     */
    public static long oneBits(Countable count)
    {
        var one = 1L;
        var value = 0L;
        for (var i = 0L; i < count.count().asLong(); i++)
        {
            value |= one;
            one <<= 1;
        }
        return value;
    }
}
