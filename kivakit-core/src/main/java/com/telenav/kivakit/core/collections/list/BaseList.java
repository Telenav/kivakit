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
import com.telenav.kivakit.core.collections.BaseCollection;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Countable;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.collection.Addable;
import com.telenav.kivakit.interfaces.collection.Appendable;
import com.telenav.kivakit.interfaces.collection.Copyable;
import com.telenav.kivakit.interfaces.collection.Indexable;
import com.telenav.kivakit.interfaces.collection.Joinable;
import com.telenav.kivakit.interfaces.collection.Prependable;
import com.telenav.kivakit.interfaces.collection.Sectionable;
import com.telenav.kivakit.interfaces.collection.Sequence;
import com.telenav.kivakit.interfaces.collection.WriteIndexable;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.MORE_TESTING_NEEDED;

/**
 * A base class for bounded lists which adds convenient methods as well as support for various KivaKit interfaces:
 *
 * <ul>
 *     <li>{@link List}</li>
 *     <li>{@link Copyable}</li>
 *     <li>{@link WriteIndexable}</li>
 *     <li>{@link Sequence}</li>
 *     <li>{@link Sectionable}</li>
 *     <li>{@link Addable}</li>
 *     <li>{@link Prependable}</li>
 *     <li>{@link RandomAccess}</li>
 *     <li>{@link Countable}</li>
 *     <li>{@link StringFormattable}</li>
 * </ul>
 *
 * <p><b>Functional Methods</b></p>
 *
 * <p>
 * Some methods are functional and return a new list. The method {@link #newInstance()} is used to create lists.
 * Subclasses create the subclass list type by overriding {@link #onNewInstance()}.
 * </p>
 *
 * <p><b>Adding</b></p>
 *
 * <ul>
 *     <li>{@link #add(Object)}</li>
 *     <li>{@link #addIfNotNull(Object)}</li>
 *     <li>{@link #add(int, Object)}</li>
 *     <li>{@link #addAll(Collection)}</li>
 *     <li>{@link #addAll(int, Collection)}</li>
 *     <li>{@link #addAll(Object[])}</li>
 *     <li>{@link #addAll(Iterable)}</li>
 *     <li>{@link #addAll(Iterator)}</li>
 *     <li>{@link #addAllMatching(Object[], Matcher)}</li>
 *     <li>{@link #addAllMatching(Iterable, Matcher)}</li>
 *     <li>{@link #addAllMatching(Iterable, Matcher)}</li>
 *     <li>{@link #addAllMatching(Collection, Matcher)}</li>
 *     <li>{@link #append(Object)}</li>
 *     <li>{@link #appendIfNotNull(Object)}</li>
 *     <li>{@link #appendAll(Collection)}</li>
 *     <li>{@link #appendAll(Iterable)}</li>
 *     <li>{@link #appendAll(Iterator)}</li>
 *     <li>{@link #appendAll(Object[])}</li>
 *     <li>{@link #appendThen(Object)}</li>
 *     <li>{@link #appendThen(Iterable)}</li>
 *     <li>{@link #prepend(Object)}</li>
 *     <li>{@link #prependIfNotNull(Object)}</li>
 *     <li>{@link #prependAll(Collection)}</li>
 *     <li>{@link #prependAll(Iterable)}</li>
 *     <li>{@link #prependAll(Object[])}</li>
 *     <li>{@link #push(Object)}</li>
 * </ul>
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link #first()}</li>
 *     <li>{@link #first(Count)}</li>
 *     <li>{@link #first(int)}</li>
 *     <li>{@link #get(int)}</li>
 *     <li>{@link #last()}</li>
 *     <li>{@link #last(Count)}</li>
 *     <li>{@link #last(int)}</li>
 *     <li>{@link #leftOf(int)}</li>
 *     <li>{@link #mapped(Function)}</li>
 *     <li>{@link #matching(Matcher)}</li>
 *     <li>{@link #pop()}</li>
 *     <li>{@link #rightOf(int)}</li>
 *     <li>{@link #set(int, Object)}</li>
 *     <li>{@link #subList(int, int)}</li>
 * </ul>
 *
 * <p><b>Membership</b></p>
 *
 * <ul>
 *     <li>{@link #contains(Object)}</li>
 *     <li>{@link #containsAll(Collection)}</li>
 * </ul>
 *
 * <p><b>Size</b></p>
 *
 * <ul>
 *     <li>{@link #size()}</li>
 *     <li>{@link #count()}</li>
 *     <li>{@link #isEmpty()}</li>
 *     <li>{@link #isNonEmpty()}</li>
 * </ul>
 *
 * <p><b>Bounds</b></p>
 *
 * <ul>
 *     <li>{@link #maximumSize()} - The maximum size of this list</li>
 *     <li>{@link #totalRoom()} - The maximum size of this list</li>
 *     <li>{@link #hasRoomFor(int)} - For use by subclasses to check their size</li>
 *     <li>{@link #onOutOfRoom(int)} - Responds with a warning when the list is out of space</li>
 * </ul>
 *
 * <p><b>Removing</b></p>
 *
 * <ul>
 *     <li>{@link #clear()}</li>
 *     <li>{@link #remove(int)}</li>
 *     <li>{@link #remove(Object)}</li>
 *     <li>{@link #removeLast()}</li>
 *     <li>{@link #removeAll(Collection)}</li>
 *     <li>{@link #removeIf(Predicate)}</li>
 *     <li>{@link #removeAllMatching(Matcher)}</li>
 * </ul>
 *
 * <p><b>Search/Replace</b></p>
 *
 * <ul>
 *     <li>{@link #indexOf(Object)}</li>
 *     <li>{@link #lastIndexOf(Object)}</li>
 *     <li>{@link #replaceAll(Object, Object)}</li>
 *     <li>{@link #replaceAll(UnaryOperator)}</li>
 * </ul>
 *
 * <p><b>Tests</b></p>
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
 *     <li>{@link #asIterable()}</li>
 *     <li>{@link #asIterable(Matcher)}</li>
 *     <li>{@link #asIterator()}</li>
 *     <li>{@link #asIterator(Matcher)}</li>
 *     <li>{@link #asSet()}</li>
 *     <li>{@link #asString(Format)}</li>
 *     <li>{@link #asStringList()}</li>
 * </ul>
 *
 * <p><b>String Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #bracketed()}</li>
 *     <li>{@link #bracketed(int)}</li>
 *     <li>{@link #bulleted()} - The elements in this list as a bulleted string, with on element to a line</li>
 *     <li>{@link #bulleted(int)} - An indented bullet list of the elements in this list</li>
 *     <li>{@link #join()} - This list joined by the list {@link #separator()}</li>
 *     <li>{@link #separator()} - The separator used when joining this list into a string</li>
 *     <li>{@link #titledBox(String)}</li>
 *     <li>{@link #titledBox(String, Object...)}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #shuffle()}</li>
 *     <li>{@link #shuffle(Random)}</li>
 *     <li>{@link #sort(Comparator)}</li>
 *     <li>{@link #sorted()}</li>
 *     <li>{@link #sorted(Comparator)}</li>
 *     <li>{@link #uniqued()}</li>
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
 *     <li>{@link #sorted()}</li>
 *     <li>{@link #sorted(Comparator)}</li>
 *     <li>{@link #reversed()} - This list reversed</li>
 *     <li>{@link #maybeReversed(boolean)} - This list reversed if the given boolean is true</li>
 *     <li>{@link #uniqued()}</li>
 *     <li>{@link #with(Object)}</li>
 *     <li>{@link #without(Matcher)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Addable
 * @see Appendable
 * @see Countable
 * @see Indexable
 * @see Joinable
 * @see List
 * @see Prependable
 * @see RandomAccess
 * @see Sectionable
 * @see StringFormattable
 * @see WriteIndexable
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCollections.class, excludeAllSuperTypes = true)
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = MORE_TESTING_NEEDED,
            documentation = FULLY_DOCUMENTED)
public abstract class BaseList<Value> extends BaseCollection<Value> implements
        List<Value>,
        WriteIndexable<Value>,
        Sequence<Value>,
        Sectionable<Value, BaseList<Value>>,
        Joinable<Value>,
        Appendable<Value>,
        Prependable<Value>,
        RandomAccess,
        Countable,
        StringFormattable
{
    /** Initial list implementation while mutable */
    private final List<Value> list;

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
        super(maximumSize);

        // If we have room for the list,
        if (hasRoomFor(list.size()))
        {
            // save it.
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
        return (BaseList<Value>) super.appendThen(value);
    }

    @Override
    public BaseList<Value> appendThen(Iterable<? extends Value> values)
    {
        return (BaseList<Value>) super.appendThen(values);
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

    @Override
    public BaseList<Value> copy()
    {
        return (BaseList<Value>) super.copy();
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
    public int indexOf(Object element)
    {
        return list.indexOf(element);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseList<Value> leftOf(int index)
    {
        return Sectionable.super.leftOf(index);
    }

    /**
     * {@inheritDoc}
     */
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
     * @return This list reversed if reverse is true, or the list itself if it is false
     */
    public BaseList<Value> maybeReversed(boolean reverse)
    {
        return reverse ? reversed() : this;
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
        return isEmpty() ? null : removeLast();
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
        var sorted = newList();
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
     * @return This list of objects as an ASCII art text box with the given title
     */
    public String titledBox(String title)
    {
        return AsciiArt.textBox(title, join("\n"));
    }

    /**
     * @return This list of objects as an ASCII art text box with the given title
     */
    public String titledBox(String title, Object... arguments)
    {
        return titledBox(Formatter.format(title, arguments));
    }

    /**
     * @return A copy of this list with only unique elements in it
     */
    @SuppressWarnings("SpellCheckingInspection")
    public BaseList<Value> uniqued()
    {
        var list = newList();
        list.addAll(asSet());
        return list;
    }

    @Override
    protected Collection<Value> collection()
    {
        return list;
    }

    /**
     * @return The wrapped list
     */
    protected List<Value> list()
    {
        return list;
    }

    protected BaseList<Value> newList()
    {
        return (BaseList<Value>) newCollection();
    }

    @Override
    protected abstract BaseList<Value> onNewCollection();
}
