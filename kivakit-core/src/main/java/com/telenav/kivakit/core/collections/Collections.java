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

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_STATIC_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Collection utility methods
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_STATIC_EXPANDABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public class Collections
{
    /**
     * Returns the first value in a collection of values, or null if the collection is empty. If the collection is not
     * ordered, which value is returned is not defined.
     *
     * @param values The values
     * @return The first value
     */
    public static <T> T first(Collection<T> values)
    {
        var iterator = values.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }
}
