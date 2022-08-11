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
import com.telenav.kivakit.interfaces.collection.Indexable;
import com.telenav.kivakit.interfaces.collection.Prependable;
import com.telenav.kivakit.interfaces.collection.Sequence;
import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.numeric.Quantizable;
import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.kivakit.interfaces.value.Instantiable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Function;

/**
 * A base class for bounded lists which adds a number of convenient methods as well as support for various KivaKit
 * interfaces, including {@link Indexable}, {@link Sequence}, {@link Addable}, {@link java.lang.Appendable},and {@link
 * Sized}}. Some added convenience methods include:
 *
 * <p><b>Bounds</b></p>
 *
 * <ul>
 *     <li>{@link #maximumSize()} - The maximum size of this list</li>
 *     <li>{@link #checkSizeIncrease(int)} - For use by subclasses to check their size</li>
 *     <li>{@link #onOutOfRoom()} - Implemented by subclasses to respond when the list is out of space</li>
 * </ul>
 *
 * <p><b>Checks</b></p>
 *
 * <ul>
 *     <li>{@link #endsWith(BaseList)} - True if this list ends with the same elements as the given list</li>
 *     <li>{@link #startsWith(BaseList)} - True if this list starts with the same elements as the given list</li>
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
 * @see Instantiable
 * @see List
 * @see Indexable
 * @see Addable
 * @see java.lang.Appendable
 * @see RandomAccess
 * @see Stringable
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCollections.class, excludeAllSuperTypes = true)
public abstract class BaseList<Element> implements
        Instantiable<BaseList<Element>>,
        List<Element>,
        Indexable<Element>,
        Addable<Element>,
        Appendable<Element>,
        Prependable<Element>,
        RandomAccess,
        Countable,
        Stringable
{
    /** Initial list implementation while mutable */
    private final List<Element> list;

    /** The maximum size of this bounded list */
    private int maximumSize;

    /** True if the list has run out of room */
    private boolean outOfRoom;

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
    protected BaseList(Maximum maximumSize, Collection<Element> list)
    {
        this.maximumSize = maximumSize.asInt();
        if (list instanceof List)
        {
            this.list = (List<Element>) list;
        }
        else
        {
            this.list = new ArrayList<>(list);
        }
        checkSizeIncrease(0);
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
    protected BaseList(Collection<Element> collection)
    {
        this(Maximum.MAXIMUM, collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(int index, Element element)
    {
        if (checkSizeIncrease(1))
        {
            list.add(index, element);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(Element element)
    {
        if (checkSizeIncrease(1))
        {
            return list.add(element);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends Element> elements)
    {
        if (checkSizeIncrease(elements.size()))
        {
            return list.addAll(elements);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(int index, Collection<? extends Element> collection)
    {
        if (checkSizeIncrease(collection.size()))
        {
            return list.addAll(index, collection);
        }
        return false;
    }

    /**
     * Adds the given elements to this bounded list
     */
    public boolean addAll(Element[] elements)
    {
        if (checkSizeIncrease(elements.length))
        {
            list.addAll(Arrays.asList(elements));
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseList<Element> append(Element element)
    {
        add(element);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseList<Element> appendAll(Iterable<? extends Element> elements)
    {
        addAll(elements);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseList<Element> appendAll(Iterator<? extends Element> elements)
    {
        while (elements.hasNext())
        {
            add(elements.next());
        }
        return this;
    }

    public BaseList<Element> appendAll(Element[] elements)
    {
        addAll(elements);
        return this;
    }

    /**
     * @return This list as an array
     */
    @SuppressWarnings({ "unchecked" })
    public Element[] asArray(Class<Element> type)
    {
        var array = (Element[]) Array.newInstance(type, size());
        toArray(array);
        return array;
    }

    @Override
    public @NotNull
    Iterator<Element> asIterator(Matcher<Element> matcher)
    {
        return new BaseIterator<>()
        {
            int index = 0;

            @Override
            protected Element onNext()
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
    Iterator<Element> asIterator()
    {
        return new BaseIterator<>()
        {
            int index = 0;

            @Override
            protected Element onNext()
            {
                if (index < size())
                {
                    return get(index++);
                }
                return null;
            }
        };
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
                return toString();
        }
    }

    /**
     * @return This list as a string list
     */
    public StringList asStringList()
    {
        return new StringList(maximumSize(), this);
    }

    public String bracketed()
    {
        return bracketed(4);
    }

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
        return list.containsAll(collection);
    }

    /**
     * @return A copy of this list
     */
    public BaseList<Element> copy()
    {
        var copy = newInstance();
        copy.addAll(this);
        return copy;
    }

    /**
     * @return True if this list ends with the given list
     */
    public boolean endsWith(BaseList<Element> that)
    {
        return that != null && reversed().startsWith(that.reversed());
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
     * @return The first n elements in this list
     */
    public BaseList<Element> first(Count count)
    {
        return first(count.asInt());
    }

    /**
     * @return The first n elements in this list
     */
    public BaseList<Element> first(int count)
    {
        var list = newInstance();
        for (var i = 0; i < Math.min(count, size()); i++)
        {
            list.add(get(i));
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element get(int index)
    {
        return list.get(index);
    }

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
    public Iterator<Element> iterator()
    {
        return list.iterator();
    }

    /**
     * @return This bounded list joined as a string with the list {@link #separator()}
     */
    public final String join()
    {
        return join(separator());
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
     * @return The elements in this list to the left of the index, exclusive
     */
    public BaseList<Element> leftOf(int index)
    {
        var left = newInstance();
        for (var i = 0; i < index; i++)
        {
            left.add(get(i));
        }
        return left;
    }

    @NotNull
    @Override
    public ListIterator<Element> listIterator()
    {
        return list.listIterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<Element> listIterator(int index)
    {
        return list.listIterator(index);
    }

    /**
     * @return This bounded list with all elements mapped by the given mapper to the mapper's target type
     */
    @SuppressWarnings("unchecked")
    public <Target> BaseList<Target> mapped(Function<Element, Target> mapper)
    {
        var filtered = (BaseList<Target>) newInstance();
        for (var element : this)
        {
            filtered.add(mapper.apply(element));
        }
        return filtered;
    }

    /**
     * @return This bounded list filtered to only the elements that match the given matcher
     */
    public BaseList<Element> matching(Matcher<Element> matcher)
    {
        var filtered = newInstance();
        filtered.addAll(asIterable(matcher));
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
    public BaseList<Element> maybeReversed(boolean reverse)
    {
        return reverse ? reversed() : this;
    }

    @Override
    public BaseList<Element> newInstance()
    {
        var instance = onNewInstance();
        instance.maximumSize = maximumSize;
        return instance;
    }

    public Element pop()
    {
        return removeLast();
    }

    /**
     * Prepends the given element to the front of this list
     */
    @Override
    public BaseList<Element> prepend(Element element)
    {
        if (isEmpty())
        {
            add(element);
        }
        else
        {
            add(0, element);
        }
        return this;
    }

    public void push(Element element)
    {
        append(element);
    }

    /**
     * @return This list of elements as quantized values or a cast exception if the elements are not {@link Quantizable}
     */
    public long[] quantized()
    {
        var quantized = new long[size()];
        var i = 0;
        for (var object : this)
        {
            quantized[i++] = ((Quantizable) object).quantum();
        }
        return quantized;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element remove(int index)
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
    public Element removeLast()
    {
        if (!isEmpty())
        {
            return remove(size() - 1);
        }
        return null;
    }

    /**
     * Replace all occurrences of the given element with the given replacement
     *
     * @param element The element to replace
     * @param replacement The element to substitute
     */
    public boolean replaceAll(final Element element, final Element replacement)
    {
        return Collections.replaceAll(list, element, replacement);
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
    public BaseList<Element> reversed()
    {
        var copy = copy();
        copy.reverse();
        return copy;
    }

    /**
     * @return The elements in this list to the right of the index, exclusive
     */
    public BaseList<Element> rightOf(int index)
    {
        var right = newInstance();
        for (var i = index + 1; i < size(); i++)
        {
            right.add(get(i));
        }
        return right;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element set(int index, Element element)
    {
        return list.set(index, element);
    }

    public void shuffle()
    {
        shuffle(new Random());
    }

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
    public void sort(Comparator<? super Element> comparator)
    {
        list.sort(comparator);
    }

    /**
     * @return This list sorted by casting the element type to {@link Comparable}. If the elements in the list are not
     * comparable, an exception will be thrown.
     */
    @SuppressWarnings("unchecked")
    public BaseList<Element> sorted()
    {
        return sorted((Element a, Element b) -> ((Comparable<Element>) a).compareTo(b));
    }

    /**
     * @return A copy of this list sorted by the given comparator
     */
    public BaseList<Element> sorted(Comparator<Element> comparator)
    {
        var sorted = newInstance();
        sorted.addAll(this);
        sorted.sort(comparator);
        return sorted;
    }

    /**
     * @return True if this list starts with the given list
     */
    public boolean startsWith(BaseList<Element> that)
    {
        if (that == null || that.size() > size())
        {
            return false;
        }
        else
        {
            for (var i = 0; i < that.size(); i++)
            {
                if (!get(i).equals(that.get(i)))
                {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Element> subList(int fromIndex, int toIndex)
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
        return join();
    }

    /**
     * @return A copy of this list with only unique elements in it
     */
    @SuppressWarnings("SpellCheckingInspection")
    public BaseList<Element> uniqued()
    {
        var list = newInstance();
        list.addAll(asSet());
        return list;
    }

    /**
     * @return This list without the matching elements
     */
    public BaseList<Element> without(Matcher<Element> matcher)
    {
        var iterator = iterator();
        var without = newInstance();
        while (iterator.hasNext())
        {
            var element = iterator.next();
            if (!matcher.matches(element))
            {
                without.add(element);
            }
        }
        return without;
    }

    /**
     * @return True if the given size increase is acceptable, false if not
     */
    protected boolean checkSizeIncrease(int increase)
    {
        if (size() + increase > maximumSize)
        {
            if (!outOfRoom)
            {
                onOutOfRoom();
                outOfRoom = true;
            }
            return false;
        }
        else
        {
            outOfRoom = false;
            return true;
        }
    }

    /**
     * Called when a bounded list runs out of room
     */
    protected void onOutOfRoom()
    {
        Ensure.warning("Maximum size of " + maximumSize + " elements would have been exceeded. Ignoring operation: " + new Throwable());
    }

    /**
     * @return The separator to use when joining this list
     */
    protected String separator()
    {
        return ", ";
    }

    /**
     * Convert the given value to a string
     *
     * @param value The value
     * @return A string corresponding to the value
     */
    protected String toString(Element value)
    {
        return StringTo.string(value);
    }
}
