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

import com.telenav.kivakit.core.collections.primitive.PrimitiveCollection;
import com.telenav.kivakit.core.collections.primitive.array.scalars.ByteArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.IntArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.LongArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.ShortArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitByteArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitIntArray;
import com.telenav.kivakit.core.collections.primitive.array.scalars.SplitLongArray;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArray;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.values.count.Count;

/**
 * Base class for primitive arrays.
 *
 * @author jonathanl (shibo)
 * @see ByteArray
 * @see IntArray
 * @see ShortArray
 * @see LongArray
 * @see PrimitiveSplitArray
 * @see SplitByteArray
 * @see SplitIntArray
 * @see SplitLongArray
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArray.class)
public abstract class PrimitiveArray extends PrimitiveCollection
{
    protected PrimitiveArray(final String name)
    {
        super(name);
    }

    protected PrimitiveArray()
    {
    }

    @Override
    public Count capacity()
    {
        return count();
    }
}
