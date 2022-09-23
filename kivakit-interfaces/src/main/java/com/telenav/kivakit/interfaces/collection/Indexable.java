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

package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramCollection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.NONE;

/**
 * A sequence that has a known size and can be indexed, like a list, although not necessarily a collection. For example,
 * a random access file is {@link Indexable} because its length is known and values can be retrieved from any index in
 * the file.
 * <p>
 * Provides a default implementation of {@link Iterable} and {@link Iterator} accessible through {@link #asIterable()}
 * and {@link #asIterator()} as well as equals and hashcode accessible through {@link #isEqualTo(Indexable)} and
 * {@link Object#hashCode()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCollection.class)
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = NONE,
            documentation = SUFFICIENT)
public interface Indexable<Element> extends
        Sized,
        Sequence<Element>
{
    /**
     * @return The first item in this indexable object, or null if there is none
     */
    @Override
    default Element first()
    {
        return isEmpty() ? null : get(0);
    }

    /**
     * @return The value for the given index
     */
    Element get(int index);

    /**
     * @return The value at the given index or the default value if that index does not exist
     */
    default Element getOrDefault(int index, Element defaultValue)
    {
        return index < size() ? get(index) : defaultValue;
    }

    /**
     * @return True if this indexable object and that indexable object have all the same values
     */
    default boolean isEqualTo(Indexable<Element> that)
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
