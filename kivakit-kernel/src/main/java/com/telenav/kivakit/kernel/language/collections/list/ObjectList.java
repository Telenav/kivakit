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

import com.telenav.kivakit.kernel.data.conversion.string.StringConverter;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.interfaces.factory.IntMapFactory;
import com.telenav.kivakit.kernel.interfaces.factory.LongMapFactory;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageCollectionsList;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;

/**
 * A bounded list of objects with overrides of methods from {@link BaseList} to downcast return values to {@link
 * ObjectList} for convenience. New instances of {@link ObjectList} are created by {@link BaseList} by calling {@link
 * #onNewInstance()}, allowing functional logic to reside in the base class.
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
 * objects. The method {@link #objectList(Iterable, LongMapFactory)} iterates through the given {@link Quantizable}
 * values, passing each quantum to the given primitive map factory and adding the resulting object to a new object list.
 * </p>
 *
 * @param <Element> The object type
 * @author jonathanl (shibo)
 * @see BaseList
 * @see Quantizable
 * @see LongMapFactory
 * @see LongMapFactory
 * @see IntMapFactory
 */
@UmlClassDiagram(diagram = DiagramLanguageCollectionsList.class)
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
    public static <T> ObjectList<T> objectList(final Iterable<T> values)
    {
        return new ObjectList<T>().appendAll(values);
    }

    /**
     * @return A list of elements from the given integers created using the given map factory
     */
    public static <T> ObjectList<T> objectList(final Iterable<Quantizable> values, final LongMapFactory<T> factory)
    {
        final var objects = new ObjectList<T>();
        for (final var value : values)
        {
            objects.add(factory.newInstance(value.quantum()));
        }
        return objects;
    }

    /**
     * @return The given list of objects with a maximum size
     */
    @SafeVarargs
    public static <T> ObjectList<T> objectList(final Maximum maximumSize, final T... objects)
    {
        final var list = new ObjectList<T>(maximumSize);
        list.addAll(Arrays.asList(objects));
        return list;
    }

    /**
     * @return The given list of objects
     */
    @SafeVarargs
    public static <T> ObjectList<T> objectList(final T... objects)
    {
        return objectList(Maximum._1024, objects);
    }

    /**
     * @return A list of elements from the given integers created using the given map factory
     */
    public static <T> ObjectList<T> objectListFromInts(final IntMapFactory<T> factory, final int... values)
    {
        final var objects = new ObjectList<T>();
        for (final var value : values)
        {
            objects.add(factory.newInstance(value));
        }
        return objects;
    }

    /**
     * @return A list of elements from the given integers created using the given map factory
     */
    public static <T> ObjectList<T> objectListFromLongs(final LongMapFactory<T> factory, final long... values)
    {
        final var objects = new ObjectList<T>();
        for (final var value : values)
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
     * An list of objects with the given upper bound
     */
    public ObjectList(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Element> append(final Element element)
    {
        super.append(element);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Element> appendAll(final Iterable<? extends Element> objects)
    {
        super.appendAll(objects);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Element> appendAll(final Iterator<? extends Element> objects)
    {
        super.appendAll(objects);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Element> appendAll(final Element[] objects)
    {
        super.appendAll(objects);
        return this;
    }

    @Override
    public StringList asStringList()
    {
        return StringList.stringList(this);
    }

    public StringList asStringList(final StringConverter<Element> converter)
    {
        return StringList.stringList(this);
    }

    @Override
    public ObjectList<Element> copy()
    {
        return (ObjectList<Element>) super.copy();
    }

    @Override
    public ObjectList<Element> first(final Count count)
    {
        return (ObjectList<Element>) super.first(count);
    }

    @Override
    public ObjectList<Element> first(final int count)
    {
        return (ObjectList<Element>) super.first(count);
    }

    @Override
    public ObjectList<Element> leftOf(final int index)
    {
        return (ObjectList<Element>) super.leftOf(index);
    }

    @Override
    public <To> ObjectList<To> mapped(final Function<Element, To> mapper)
    {
        return (ObjectList<To>) super.mapped(mapper);
    }

    @Override
    public ObjectList<Element> matching(final Matcher<Element> matcher)
    {
        return (ObjectList<Element>) super.matching(matcher);
    }

    @Override
    public ObjectList<Element> maybeReversed(final boolean reverse)
    {
        return (ObjectList<Element>) super.maybeReversed(reverse);
    }

    @Override
    public ObjectList<Element> onNewInstance()
    {
        return new ObjectList<>();
    }

    /**
     * @return This object list partitioned in to n object lists
     */
    public ObjectList<ObjectList<Element>> partition(final Count partitions)
    {
        final var lists = new ObjectList<ObjectList<Element>>(maximumSize());
        var i = 0;
        var list = -1;
        final var every = (int) Math.round((double) size() / (double) partitions.asInt());
        for (final var object : this)
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
    public ObjectList<Element> prepend(final Element element)
    {
        return (ObjectList<Element>) super.prepend(element);
    }

    @Override
    public ObjectList<Element> reversed()
    {
        return (ObjectList<Element>) super.reversed();
    }

    @Override
    public ObjectList<Element> rightOf(final int index)
    {
        return (ObjectList<Element>) super.rightOf(index);
    }

    @Override
    public ObjectList<Element> sorted()
    {
        return (ObjectList<Element>) super.sorted();
    }

    @Override
    public ObjectList<Element> sorted(final Comparator<Element> comparator)
    {
        return (ObjectList<Element>) super.sorted(comparator);
    }

    @Override
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
    public ObjectList<Element> without(final Matcher<Element> matcher)
    {
        return (ObjectList<Element>) super.without(matcher);
    }
}
