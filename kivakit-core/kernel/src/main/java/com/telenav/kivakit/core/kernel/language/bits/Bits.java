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

package com.telenav.kivakit.core.kernel.language.bits;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageBits;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.values.count.Count;

/**
 * Utility methods for manipulating bits
 */
@UmlClassDiagram(diagram = DiagramLanguageBits.class)
public class Bits
{
    /**
     * @return The given number of one bits
     */
    public static long oneBits(final Count count)
    {
        var one = 1L;
        var value = 0L;
        for (var i = 0L; i < count.asInt(); i++)
        {
            value |= one;
            one <<= 1;
        }
        return value;
    }
}
