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

package com.telenav.kivakit.core.collections.primitive.iteration;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveCollection;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.objects.Hash;

/**
 * An iterator that produces a sequence of values. More values are available so long as {@link #hasNext()} returns true.
 * The next value can be retrieved with {@link #next()}. A hash code for all the values in the sequence can be retrieved
 * with {@link #hash()}
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitiveCollection.class)
public interface ByteIterator extends PrimitiveIterator
{
    /**
     * @return True if there is a next value
     */
    @Override
    boolean hasNext();

    /**
     * Computes a hash value for this iterator. Since iterators often produce the same values out of order (in sets and
     * maps), the algorithm takes this into account, producing the same hash value no matter the order of elements
     * iterated.
     *
     * @return A hash value composed by iterating through available values
     */
    default int hash()
    {
        var hashCode = 1;
        while (hasNext())
        {
            hashCode = hashCode + Hash.code(next());
        }
        return hashCode;
    }

    /**
     * @return The next value in the sequence
     */
    byte next();

    @Override
    default long nextLong()
    {
        return next();
    }
}
