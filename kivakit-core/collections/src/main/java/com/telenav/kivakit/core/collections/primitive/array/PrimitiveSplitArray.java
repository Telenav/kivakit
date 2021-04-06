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

package com.telenav.kivakit.core.collections.primitive.array;

import com.telenav.kivakit.core.collections.primitive.array.packed.SplitPackedArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitByteArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitCharArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitIntArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitLongArray;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveSplitArray;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Base class for "split" primitive arrays. Split collections manage storage with several children instead of large
 * allocations, which can be expensive to resize and hard on the garbage collector.
 *
 * @author jonathanl (shibo)
 * @see SplitByteArray
 * @see SplitCharArray
 * @see SplitIntArray
 * @see SplitLongArray
 * @see SplitPackedArray
 */
@UmlClassDiagram(diagram = DiagramPrimitiveSplitArray.class)
public abstract class PrimitiveSplitArray extends PrimitiveArray
{
    protected PrimitiveSplitArray(final String name)
    {
        super(name);
    }

    protected PrimitiveSplitArray()
    {
    }
}
