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

import com.telenav.kivakit.interfaces.project.lexakai.DiagramCollection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;

/**
 * An object, often a collection or related type, to which objects can be added. Provides default implementations for
 * adding values from objects that implement {@link Iterable} or {@link Iterator}. Note that all Java collections are
 * {@link Iterable}, so they can be added with {@link #addAll(Iterable)}
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramCollection.class)
public interface Addable<Value>
{
    /**
     * Adds the given value
     *
     * @return True if the value was added
     */
    boolean add(Value value);

    /**
     * @param values A sequence of values to add
     */
    default boolean addAll(Iterable<? extends Value> values)
    {
        for (Value value : values)
        {
            if (!add(value))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @param values A sequence of values to add
     */
    default boolean addAll(Iterator<? extends Value> values)
    {
        while (values.hasNext())
        {
            if (!add(values.next()))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds the given value if it is not null
     *
     * @param value The value to add
     * @return True if the value was added, false otherwise
     */
    default boolean addIfNotNull(Value value)
    {
        if (value != null)
        {
            return add(value);
        }
        return false;
    }
}
