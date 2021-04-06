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

package com.telenav.kivakit.core.collections.primitive.array.packed;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArray;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.values.count.BitCount;

/**
 * A bit-packed array of primitive values
 *
 * @author jonathanl (shibo)
 * @see PackedArray
 * @see SplitPackedArray
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArray.class)
public interface PackedPrimitiveArray
{
    /**
     * OverflowHandling handling specifier
     */
    enum OverflowHandling
    {
        /** Allow overflow to happen */
        ALLOW_OVERFLOW,

        /** Limit values to the maximum value for the number of bits in this array */
        NO_OVERFLOW
    }

    /**
     * @param bits The number of bits of storage per element in the array
     * @param overflow How to handle values that are too large to be stored in the given number of bits
     * @return The array itself
     */
    PackedPrimitiveArray bits(BitCount bits, OverflowHandling overflow);

    /**
     * @return The number of bits per element in this packed array
     */
    BitCount bits();

    /**
     * @return Directive on how to handle values too large for the bit-width of the array
     */
    OverflowHandling overflow();
}
