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

import static com.telenav.kivakit.annotations.code.ApiStability.API_FURTHER_EVALUATION_NEEDED;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

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
@ApiQuality(stability = API_FURTHER_EVALUATION_NEEDED,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE,
            reviews = 1,
            reviewers = "shibo")
public interface Indexable<Value> extends
        Sized,
        Sequence<Value>
{
    /**
     * @return True if this list starts with the given list
     */
    default boolean endsWith(Indexable<Value> that)
    {
        if (that == null || that.size() > size())
        {
            return false;
        }
        else
        {
            var thisIndex = this.size() - 1;
            var thatIndex = that.size() - 1;
            for (; thatIndex >= 0 && thisIndex >= 0; thisIndex--, thatIndex--)
            {
                if (!get(thisIndex).equals(that.get(thatIndex)))
                {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * @return The first item in this indexable object, or null if there is none
     */
    @Override
    default Value first()
    {
        return isEmpty() ? null : get(0);
    }

    /**
     * @return The value for the given index
     */
    Value get(int index);

    /**
     * @return The value at the given index or the default value if that index does not exist
     */
    default Value getOrDefault(int index, Value defaultValue)
    {
        return index < size() ? get(index) : defaultValue;
    }

    /**
     * @return True if this indexable object and that indexable object have all the same values
     */
    default boolean isEqualTo(Indexable<Value> that)
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
    default Value last()
    {
        return isEmpty() ? null : get(size() - 1);
    }

    /**
     * @return True if this list starts with the given list
     */
    default boolean startsWith(Indexable<Value> that)
    {
        if (that == null || that.size() > size())
        {
            return false;
        }
        else
        {
            for (var i = 0; i < that.size(); i++)
            {
                if (!get(i).equals(that.get(i)))
                {
                    return false;
                }
            }
            return true;
        }
    }
}
