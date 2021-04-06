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
 * Base class for primitive iterators, allowing subclasses to be treated the same
 */
@UmlClassDiagram(diagram = DiagramPrimitiveCollection.class)
public interface PrimitiveIterator
{
    /** True if there is a next value */
    boolean hasNext();

    default int hashValue()
    {
        long hash = Hash.SEED;
        while (hasNext())
        {
            hash = hash ^ nextLong();
        }
        return (int) hash;
    }

    default boolean identical(final PrimitiveIterator that)
    {
        while (hasNext())
        {
            if (!that.hasNext())
            {
                return false;
            }
            if (nextLong() != that.nextLong())
            {
                return false;
            }
        }
        return !that.hasNext();
    }

    /** The next value as a long (even if the subclass is iterating another primitive type */
    long nextLong();
}
