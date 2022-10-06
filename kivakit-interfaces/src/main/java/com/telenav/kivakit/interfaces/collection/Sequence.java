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
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramCollection;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_FURTHER_EVALUATION_NEEDED;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A sequence of values returned by {@link #asIterator()} and {@link #asIterator(Matcher)}.
 *
 * <p><b>Iteration</b></p>
 *
 * <p>
 * Sequences can be iterated over by simply using the sequence as an {@link Iterable} (which it implements), or by
 * converting it to an {@link Iterable} or {@link Iterator} using one of the asIter*() methods. Sequence does not
 * directly extend these interfaces because some objects may already be using them for another purpose (and type
 * erasures in particular would ensure difficulties).
 * </p>
 *
 * <ul>
 *     <li>{@link #asIterator()} - This sequence as a Java {@link Iterator}</li>
 *     <li>{@link #asIterator(Matcher)} - The sequence of values in this sequence that match the given matcher as a Java {@link Iterator}</li>
 *     <li>{@link #asIterable(Matcher)} - The sequence of values in this sequence that match the given matcher as a Java {@link Iterable}</li>
 * </ul>
 *
 * <p><b>Finding Elements and Subsequences</b></p>
 *
 * <ul>
 *     <li>{@link #findFirst(Matcher)} - The first matching element in this sequence</li>
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
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCollection.class)
@CodeQuality(stability = CODE_FURTHER_EVALUATION_NEEDED,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             reviews = 1,
             reviewers = "shibo")
public interface Sequence<Value> extends
        Iterable<Value>,
        Joinable<Value>
{
    /**
     * Returns true if all elements in this sequence match the given matcher
     */
    default boolean allMatch(Matcher<Value> matcher)
    {
        for (var element : this)
        {
            if (!matcher.matches(element))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if any value in this sequence matches the given matcher
     */
    default boolean anyMatch(Matcher<Value> matcher)
    {
        return findFirst(matcher) != null;
    }

    /**
     * Returns a hash code for the elements in this sequence
     */
    default int asHashCode()
    {
        var hash = 536_870_909;
        for (var value : this)
        {
            hash = hash ^ value.hashCode();
        }
        return hash;
    }

    /**
     * Returns the elements in this list matching the given matcher
     */
    default Iterable<Value> asIterable(Matcher<Value> matcher)
    {
        return () -> asIterator(matcher);
    }

    /**
     * Returns an {@link Iterable} over elements in this sequence
     */
    @NotNull
    default Iterable<Value> asIterable()
    {
        return this::asIterator;
    }

    /**
     * Returns an {@link Iterator} over elements in this sequence
     */
    @NotNull
    default Iterator<Value> asIterator()
    {
        return asIterator(value -> true);
    }

    /**
     * Returns an iterator over all the values in this sequence that match the given matcher
     *
     * @param matcher The matcher to match values against
     * @return An {@link Iterator} over values in this sequence
     */
    @NotNull
    Iterator<Value> asIterator(Matcher<Value> matcher);

    /**
     * Returns this sequence as a list
     */
    default List<Value> asList()
    {
        var list = new ArrayList<Value>();
        forEach(list::add);
        return list;
    }

    /**
     * Returns this sequence as a set
     */
    default Set<Value> asSet()
    {
        var list = new HashSet<Value>();
        forEach(list::add);
        return list;
    }

    /**
     * @param type The type to search for
     * @return The first element of the given type
     */
    default Value findFirst(Class<? extends Value> type)
    {
        return findFirst(at -> type.isAssignableFrom(at.getClass()));
    }

    /**
     * Returns the first value that matches the matcher
     */
    default Value findFirst(Matcher<Value> matcher)
    {
        for (var element : this)
        {
            if (matcher.matches(element))
            {
                return element;
            }
        }
        return null;
    }

    /**
     * Returns the first item in this sequence, or null if there is none
     */
    default Value first()
    {
        return asIterator().next();
    }

    /**
     * Returns the head (first element) of this sequence or null if there is none
     */
    default Value head()
    {
        return first();
    }

    /**
     * Returns the index of the first value in this sequence matching the given matcher. If no value in the sequence
     * matches then -1 is returned.
     */
    default int indexOfFirst(Matcher<Value> matcher)
    {
        int index = 0;
        for (var element : this)
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
     * Returns the index of the given value or -1 if none exists
     */
    default int indexOfFirst(Value value)
    {
        return indexOfFirst(element -> element.equals(value));
    }

    /**
     * Returns true if this sequence object and that sequence have all the same elements
     */
    default boolean isEqualTo(Sequence<Value> that)
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

            // otherwise, both sequences have a next element,
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

    @Override
    default Iterator<Value> iterator()
    {
        return asIterator();
    }

    /**
     * Returns true if no element in this sequence matches the given matcher
     */
    default boolean noneMatch(Matcher<Value> matcher)
    {
        return findFirst(matcher) == null;
    }

    /**
     * Returns the tail of this sequence (all elements but the first element). If there is only one element, the tail is
     * an empty list.
     */
    default List<Value> tail()
    {
        var tail = new ArrayList<Value>();
        int index = 0;
        for (var element : this)
        {
            if (index++ > 0)
            {
                tail.add(element);
            }
        }
        return tail;
    }
}
