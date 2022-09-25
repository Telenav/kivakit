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

package com.telenav.kivakit.core.collections;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_STATIC_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_REQUIRED;

/**
 * Collection utility methods
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_STATIC_EXPANDABLE,
            testing = TESTING_NOT_REQUIRED,
            documentation = FULLY_DOCUMENTED)
public class Collections
{
    /**
     * Adds the given value n times to a collection of values
     *
     * @param values The collection to add to
     * @param value The value to add
     * @param times The number of times to add the value
     */
    public static <T> void addRepeatedly(Collection<T> values, T value, int times)
    {
        for (var index = 0; index < times; index++)
        {
            values.add(value);
        }
    }

    /**
     * Returns the first value in a collection of values
     *
     * @param values The values
     * @return The first value
     */
    public static <T> T first(Collection<T> values)
    {
        var iterator = values.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * Returns a collection of values as a sorted {@link ObjectList}
     *
     * @param values The values
     * @return The values as a sorted object list
     */
    public static <T extends Comparable<T>> ObjectList<T> sortedCollection(Collection<T> values)
    {
        return ObjectList.objectList(values).sorted();
    }
}
