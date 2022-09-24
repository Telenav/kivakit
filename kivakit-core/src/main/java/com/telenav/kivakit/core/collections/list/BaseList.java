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

package com.telenav.kivakit.core.collections.list;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.iteration.BaseIterator;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.string.StringTo;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Countable;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.collection.Addable;
import com.telenav.kivakit.interfaces.collection.Appendable;
import com.telenav.kivakit.interfaces.collection.Copyable;
import com.telenav.kivakit.interfaces.collection.Indexable;
import com.telenav.kivakit.interfaces.collection.Prependable;
import com.telenav.kivakit.interfaces.collection.Sectionable;
import com.telenav.kivakit.interfaces.collection.Sequence;
import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.kivakit.interfaces.collection.WriteIndexable;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.factory.Factory;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.MORE_TESTING_REQUIRED;
import static com.telenav.kivakit.interfaces.string.StringFormattable.Format.TO_STRING;

/**
 * A base class for bounded lists which adds a number of convenient methods as well as support for various KivaKit
 * interfaces, including {@link Indexable}, {@link Sequence}, {@link Addable}, {@link java.lang.Appendable},and
 * {@link Sized}}. Some added convenience methods include:
 *
 * <p><b>Bounds</b></p>
 *
 * <ul>
 *     <li>{@link #maximumSize()} - The maximum size of this list</li>
 *     <li>{@link #hasRoomFor(int)} - For use by subclasses to check their size</li>
 *     <li>{@link #onOutOfRoom(int)} - Responds with a warning when the list is out of space</li>
 * </ul>
 *
 * <p><b>Checks</b></p>
 *
 * <ul>
 *     <li>{@link #endsWith(Indexable)} - True if this list ends with the same elements as the given list</li>
 *     <li>{@link #startsWith(Indexable)} - True if this list starts with the same elements as the given list</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asArray(Class)} - This list as an array of the given type</li>
 * </ul>
 *
 * <p><b>String Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #bulleted()} - The elements in this list as a bulleted string, with on element to a line</li>
 *     <li>{@link #bulleted(int)} - An indented bullet list of the elements in this list</li>
 *     <li>{@link #join()} - This list joined by the list {@link #separator()}</li>
 *     <li>{@link #separator()} - The separator used when joining this list into a string</li>
 * </ul>
 *
 * <p><b>Functional Methods</b></p>
 *
 * <ul>
 *     <li>{@link #copy()} - A copy of this list</li>
 *     <li>{@link #without(Matcher)} - This list without the matching elements</li>
 *     <li>{@link #first(int)} - A new list with the first n elements in it</li>
 *     <li>{@link #first(Count)} - A new list with the first n elements in it</li>
 *     <li>{@link #last(int)} - A new list with the first n elements in it</li>
 *     <li>{@link #last(Count)} - A new list with the first n elements in it</li>
 *     <li>{@link #leftOf(int)} - The elements in this list to the left on the given index, exclusive</li>
 *     <li>{@link #rightOf(int)} - The elements in this list to the right on the given index, exclusive</li>
 *     <li>{@link #matching(Matcher)} - A copy of this list filtered to matching elements</li>
 *     <li>{@link #mapped(Function)} - A copy of this list with elements mapped to another type</li>
 *     <li>{@link #sorted(Comparator)} - A copy of this list sorted by the given comparator</li>
 *     <li>{@link #reversed()} - This list reversed</li>
 *     <li>{@link #maybeReversed(boolean)} - This list reversed if the given boolean is true</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Factory
 * @see List
 * @see Indexable
 * @see WriteIndexable
 * @see Sectionable
 * @see Addable
 * @see Appendable
 * @see Prependable
 * @see RandomAccess
 * @see Countable
 * @see StringFormattable
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCollections.class, excludeAllSuperTypes = true)
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = MORE_TESTING_REQUIRED,
            documentation = FULLY_DOCUMENTED)
public abstract class BaseList<Value> implements
        Factory<BaseList<Value>>,
        List<Value>,
        Copyable<Value, BaseList<Value>>,
        WriteIndexable<Value>,
        Sequence<Value>,
        Sectionable<Value, BaseList<Value>>,
        Addable<Value>,
        Appendable<Value>,
        Prependable<Value>,
        RandomAccess,
        Countable,
        StringFormattable
{
    /** Initial list implementation while mutable */
    private final List<Value> list;

    /** The maximum number of values that can be stored in this list */
    private int maximumSize;

    /** True if this set ran out of room, and we've already warned about it */
    private boolean warnedAboutOutOfRoom;

    /**
     * @param maximumSize The maximum size of this list
     */
    protected BaseList(Maximum maximumSize)
    {
        this(maximumSize, new ArrayList<>());
    }

    /**
     * @param maximumSize The maximum size of this list
     * @param list The list implementation to use
     */
    protected BaseList(Maximum maximumSize, Collection<Value> list)
    {
        // If we have room for the list
        if (hasRoomFor(list.size()))
        {
            // save the maximum size,
            this.maximumSize = maximumSize.asInt();

            // and the list.
            if (list instanceof List)
            {
                this.list = (List<Value>) list;
            }
            else
            {
                this.list = new ArrayList<>(list);
            }
        }
        else
        {
            // otherwise, signal that the list is out of room,
            onOutOfRoom(list.size());

            // and leave the list empty.
            this.list = new ArrayList<>();
        }
    }

    /**
     * An unbounded list
     */
    protected BaseList()
    {
        this(Maximum.MAXIMUM);
    }

    /**
     * An unbounded list with the given list implementation
     */
    protected BaseList(Collection<Value> collection)
    {
        this(Maximum.MAXIMUM, collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(int index, Value value)
    {
        if (hasRoomFor(1))
        {
            list.add(index, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(Value value)
    {
        return hasRoomFor(1) && onAdd(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends Value> elements)
    {
        if (hasRoomFor(elements.size()))
        {
            return list.addAll(elements);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(int index, Collection<? extends Value> collection)
    {
        if (hasRoomFor(collection.size()))
        {
            return list.addAll(index, collection);
        }
        return false;
    }

    @Override
    public BaseList<Value> appendThen(Value value)
    {
        return (BaseList<Value>) Appendable.super.appendThen(value);
    }

    @Override
    public BaseList<Value> appendThen(Iterable<? extends Value> values)
    {
        return (BaseList<Value>) Appendable.super.appendThen(values);
    }

    @Override
    public BaseList<Value> appendThen(Iterator<? extends Value> values)
    {
        return (BaseList<Value>) Appendable.super.appendThen(values);
    }

    /**
     * @return This list as an array
     */
    @SuppressWarnings({ "unchecked" })
    public Value[] asArray(Class<Value> type)
    {
        var array = (Value[]) Array.newInstance(type, size());
        toArray(array);
        return array;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Iterator<Value> asIterator(Matcher<Value> matcher)
    {
        return new BaseIterator<>()
        {
            int index = 0;

            @Override
            protected Value onNext()
            {
                while (index < size())
                {
                    var element = get(index++);
                    if (matcher.matches(element))
                    {
                        return element;
                    }
                }
                return null;
            }
        };
    }

    @Override
    public @NotNull
    Iterator<Value> asIterator()
    {
        return asIterator(Matcher.matchAll());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    public String asString(Format format)
    {
        switch (format)
        {
            case DEBUG:
                return join(separator(), StringTo::debug);

            default:
                return join();
        }
    }

    /**
     * @return This list as a string list
     */
    public StringList asStringList()
    {
        return new StringList(maximumSize(), this);
    }

    /**
     * @return This list with braces around it indented by 4 spaces
     */
    public String bracketed()
    {
        return bracketed(4);
    }

    /**
     * @return This list with braces around it indented by the given number of spaces
     */
    public String bracketed(int indent)
    {
        return "\n{\n" + bulleted(indent) + "\n}";
    }

    /**
     * @return The items in this list in a bulleted ASCII art representation
     */
    public String bulleted()
    {
        return bulleted(4);
    }

    /**
     * @return The items in this list in a bulleted ASCII art representation with the given indent
     */
    public String bulleted(int indent)
    {
        return AsciiArt.bulleted(indent, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear()
    {
        list.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object that)
    {
        return list.contains(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(@NotNull Collection<?> collection)
    {
        return new HashSet<>(list).containsAll(collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Count count()
    {
        return Count.count(size());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof List)
        {
            var that = (List<?>) object;

            // The lists are only seen as equal if they have the same objects in
            // the same order.
            if (size() == that.size())
            {
                for (var i = 0; i < size(); i++)
                {
                    if (get(i) != that.get(i) && !get(i).equals(that.get(i)))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseList<Value> first(int count)
    {
        return Sectionable.super.first(count);
    }

    /**
     * @return The first count elements of this list
     */
    public BaseList<Value> first(Count count)
    {
        return first(count.asInt());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Value get(int index)
    {
        return list.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return list.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int indexOf(Object element)
    {
        return list.indexOf(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Value> iterator()
    {
        return list.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseList<Value> last(int count)
    {
        return Sectionable.super.last(count);
    }

    /**
     * @return The last count values of this list
     */
    public BaseList<Value> last(Count count)
    {
        return last(count.asInt());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int lastIndexOf(Object element)
    {
        return list.lastIndexOf(element);
    }

    @Override
    public BaseList<Value> leftOf(int index)
    {
        return Sectionable.super.leftOf(index);
    }

    @NotNull
    @Override
    public ListIterator<Value> listIterator()
    {
        return list.listIterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<Value> listIterator(int index)
    {
        return list.listIterator(index);
    }

    /**
     * @return This bounded list with all elements mapped by the given mapper to the mapper's target type
     */
    @SuppressWarnings("unchecked")
    public <Target> BaseList<Target> mapped(Function<Value, Target> mapper)
    {
        var filtered = (BaseList<Target>) newInstance();
        for (var element : this)
        {
            filtered.add(mapper.apply(element));
        }
        return filtered;
    }

    /**
     * @return The maximum size of this bounded list
     */
    public final Maximum maximumSize()
    {
        return Maximum.maximum(maximumSize);
    }

    /**
     * @return This list reversed if reverse is true, or the list itself if it is false
     */
    public BaseList<Value> maybeReversed(boolean reverse)
    {
        return reverse ? reversed() : this;
    }

    @Override
    public BaseList<Value> newInstance()
    {
        var instance = onNewInstance();
        instance.maximumSize = maximumSize;
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onAdd(Value value)
    {
        return list.add(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onAppend(Value value)
    {
        return onAdd(value);
    }

    /**
     * Called when a bounded list runs out of room
     */
    @Override
    public void onOutOfRoom(int values)
    {
        if (!warnedAboutOutOfRoom)
        {
            warnedAboutOutOfRoom = true;
            Ensure.warning(new Throwable(), "Adding $ values, would exceed maximum size of $. Ignoring operation.", values, totalRoom());
        }
    }

    /**
     * Prepends the given value to the front of this list
     */
    @Override
    public boolean onPrepend(Value value)
    {
        list.add(0, value);
        return true;
    }

    /**
     * @return The last value in this list, after removing it
     */
    public Value pop()
    {
        return removeLast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Value remove(int index)
    {
        return list.remove(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object element)
    {
        return list.remove(element);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public boolean removeAll(Collection<?> collection)
    {
        return list.removeAll(collection);
    }

    /**
     * @return Removes the last element in this list
     */
    public Value removeLast()
    {
        if (!isEmpty())
        {
            return remove(size() - 1);
        }
        return null;
    }

    /**
     * Replace all occurrences of the given value with the given replacement
     *
     * @param value The value to replace
     * @param replacement The value to substitute
     */
    public boolean replaceAll(Value value, Value replacement)
    {
        return Collections.replaceAll(list, value, replacement);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public boolean retainAll(Collection<?> collection)
    {
        return list.retainAll(collection);
    }

    /**
     * Reverses this list in-place
     */
    public void reverse()
    {
        Collections.reverse(this);
    }

    /**
     * @return This list reversed
     */
    public BaseList<Value> reversed()
    {
        var copy = copy();
        copy.reverse();
        return copy;
    }

    @Override
    public BaseList<Value> rightOf(int index)
    {
        return Sectionable.super.rightOf(index);
    }

    /**
     * @return The separator to use when joining this list
     */
    @Override
    public String separator()
    {
        return ", ";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Value set(int index, Value value)
    {
        return list.set(index, value);
    }

    /**
     * Shuffles this list into a random ordering
     */
    public void shuffle()
    {
        shuffle(new Random());
    }

    /**
     * Shuffles this list using the given {@link Random} number generator
     *
     * @param random The random number generator
     */
    public void shuffle(Random random)
    {
        Collections.shuffle(list, random);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        return list.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sort(Comparator<? super Value> comparator)
    {
        list.sort(comparator);
    }

    /**
     * @return This list sorted by casting the element type to {@link Comparable}. If the elements in the list are not
     * comparable, an exception will be thrown.
     */
    @SuppressWarnings("unchecked")
    public BaseList<Value> sorted()
    {
        return sorted((Value a, Value b) -> ((Comparable<Value>) a).compareTo(b));
    }

    /**
     * @return A copy of this list sorted by the given comparator
     */
    public BaseList<Value> sorted(Comparator<Value> comparator)
    {
        var sorted = newInstance();
        sorted.addAll(this);
        sorted.sort(comparator);
        return sorted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Value> subList(int fromIndex, int toIndex)
    {
        return list.subList(fromIndex, toIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray()
    {
        return list.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "SuspiciousToArrayCall" })
    @Override
    public <E> E[] toArray(E @NotNull [] array)
    {
        return list.toArray(array);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return asString(TO_STRING);
    }

    @Override
    public int totalRoom()
    {
        return maximumSize;
    }

    /**
     * @return A copy of this list with only unique elements in it
     */
    @SuppressWarnings("SpellCheckingInspection")
    public BaseList<Value> uniqued()
    {
        var list = newInstance();
        list.addAll(asSet());
        return list;
    }

    /**
     * Convert the given value to a string
     *
     * @param value The value
     * @return A string corresponding to the value
     */
    protected String toString(Value value)
    {
        return StringTo.string(value);
    }
}
