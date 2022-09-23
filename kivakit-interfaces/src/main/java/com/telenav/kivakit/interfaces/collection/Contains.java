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

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_REQUIRED;

/**
 * An interface for any object that can contain values, typically a collection.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramCollection.class)
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = TESTING_NOT_REQUIRED,
            documentation = DOCUMENTED)
public interface Contains<Value>
{
    /**
     * @return True if the given value is contained in this object
     */
    boolean contains(Value value);

    /**
     * @param values The values to check
     * @return True if all values are contained by this object
     */
    default boolean containsAll(Iterable<Value> values)
    {
        for (var value : values)
        {
            if (!contains(value))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @param values The values to check
     * @return True if none of the given values are contained by this object
     */
    default boolean containsNone(Iterable<Value> values)
    {
        for (var value : values)
        {
            if (contains(value))
            {
                return false;
            }
        }
        return true;
    }
}
