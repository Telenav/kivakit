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

package com.telenav.kivakit.kernel.interfaces.collection;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramExampleBaseList;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramInterfaceCollection;
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
@UmlClassDiagram(diagram = DiagramInterfaceCollection.class)
@UmlClassDiagram(diagram = DiagramExampleBaseList.class)
public interface Addable<T>
{
    /**
     * Adds the given value
     *
     * @return True if the value was added
     */
    boolean add(T value);

    /**
     * @param values A sequence of values to add
     */
    default boolean addAll(final Iterable<? extends T> values)
    {
        for (final T value : values)
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
    default boolean addAll(final Iterator<? extends T> values)
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
}
