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

package com.telenav.kivakit.kernel.language.collections.list;

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.interfaces.collection.Addable;
import com.telenav.kivakit.kernel.interfaces.collection.Appendable;
import com.telenav.kivakit.kernel.interfaces.collection.Indexable;
import com.telenav.kivakit.kernel.interfaces.collection.Prependable;
import com.telenav.kivakit.kernel.interfaces.collection.Sequence;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.kernel.interfaces.numeric.Sized;
import com.telenav.kivakit.kernel.interfaces.value.NewInstance;
import com.telenav.kivakit.kernel.language.collections.CompressibleCollection;
import com.telenav.kivakit.kernel.language.iteration.BaseIterator;
import com.telenav.kivakit.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.kernel.language.strings.StringTo;
import com.telenav.kivakit.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramExampleBaseList;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsList;
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
import java.util.RandomAccess;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A base class for bounded lists which adds a number of convenient methods as well as support for various KivaKit
 * interfaces, including {@link Indexable}, {@link Sequence}, {@link Addable}, {@link java.lang.Appendable}, {@link
 * Sized} and {@link CompressibleCollection}. Some of the added convenience methods include:
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
 *     <li>{@link #filtered(Matcher)} - A copy of this list filtered to matching elements</li>
 *     <li>{@link #mapped(Function)} - A copy of this list with elements mapped to another type</li>
 *     <li>{@link #sorted(Comparator)} - A copy of this list sorted by the given comparator</li>
 *     <li>{@link #reversed()} - This list reversed</li>
 *     <li>{@link #maybeReversed(boolean)} - This list reversed if the given boolean is true</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see NewInstance
 * @see List
 * @see Indexable
 * @see Addable
 * @see java.lang.Appendable
 * @see CompressibleCollection
 * @see RandomAccess
 * @see AsString
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsList.class, excludeAllSuperTypes = true)
@UmlClassDiagram(diagram = DiagramExampleBaseList.class)
public abstract class BaseList<Element> implements
        NewInstance<BaseList<Element>>,
        List<Element>,
        Indexable<Element>,
        Addable<Element>,
        Appendable<Element>,
        Prependable<Element>,
        CompressibleCollection,
        RandomAccess,
        AsString
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /** Initial list implementation while mutable */
    private List<Element> list;

    /** Array store when frozen */
    private Element[] array;

    /** The maximum size of this bounded list */
    private int maximumSize;

    /** True if the list has run out of room */
    private boolean outOfRoom;

    /**
     * @param maximumSize The maximum size of this list
     */
    protected BaseList(final Maximum maximumSize)
    {
        this.maximumSize = maximumSize.asInt();
        list = new ArrayList<>();
    }

    /**
     * @param maximumSize The maximum size of this list
     * @param list The list implementation to use
     */
    protected BaseList(final Maximum maximumSize, final List<Element> list)
    {
        this.maximumSize = maximumSize.asInt();
        this.list = list;
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
    protected BaseList(final List<Element> list)
    {
        this(Maximum.MAXIMUM, list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final int index, final Element element)
    {
        if (checkSizeIncrease(1))
        {
            modify();
            list.add(index, element);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(final Element element)
    {
        if (checkSizeIncrease(1))
        {
            modify();
            return list.add(element);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(final Collection<? extends Element> elements)
    {
        if (checkSizeIncrease(elements.size()))
        {
            modify();
            return list.addAll(elements);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends Element> collection)
    {
        if (checkSizeIncrease(collection.size()))
        {
            modify();
            return list.addAll(index, collection);
        }
        return false;
    }

    /**
     * Adds the given elements to this bounded list
     */
    public boolean addAll(final Element[] elements)
    {
        if (checkSizeIncrease(elements.length))
        {
            list.addAll(Arrays.asList(elements));
            return true;
        }
        return false;
    }

    /**
     * Adds the given element if it is not null
     */
    public boolean addIfNotNull(final Element element)
    {
        if (element != null)
        {
            return add(element);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseList<Element> append(final Element element)
    {
        add(element);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseList<Element> appendAll(final Iterable<? extends Element> elements)
    {
        addAll(elements);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseList<Element> appendAll(final Iterator<? extends Element> elements)
    {
        while (elements.hasNext())
        {
            add(elements.next());
        }
        return this;
    }

    public BaseList<Element> appendAll(final Element[] elements)
    {
        addAll(elements);
        return this;
    }

    /**
     * @return This list as an array
     */
    @SuppressWarnings({ "unchecked" })
    public Element[] asArray(final Class<Element> type)
    {
        final var array = (Element[]) Array.newInstance(type, size());
        toArray(array);
        return array;
    }

    @Override
    public @NotNull Iterator<Element> asIterator(final Matcher<Element> matcher)
    {
        return new BaseIterator<>()
        {
            int index = 0;

            @Override
            protected Element onNext()
            {
                while (index < size())
                {
                    final var element = get(index++);
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
    public @NotNull Iterator<Element> asIterator()
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
    @Override
    public String asString(final StringFormat format)
    {
        switch (format.identifier())
        {
            case StringFormat.DEBUGGER_IDENTIFIER:
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

    /**
     * @return The items in this list in a bulleted ASCII art representation
     */
    public String bulleted()
    {
        return AsciiArt.bulleted(this);
    }

    /**
     * @return The items in this list in a bulleted ASCII art representation with the given indent
     */
    public String bulleted(final int indent)
    {
        return AsciiArt.bulleted(indent, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear()
    {
        modify();
        list.clear();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Method compress(final Method method)
    {
        if (!isCompressed())
        {
            switch (method)
            {
                case FREEZE:
                {
                    array = (Element[]) new Object[size()];
                    var i = 0;
                    for (final var element : list)
                    {
                        array[i++] = element;
                    }
                    list = null;
                    return method;
                }

                case RESIZE:
                {
                    list = new ArrayList<>(list);
                    return method;
                }
            }
        }

        return compressionMethod();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Method compressionMethod()
    {
        return array != null ? Method.FREEZE : Method.RESIZE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(final Object that)
    {
        if (compressionMethod() == Method.FREEZE)
        {
            for (final var element : array)
            {
                if (that.equals(element))
                {
                    return true;
                }
            }
            return false;
        }
        else
        {
            return list.contains(that);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(final @NotNull Collection<?> collection)
    {
        if (compressionMethod() == Method.FREEZE)
        {
            for (final Object element : collection)
            {
                if (!contains(element))
                {
                    return false;
                }
            }
            return true;
        }
        else
        {
            return list.containsAll(collection);
        }
    }

    /**
     * @return A copy of this list
     */
    public BaseList<Element> copy()
    {
        final var copy = newInstance();
        copy.addAll(this);
        return copy;
    }

    /**
     * @return True if this list ends with the given list
     */
    public boolean endsWith(final BaseList<Element> that)
    {
        return that != null && reversed().startsWith(that.reversed());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof List)
        {
            final var that = (List<?>) object;

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
     * @return This bounded list filtered to only the elements that match the given matcher
     */
    public BaseList<Element> filtered(final Matcher<Element> matcher)
    {
        final var filtered = newInstance();
        filtered.addAll(asIterable(matcher));
        return filtered;
    }

    /**
     * @return The first n elements in this list
     */
    public BaseList<Element> first(final Count count)
    {
        return first(count.asInt());
    }

    /**
     * @return The first n elements in this list
     */
    public BaseList<Element> first(final int count)
    {
        final var list = newInstance();
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
    public Element get(final int index)
    {
        if (compressionMethod() == Method.FREEZE)
        {
            return array[index];
        }
        else
        {
            return list.get(index);
        }
    }

    @Override
    public int hashCode()
    {
        return asHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int indexOf(final Object element)
    {
        if (compressionMethod() == Method.FREEZE)
        {
            for (var i = 0; i < size(); i++)
            {
                if (element.equals(array[i]))
                {
                    return i;
                }
            }
            return -1;
        }
        else
        {
            return list.indexOf(element);
        }
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
        if (compressionMethod() == Method.FREEZE)
        {
            final var outer = this;
            return new BaseIterator<>()
            {
                private int index;

                @Override
                protected Element onNext()
                {
                    if (index < size())
                    {
                        return outer.array[index++];
                    }
                    return null;
                }
            };
        }
        else
        {
            return list.iterator();
        }
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
    public int lastIndexOf(final Object element)
    {
        if (compressionMethod() == Method.FREEZE)
        {
            for (var i = size() - 1; i >= 0; i--)
            {
                if (element.equals(array[i]))
                {
                    return i;
                }
            }
            return -1;
        }
        else
        {
            return list.lastIndexOf(element);
        }
    }

    /**
     * @return The elements in this list to the left of the index, exclusive
     */
    public BaseList<Element> leftOf(final int index)
    {
        final var left = newInstance();
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
    public ListIterator<Element> listIterator(final int index)
    {
        if (compressionMethod() == Method.FREEZE)
        {
            return Ensure.unsupported();
        }
        else
        {
            return list.listIterator(index);
        }
    }

    /**
     * @return This bounded list with all elements mapped by the given mapper to the mapper's target type
     */
    @SuppressWarnings("unchecked")
    public <Target> BaseList<Target> mapped(final Function<Element, Target> mapper)
    {
        final var filtered = (BaseList<Target>) newInstance();
        for (final var element : asIterable())
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
    public BaseList<Element> maybeReversed(final boolean reverse)
    {
        return reverse ? reversed() : this;
    }

    @Override
    public BaseList<Element> newInstance()
    {
        final var instance = onNewInstance();
        instance.maximumSize = maximumSize;
        return instance;
    }

    /**
     * Prepends the given element to the front of this list
     */
    @Override
    public BaseList<Element> prepend(final Element element)
    {
        modify();
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

    /**
     * @return This list of elements as quantized values or a cast exception if the elements are not {@link Quantizable}
     */
    public long[] quantized()
    {
        final var quantized = new long[size()];
        var i = 0;
        for (final var object : this)
        {
            quantized[i++] = ((Quantizable) object).quantum();
        }
        return quantized;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element remove(final int index)
    {
        modify();
        return list.remove(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(final Object element)
    {
        modify();
        return list.remove(element);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public boolean removeAll(final Collection<?> collection)
    {
        modify();
        return list.removeAll(collection);
    }

    /**
     * @return Removes the last element in this list
     */
    public Element removeLast()
    {
        modify();
        if (!isEmpty())
        {
            return remove(size() - 1);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public boolean retainAll(final Collection<?> collection)
    {
        modify();
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
        final var copy = copy();
        copy.reverse();
        return copy;
    }

    /**
     * @return The elements in this list to the right of the index, exclusive
     */
    public BaseList<Element> rightOf(final int index)
    {
        final var right = newInstance();
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
    public Element set(final int index, final Element element)
    {
        modify();
        return list.set(index, element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        if (compressionMethod() == Method.FREEZE)
        {
            return array.length;
        }
        else
        {
            return list.size();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sort(final Comparator<? super Element> comparator)
    {
        modify();
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
    public BaseList<Element> sorted(final Comparator<Element> comparator)
    {
        final var sorted = newInstance();
        sorted.addAll(this);
        sorted.sort(comparator);
        return sorted;
    }

    /**
     * @return True if this list starts with the given list
     */
    public boolean startsWith(final BaseList<Element> that)
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
    public List<Element> subList(final int fromIndex, final int toIndex)
    {
        if (compressionMethod() == Method.FREEZE)
        {
            return Arrays.stream(array, fromIndex, toIndex).collect(Collectors.toList());
        }
        else
        {
            return list.subList(fromIndex, toIndex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray()
    {
        if (compressionMethod() == Method.FREEZE)
        {
            return array;
        }
        else
        {
            return list.toArray();
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "SuspiciousToArrayCall", "SuspiciousSystemArraycopy" })
    @Override
    public <E> E[] toArray(final E[] array)
    {
        if (compressionMethod() == Method.FREEZE)
        {
            System.arraycopy(this.array, 0, array, 0, size());
            return array;
        }
        else
        {
            return list.toArray(array);
        }
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
    public BaseList<Element> uniqued()
    {
        final var list = newInstance();
        list.addAll(asSet());
        return list;
    }

    /**
     * @return This list without the matching elements
     */
    public BaseList<Element> without(final Matcher<Element> matcher)
    {
        final var iterator = iterator();
        final var removed = newInstance();
        while (iterator.hasNext())
        {
            final var element = iterator.next();
            if (matcher.matches(element))
            {
                removed.add(element);
                iterator.remove();
            }
        }
        return removed;
    }

    /**
     * @return True if the given size increase is acceptable, false if not
     */
    protected boolean checkSizeIncrease(final int increase)
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
        LOGGER.warning(new Throwable(), "Maximum size of ${debug} elements would have been exceeded. Ignoring operation.", maximumSize);
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
    protected String toString(final Element value)
    {
        return StringTo.string(value);
    }

    /**
     * Checks the compression method for this list and fails if the list is frozen
     */
    private void modify()
    {
        if (compressionMethod() == Method.FREEZE)
        {
            Ensure.fail("Cannot modify frozen list");
        }
    }
}
