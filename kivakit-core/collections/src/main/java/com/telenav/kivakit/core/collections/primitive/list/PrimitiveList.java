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

package com.telenav.kivakit.core.collections.primitive.list;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveList;
import com.telenav.kivakit.core.kernel.interfaces.factory.LongMapFactory;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Sized;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.values.count.Count;

/**
 * Interface implemented by primitive lists. Primitive values are treated as longs to unify the different lists and
 * allow them to be treated the same.
 *
 * @author jonathanl (shibo)
 * @see ByteList
 * @see IntList
 * @see LongList
 * @see ShortList
 */
@UmlClassDiagram(diagram = DiagramPrimitiveList.class)
public interface PrimitiveList extends Sized
{
    default <T> ObjectList<T> asList(final LongMapFactory<T> factory)
    {
        final var list = new ObjectList<T>();
        for (var at = 0; at < size(); at++)
        {
            list.add(factory.newInstance(getPrimitive(at)));
        }
        return list;
    }

    /**
     * @return The present maximum storage capacity of this list in elements without resizing
     */
    Count capacity();

    /** The value at the given index (even if the subclass is a list of another primitive type) */
    long getPrimitive(int index);

    /**
     * @return True if the given value is null
     */
    boolean isPrimitiveNull(long value);

    /**
     * @return The value at the given index, but without a bounds check
     */
    long safeGetPrimitive(int index);

    /**
     * Sets the indexed element in the list to the given value
     */
    void setPrimitive(int index, long value);
}
