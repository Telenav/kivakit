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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramCollection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNNECESSARY;

/**
 * An object, often a collection or related type, to which objects can be appended. Provides default implementations for
 * appending values from objects that implement {@link Iterable} or {@link Iterator}. Note that all Java collections are
 * {@link Iterable}, so they can be appended with {@link #appendAll(Iterable)}
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramCollection.class)
@CodeQuality(stability = STABLE,
             testing = UNNECESSARY,
             documentation = SUFFICIENT)
public interface Appendable<T>
{
    /**
     * Adds the given value
     *
     * @return Self reference for chaining of append calls
     */
    Appendable<T> append(T value);

    /**
     * @param values A sequence of values to add
     */
    default Appendable<T> appendAll(Iterator<? extends T> values)
    {
        while (values.hasNext())
        {
            append(values.next());
        }
        return this;
    }

    /**
     * @param values A sequence of values to add
     */
    default Appendable<T> appendAll(Iterable<? extends T> values)
    {
        for (T value : values)
        {
            append(value);
        }
        return this;
    }
}
