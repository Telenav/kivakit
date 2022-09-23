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

import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.factory.IntMapFactory;
import com.telenav.kivakit.interfaces.factory.LongMapFactory;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;

/**
 * A bounded list of objects with overrides of methods from {@link BaseList} to downcast return values to
 * {@link ObjectList} for convenience. New instances of {@link ObjectList} are created by {@link BaseList} by calling
 * {@link #onNewInstance()}, allowing functional logic to reside in the base class.
 *
 * <p><b>Partitioning</b></p>
 *
 * <ul>
 *     <li>{@link #partition(Count)} - The elements in this list partitioned into N new lists</li>
 * </ul>
 *
 * <p>
 * The methods {@link #objectList(Object[])} and {@link #objectList(Maximum, Object[])} can be used to construct constant lists.
 * The factory methods {@link #objectListFromInts(IntMapFactory, int...)} and {@link #objectListFromLongs(LongMapFactory, long...)}
 * construct lists of objects from integer and long values using the given map factories to convert the values into
 * objects. The method {@link #objectList(Iterable, LongMapFactory)} iterates through the given {@link LongValued} object
 * values, passing each quantum to the given primitive map factory and adding the resulting object to a new object list.
 * </p>
 *
 * @param <Element> The object type
 * @author jonathanl (shibo)
 * @see BaseList
 * @see LongMapFactory
 * @see LongMapFactory
 * @see IntMapFactory
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCollections.class)
public class ObjectList<Element> extends BaseList<Element>
{
    /**
     * @return An empty object list
     */
    public static <T> ObjectList<T> emptyList()
    {
        return new ObjectList<>(Maximum._0);
    }

    /**
     * @return A list of objects from the given iterable
     */
    public static <T> ObjectList<T> objectList(Iterable<T> values)
    {
        return new ObjectList<T>().appendAll(values);
    }

    /**
     * @return A list of elements from the given integers created using the given map factory
     */
    public static <T> ObjectList<T> objectList(Iterable<LongValued> values, LongMapFactory<T> factory)
    {
        var objects = new ObjectList<T>();
        for (var value : values)
        {
            objects.add(factory.newInstance(value.longValue()));
        }
        return objects;
    }

    /**
     * @return The given list of objects with a maximum size
     */
    @SafeVarargs
    public static <T> ObjectList<T> objectList(Maximum maximumSize, T... objects)
    {
        var list = new ObjectList<T>(maximumSize);
        list.addAll(Arrays.asList(objects));
        return list;
    }

    /**
     * @return The given list of objects
     */
    @SafeVarargs
    public static <T> ObjectList<T> objectList(T... objects)
    {
        return objectList(Maximum._1024, objects);
    }

    public static <T> ObjectList<T> objectListFromArray(T[] objects)
    {
        var list = new ObjectList<T>();
        list.addAll(objects);
        return list;
    }

    public static ObjectList<Long> objectListFromArray(long[] objects)
    {
        var list = new ObjectList<Long>();
        for (var at : objects)
        {
            list.add(at);
        }
        return list;
    }

    /**
     * @return A list of elements from the given integers created using the given map factory
     */
    public static <T> ObjectList<T> objectListFromInts(IntMapFactory<T> factory, int... values)
    {
        var objects = new ObjectList<T>();
        for (var value : values)
        {
            objects.add(factory.newInstance(value));
        }
        return objects;
    }

    /**
     * @return A list of elements from the given integers created using the given map factory
     */
    public static <T> ObjectList<T> objectListFromLongs(LongMapFactory<T> factory, long... values)
    {
        var objects = new ObjectList<T>();
        for (var value : values)
        {
            objects.add(factory.newInstance(value));
        }
        return objects;
    }

    /**
     * An unbounded object list
     */
    public ObjectList()
    {
        this(Maximum.MAXIMUM);
    }

    /**
     * A list of objects with the given upper bound
     */
    public ObjectList(Maximum maximumSize)
    {
        super(maximumSize);
    }

    /**
     * A list of objects with the given upper bound
     */
    public ObjectList(Collection<Element> collection)
    {
        super(collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Element> append(Element element)
    {
        super.append(element);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Element> appendAll(Iterable<? extends Element> objects)
    {
        super.appendAll(objects);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Element> appendAll(Iterator<? extends Element> objects)
    {
        super.appendAll(objects);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Element> appendAll(Element[] objects)
    {
        super.appendAll(objects);
        return this;
    }

    @Override
    public StringList asStringList()
    {
        return StringList.stringList(this);
    }

    /**
     * Returns a copy of this list
     *
     * @return The copy
     */
    @Override
    public ObjectList<Element> copy()
    {
        return (ObjectList<Element>) super.copy();
    }

    @Override
    public ObjectList<Element> first(Count count)
    {
        return (ObjectList<Element>) super.first(count);
    }

    @Override
    public ObjectList<Element> first(int count)
    {
        return (ObjectList<Element>) super.first(count);
    }

    @Override
    public ObjectList<Element> leftOf(int index)
    {
        return (ObjectList<Element>) super.leftOf(index);
    }

    @Override
    public <To> ObjectList<To> mapped(Function<Element, To> mapper)
    {
        return (ObjectList<To>) super.mapped(mapper);
    }

    /**
     * Returns a new list containing the elements in this list that match the given matcher.
     *
     * @param matcher The matcher to use
     * @return The list of elements matching the matcher
     */
    @Override
    public ObjectList<Element> matching(Matcher<Element> matcher)
    {
        return (ObjectList<Element>) super.matching(matcher);
    }

    @Override
    public ObjectList<Element> maybeReversed(boolean reverse)
    {
        return (ObjectList<Element>) super.maybeReversed(reverse);
    }

    /**
     * Creates an empty new list. Subclasses can return a subclass list type.
     *
     * @return The new list
     */
    @Override
    public ObjectList<Element> onNewInstance()
    {
        return new ObjectList<>();
    }

    /**
     * @return This object list partitioned in to n object lists
     */
    public ObjectList<ObjectList<Element>> partition(Count partitions)
    {
        var lists = new ObjectList<ObjectList<Element>>(maximumSize());
        var i = 0;
        var list = -1;
        var every = (int) Math.round((double) size() / (double) partitions.asInt());
        for (var object : this)
        {
            if (i++ % every == 0 && list < partitions.asInt() - 1)
            {
                lists.add(new ObjectList<>(Maximum.MAXIMUM));
                list++;
            }
            lists.get(list).add(object);
        }
        return lists;
    }

    @Override
    public ObjectList<Element> prepend(Element element)
    {
        return (ObjectList<Element>) super.prepend(element);
    }

    @Override
    public ObjectList<Element> reversed()
    {
        return (ObjectList<Element>) super.reversed();
    }

    @Override
    public ObjectList<Element> rightOf(int index)
    {
        return (ObjectList<Element>) super.rightOf(index);
    }

    @Override
    public ObjectList<Element> sorted()
    {
        return (ObjectList<Element>) super.sorted();
    }

    @Override
    public ObjectList<Element> sorted(Comparator<Element> comparator)
    {
        return (ObjectList<Element>) super.sorted(comparator);
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
     * Creates a list containing the unique objects in this list
     *
     * @return The unique objects
     */
    @Override
    @SuppressWarnings("SpellCheckingInspection")
    public ObjectList<Element> uniqued()
    {
        return (ObjectList<Element>) super.uniqued();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> ObjectList<T> with(T value)
    {
        var copy = new ObjectList();
        copy.addAll(this);
        copy.add(value);
        return copy;
    }

    @Override
    public ObjectList<Element> without(Matcher<Element> matcher)
    {
        return (ObjectList<Element>) super.without(matcher);
    }
}
