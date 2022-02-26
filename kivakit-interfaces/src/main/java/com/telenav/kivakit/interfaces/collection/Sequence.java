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

import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.project.lexakai.diagrams.DiagramInterfaceCollection;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * A sequence of values returned by {@link #asIterator()} and {@link #asIterator(Matcher)}.
 *
 * <p><b>Iteration</b></p>
 *
 * <p>
 * Sequences can be iterated over by converting them to an {@link Iterator} or {@link Iterable}. Sequence does not
 * directly extend these interfaces because some objects may already be using them for another purpose (and type
 * erasures in particular would ensure difficulties).
 * </p>
 *
 * <ul>
 *     <li>{@link #asIterator()} - This sequence as a Java {@link Iterator}</li>
 *     <li>{@link #asIterable()} - This sequence as a Java {@link Iterable}</li>
 *     <li>{@link #asIterator(Matcher)} - The sequence of values in this sequence that match the given matcher as a Java {@link Iterator}</li>
 *     <li>{@link #asIterable(Matcher)} - The sequence of values in this sequence that match the given matcher as a Java {@link Iterable}</li>
 * </ul>
 *
 * <p><b>Finding Elements and Subsequences</b></p>
 *
 * <ul>
 *     <li>{@link #find(Matcher)} - The first matching element in this sequence</li>
 *     <li>{@link #first()} - The first element in the sequence, or null if there is none</li>
 *     <li>{@link #head()} - The same as first()</li>
 *     <li>{@link #tail()} - All elements in the sequence except the first one as a list</li>
 *     <li>{@link #indexOfFirst(Object)} - The index of the given element in this sequence</li>
 *     <li>{@link #indexOfFirst(Matcher)} - The index of the first matching element in this sequence</li>
 * </ul>
 *
 * <p><b>Collections</b></p>
 *
 * <ul>
 *     <li>{@link #asSet()} - Converts the elements in this sequence into a set</li>
 *     <li>{@link #asList()} - Converts the elements in this sequence into a list</li>
 * </ul>
 *
 * <p><b>Joining Sequences</b></p>
 *
 * <ul>
 *     <li>{@link #join(String)} - This sequence as a string with elements separated by the given separator</li>
 *     <li>{@link #join(char)} - This sequence as a string with elements separated by the given separator</li>
 *     <li>{@link #join(String, Function)} - The elements in this sequence converted to strings and separated by the given separator</li>
 * </ul>
 *
 * <p><b>Checks</b></p>
 *
 * <p>
 * Several methods allow checks on elements in this sequence:
 * </p>
 *
 * <ul>
 *     <li>{@link #isEqualTo(Sequence)} - True if all elements in this sequence match all elements in the given sequence</li>
 *     <li>{@link #anyMatch(Matcher)} - True if any value in the sequence matches the given matcher</li>
 *     <li>{@link #allMatch(Matcher)} - True if all elements in the sequence matches the given matcher</li>
 *     <li>{@link #noneMatch(Matcher)} - True if no elements in the sequence matches the given matcher</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Indexable
 * @see Matcher
 */
@UmlClassDiagram(diagram = DiagramInterfaceCollection.class)
public interface Sequence<Element>
{
    /**
     * @return True if all elements in this sequence match the given matcher
     */
    default boolean allMatch(Matcher<Element> matcher)
    {
        for (var element : asIterable())
        {
            if (!matcher.matches(element))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @return True if any value in this sequence matches the given matcher
     */
    default boolean anyMatch(Matcher<Element> matcher)
    {
        return find(matcher) != null;
    }

    /**
     * @return A hash code for the elements in this sequence
     */
    default int asHashCode()
    {
        var hash = 536_870_909;
        for (var value : asIterable())
        {
            hash = hash ^ value.hashCode();
        }
        return hash;
    }

    /**
     * @return The elements in this list matching the given matcher
     */
    default Iterable<Element> asIterable(Matcher<Element> matcher)
    {
        return () -> asIterator(matcher);
    }

    /**
     * @return An {@link Iterable} over elements in this sequence
     */
    @NotNull
    default Iterable<Element> asIterable()
    {
        return this::asIterator;
    }

    /**
     * @return An {@link Iterator} over elements in this sequence
     */
    @NotNull
    default Iterator<Element> asIterator()
    {
        return asIterator(element -> true);
    }

    /**
     * @return An {@link Iterator} over elements in this sequence
     */
    @NotNull
    Iterator<Element> asIterator(Matcher<Element> matcher);

    /**
     * @return This sequence as a list
     */
    default List<Element> asList()
    {
        var list = new ArrayList<Element>();
        for (var element : asIterable())
        {
            list.add(element);
        }
        return list;
    }

    /**
     * @return This sequence as a list
     */
    default Set<Element> asSet()
    {
        var set = new HashSet<Element>();
        for (var element : asIterable())
        {
            set.add(element);
        }
        return set;
    }

    default Element find(Class<? extends Element> type)
    {
        return find(at -> type.isAssignableFrom(at.getClass()));
    }

    /**
     * @return The first value that matches the matcher
     */
    default Element find(Matcher<Element> matcher)
    {
        for (var element : asIterable())
        {
            if (matcher.matches(element))
            {
                return element;
            }
        }
        return null;
    }

    /**
     * @return The first item in this sequence, or null if there is none
     */
    default Element first()
    {
        return asIterator().next();
    }

    /**
     * @return The head (first element) of this sequence or null if there is none
     */
    default Element head()
    {
        return first();
    }

    /**
     * @return The index of the first value in this sequence matching the given matcher. If no value in the sequence
     * matches then -1 is returned.
     */
    default int indexOfFirst(Matcher<Element> matcher)
    {
        int index = 0;
        for (var element : asIterable())
        {
            if (matcher.matches(element))
            {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * @return The index of the given value or -1 if none exists
     */
    default int indexOfFirst(Element value)
    {
        return indexOfFirst(element -> element.equals(value));
    }

    /**
     * @return True if this sequence object and that sequence have all the same elements
     */
    default boolean isEqualTo(Sequence<Element> that)
    {
        var thisIterator = asIterator();
        var thatIterator = that.asIterator();

        // While we have a next value,
        while (thisIterator.hasNext())
        {
            // if that doesn't have a next value,
            if (!thatIterator.hasNext())
            {
                // the sequences are not equal,
                return false;
            }

            // otherwise both sequences have a next element,
            var thisElement = thisIterator.next();
            var thatElement = thatIterator.next();

            // but if they aren't equal,
            if (!thisElement.equals(thatElement))
            {
                // the sequences are not equal.
                return false;
            }
        }

        // If we ran out of elements, then that sequence must also be out of elements.
        return !thatIterator.hasNext();
    }

    /**
     * @return The elements in this sequence joined as a string with the given separator
     */
    default String join(char separator)
    {
        return join(String.valueOf(separator));
    }

    /**
     * @return The elements in this sequence joined as a string with the given separator
     */
    default String join(String separator)
    {
        return join(separator, Object::toString);
    }

    /**
     * @return The elements in this sequence joined as a string with the given separator or the default value if this
     * sequence is empty
     */
    default String join(String separator, String defaultValue)
    {
        if (first() == null)
        {
            return defaultValue;
        }
        return join(separator, Object::toString);
    }

    /**
     * @return The elements in this sequence transformed into strings by the given function and joined together with the
     * given separator
     */
    default String join(String separator, Function<Element, String> toString)
    {
        var builder = new StringBuilder();
        for (var value : asIterable())
        {
            if (builder.length() > 0)
            {
                builder.append(separator);
            }
            builder.append(toString.apply(value));
        }
        return builder.toString();
    }

    /**
     * @return True if no element in this sequence matches the given matcher
     */
    default boolean noneMatch(Matcher<Element> matcher)
    {
        return find(matcher) == null;
    }

    /**
     * @return The tail of this sequence (all elements but the first element). If there is only one element, the tail is
     * an empty list.
     */
    default List<Element> tail()
    {
        var tail = new ArrayList<Element>();
        int index = 0;
        for (var element : asIterable())
        {
            if (index++ > 0)
            {
                tail.add(element);
            }
        }
        return tail;
    }
}
