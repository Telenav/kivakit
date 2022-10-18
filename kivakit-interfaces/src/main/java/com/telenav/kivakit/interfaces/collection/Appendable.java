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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramCollection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;
import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNDETERMINED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * An object, often a collection or related type, to which objects can be appended. Provides default implementations for
 * appending values from objects that implement {@link Iterable} or {@link Iterator}. Note that all Java collections are
 * {@link Iterable}, so they can be appended with {@link #appendAll(Iterable)}
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCollection.class)
@CodeQuality(stability = STABILITY_UNDETERMINED,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE,
             reviews = 1,
             reviewers = "shibo")
public interface Appendable<Value> extends SpaceLimited
{
    /**
     * Appends the given value
     *
     * @return True if the value was appended, false otherwise
     */
    default boolean append(Value value)
    {
        return hasRoomFor(1) && onAppend(value);
    }

    /**
     * Appends the given values
     *
     * @param values A sequence of values to append
     * @return True if all values were appended, false otherwise
     */
    default boolean appendAll(Iterator<? extends Value> values)
    {
        while (values.hasNext())
        {
            if (!append(values.next()))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Appends the given values
     *
     * @param values A sequence of values to append
     * @return True if all values were appended, false otherwise
     */
    default boolean appendAll(Value[] values)
    {
        for (var value : values)
        {
            if (!append(value))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Appends the given values
     *
     * @param values A sequence of values to append
     * @return Self reference for chaining
     */
    default boolean appendAll(Iterable<? extends Value> values)
    {
        return appendAll(values.iterator());
    }

    /**
     * Appends the given values
     *
     * @param values A sequence of values to append
     * @return Self reference for chaining
     */
    default boolean appendAll(Collection<? extends Value> values)
    {
        if (hasRoomFor(values.size()))
        {
            return appendAll(values.iterator());
        }
        return false;
    }

    /**
     * Appends the given value if it is not null
     *
     * @param value The value to append
     * @return True if the value was added, false otherwise
     */
    default boolean appendIfNotNull(Value value)
    {
        if (value != null)
        {
            return append(value);
        }
        return true;
    }

    /**
     * Variant of appendAll that can be chained
     *
     * @param values The values to append
     * @return This object, for chaining
     */
    default Appendable<Value> appendAllThen(Iterable<? extends Value> values)
    {
        appendAll(values);
        return this;
    }

    /**
     * Variant of append that can be chained
     *
     * @param value The value to append
     * @return This object, for chaining
     */
    default Appendable<Value> appendThen(Value value)
    {
        append(value);
        return this;
    }

    /**
     * Appends the given value
     *
     * @return True if the value was appended, false otherwise
     */
    boolean onAppend(Value value);

    /**
     * Pushes the given value onto this appendable
     *
     * @param value The value to push
     */
    default void push(Value value)
    {
        append(value);
    }
}
