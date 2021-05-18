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

package com.telenav.kivakit.kernel.interfaces.collection;

import com.telenav.kivakit.kernel.interfaces.numeric.Sized;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramExampleBaseList;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramInterfaceCollection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;

/**
 * A sequence that has a known size and can be indexed, like a list, although not necessarily a collection. For example,
 * a random access file is {@link Indexable} because its length is known and values can be retrieved from any index in
 * the file.
 * <p>
 * Provides a default implementation of {@link Iterable} and {@link Iterator} accessible through {@link #asIterable()}
 * and {@link #asIterator()} as well as equals and hashcode accessible through {@link #isEqualTo(Indexable)} and {@link
 * #asHashCode()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceCollection.class)
@UmlClassDiagram(diagram = DiagramExampleBaseList.class)
public interface Indexable<Element> extends Sized, Sequence<Element>
{
    /**
     * @return The value for the given index
     */
    Element get(int index);

    /**
     * @return The value at the given index or the default value if that index does not exist
     */
    default Element getOrDefault(final int index, final Element defaultValue)
    {
        return index < size() ? get(index) : defaultValue;
    }

    /**
     * @return True if this indexable object and that indexable object have all the same values
     */
    default boolean isEqualTo(final Indexable<Element> that)
    {
        if (size() == that.size())
        {
            for (var index = 0; index < size(); index++)
            {
                if (!get(index).equals(that.get(index)))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @return The last item in this indexable object, or null if there is none
     */
    default Element last()
    {
        return isEmpty() ? null : get(size() - 1);
    }
}
