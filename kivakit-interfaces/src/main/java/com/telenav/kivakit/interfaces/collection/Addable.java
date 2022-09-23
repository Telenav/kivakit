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
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramCollection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_REQUIRED;
import static com.telenav.kivakit.interfaces.comparison.Matcher.matchAll;

/**
 * An object, often a collection or related type, to which objects can be added. Provides default implementations for
 * adding values from objects that implement {@link Iterable} or {@link Iterator}. Note that all Java collections are
 * {@link Iterable}, so they can be added with {@link #addAll(Iterable)}
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramCollection.class)
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = TESTING_NOT_REQUIRED,
            documentation = DOCUMENTED)
public interface Addable<Value> extends SpaceLimited
{
    /**
     * Adds the given value
     *
     * @return True if the value was added
     */
    default boolean add(Value value)
    {
        return hasRoomFor(1) && onAdd(value);
    }

    /**
     * Adds the given values
     *
     * @param values A sequence of values to add
     * @return True if all values were added, false otherwise
     */
    default boolean addAll(Iterable<? extends Value> values)
    {
        return addAll(values.iterator());
    }

    /**
     * Adds the given values
     *
     * @param values A sequence of values to add
     * @return True if all values were added, false otherwise
     */
    default boolean addAll(Iterator<? extends Value> values)
    {
        return addAllMatching(values, matchAll()) > 0;
    }

    /**
     * {@inheritDoc}
     */
    default boolean addAll(Collection<? extends Value> elements)
    {
        if (hasRoomFor(elements.size()))
        {
            return addAll(elements.iterator());
        }
        return false;
    }

    /**
     * Adds the given values
     *
     * @param values The values to add
     * @return True if all values were added, false otherwise
     */
    default boolean addAll(Value[] values)
    {
        return addAllMatching(values, matchAll()) >= 0;
    }

    /**
     * Adds the given values
     *
     * @param values A sequence of values to add
     * @return The number of values added, or -1 if not all values could be added
     */
    default int addAllMatching(Iterable<? extends Value> values, Matcher<Value> matcher)
    {
        return addAllMatching(values.iterator(), matcher);
    }

    /**
     * Adds the given values
     *
     * @param values A sequence of values to add
     * @return The number of values added, or -1 if not all values could be added
     */
    default int addAllMatching(Collection<? extends Value> values, Matcher<Value> matcher)
    {
        if (hasRoomFor(values.size()))
        {
            return addAllMatching(values.iterator(), matcher);
        }
        return -1;
    }

    /**
     * Adds the given values
     *
     * @param values A sequence of values to add
     * @return The number of values added, or -1 if not all values could be added
     */
    default int addAllMatching(Iterator<? extends Value> values, Matcher<Value> matcher)
    {
        var added = 0;
        while (values.hasNext())
        {
            var value = values.next();
            if (matcher.matches(value))
            {
                if (!add(values.next()))
                {
                    return -1;
                }
            }
        }
        return added;
    }

    /**
     * Adds the given values
     *
     * @param values The values to add
     * @return The number of values added, or -1 if not all values could be added
     */
    default int addAllMatching(Value[] values, Matcher<Value> matcher)
    {
        if (hasRoomFor(values.length))
        {
            return addAllMatching(Arrays.asList(values), matcher);
        }
        return -1;
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

    /**
     * Adds the given value
     *
     * @param value The value to add
     * @return True if the value was added
     */
    boolean onAdd(Value value);
}
