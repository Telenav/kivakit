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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.BaseCollection;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.string.AsciiArt.TextBoxStyle;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.collection.Appendable;
import com.telenav.kivakit.interfaces.collection.Copyable;
import com.telenav.kivakit.interfaces.collection.Indexable;
import com.telenav.kivakit.interfaces.collection.Prependable;
import com.telenav.kivakit.interfaces.collection.Sectionable;
import com.telenav.kivakit.interfaces.comparison.Matcher;
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
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.string.AsciiArt.textBox;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;

/**
 * A base class for bounded lists which adds convenient methods as well as support for various KivaKit interfaces:
 *
 * <ul>
 *     <li>{@link Appendable}</li>
 *     <li>{@link Copyable}</li>
 *     <li>{@link Indexable}</li>
 *     <li>{@link List}</li>
 *     <li>{@link Prependable}</li>
 *     <li>{@link RandomAccess}</li>
 *     <li>{@link Sectionable}</li>
 * </ul>
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
 *     <li>{@link #appending(Object)}</li>
 *     <li>{@link #appending(Iterable)}</li>
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
 *     <li>{@link #map(Function)}</li>
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
 * <p>
 * Some methods are functional and return a new list. The method {@link #newList()} is used to create lists.
 * Subclasses create the subclass list type by overriding {@link #onNewList()}.
 * </p>
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
 *     <li>{@link #map(Function)} - A copy of this list with elements mapped to another type</li>
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
 * @see Appendable
 * @see Copyable
 * @see Indexable
 * @see List
 * @see Prependable
 * @see RandomAccess
 * @see Sectionable
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCollections.class, excludeAllSuperTypes = true)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTED)
public abstract class BaseList<Value> extends BaseCollection<Value> implements
    Appendable<Value>,
    Copyable<Value, BaseList<Value>>,
    Indexable<Value>,
    List<Value>,
    Prependable<Value>,
    RandomAccess,
    Sectionable<Value, BaseList<Value>>
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

        // If we have room for the list at all,
        if (list.size() < maximumSize.asInt())
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
        this(MAXIMUM);
    }

    /**
     * An unbounded list with the given list implementation
     */
    protected BaseList(Collection<Value> collection)
    {
        this(MAXIMUM, collection);
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
    public BaseList<Value> appending(Iterable<? extends Value> values)
    {
        return (BaseList<Value>) Appendable.super.appending(values);
    }

    @Override
    public BaseList<Value> appending(Value value)
    {
        return (BaseList<Value>) Appendable.super.appending(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseList<Value> appendingIfNotNull(Value value)
    {
        return (BaseList<Value>) Appendable.super.appendingIfNotNull(value);
    }

    /**
     * Returns the items in this list in a bulleted ASCII art representation
     */
    public String bulleted()
    {
        return bulleted(4);
    }

    /**
     * Returns the items in this list in a bulleted ASCII art representation with the given indent
     */
    public String bulleted(int indent)
    {
        return AsciiArt.bulleted(indent, this);
    }

    @Override
    public BaseList<Value> copy()
    {
        return Copyable.super.copy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof List<?> that)
        {
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
     * Returns the first count elements of this list
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
     * Returns the last count values of this list
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
     * Returns this list reversed if reverse is true, or the list itself if it is false
     */
    public BaseList<Value> maybeReversed(boolean reverse)
    {
        return reverse ? reversed() : this;
    }

    /**
     * Returns this list as a numbered string list
     *
     * @return The string list
     */
    public StringList numbered()
    {
        return asStringList().numbered();
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
     * {@inheritDoc}
     */
    @Override
    public BaseList<Value> onNewInstance()
    {
        return newList();
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
     * {@inheritDoc}
     */
    @Override
    public Stream<Value> parallelStream()
    {
        return list.parallelStream();
    }

    /**
     * Returns the last value in this list, after removing it
     */
    public Value pop()
    {
        return isEmpty() ? null : removeLast();
    }

    @Override
    public BaseList<Value> prepending(Iterable<? extends Value> values)
    {
        return (BaseList<Value>) Prependable.super.prepending(values);
    }

    @Override
    public BaseList<Value> prepending(Value value)
    {
        return (BaseList<Value>) Prependable.super.prepending(value);
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
    public boolean removeIf(Predicate<? super Value> filter)
    {
        return list.removeIf(filter);
    }

    /**
     * Returns removes the last element in this list
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
     * Returns this list reversed
     */
    public BaseList<Value> reversed()
    {
        var copy = copy();
        copy.reverse();
        return copy;
    }

    /**
     * Returns the values to the right of the given index
     */
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
     * {@inheritDoc}
     */
    @Override
    public Spliterator<Value> spliterator()
    {
        return list.spliterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<Value> stream()
    {
        return list.stream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> subList(int fromIndex, int toIndex)
    {
        return ObjectList.list(list.subList(fromIndex, toIndex));
    }

    /**
     * Returns this list of objects as an ASCII art text box with the given title
     */
    public String titledBox(String title)
    {
        return textBox(title, join("\n"));
    }

    public String titledBox(TextBoxStyle style, String title)
    {
        return textBox(style, title, join("\n"));
    }

    public String titledBox(TextBoxStyle style, String title, Object... arguments)
    {
        return titledBox(style, format(title, arguments));
    }

    /**
     * Returns this list of objects as an ASCII art text box with the given title
     */
    public String titledBox(String title, Object... arguments)
    {
        return titledBox(format(title, arguments));
    }

    /**
     * Returns a copy of this list with only unique elements in it
     */
    @SuppressWarnings("SpellCheckingInspection")
    public BaseList<Value> uniqued()
    {
        var list = newList();
        list.addAll(asSet());
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<Value> backingCollection()
    {
        return list;
    }

    /**
     * Returns the wrapped list
     */
    protected List<Value> backingList()
    {
        return list;
    }

    /**
     * Returns a new {@link BaseList} subclass
     */
    protected final BaseList<Value> newList()
    {
        return onNewList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BaseList<Value> onNewCollection()
    {
        return newList();
    }

    /**
     * Creates a list of the subclass type
     *
     * @return The new list
     */
    protected abstract BaseList<Value> onNewList();
}
