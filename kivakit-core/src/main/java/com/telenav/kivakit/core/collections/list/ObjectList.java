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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.factory.IntMapFactory;
import com.telenav.kivakit.interfaces.factory.LongMapFactory;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;

/**
 * A bounded list of objects with overrides of methods from {@link BaseList} to downcast return values to
 * {@link ObjectList} for convenience. New instances of {@link ObjectList} are created by {@link BaseList} by calling
 * {@link #onNewInstance()}, allowing functional logic to reside in the base class. For details on the methods inherited
 * from {@link BaseList}, see that class.
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
 * @param <Value> The object type
 * @author jonathanl (shibo)
 * @see BaseList
 * @see LongMapFactory
 * @see LongMapFactory
 * @see IntMapFactory
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCollections.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTATION_COMPLETE)
public class ObjectList<Value> extends BaseList<Value>
{
    /**
     * @return An empty object list
     */
    public static <T> ObjectList<T> emptyObjectList()
    {
        return new ObjectList<>(Maximum._0);
    }

    /**
     * @return A list of objects from the given iterable
     */
    public static <T> ObjectList<T> objectList(Iterable<T> values)
    {
        var list = new ObjectList<T>();
        list.appendAll(values);
        return list;
    }

    /**
     * @return A list of objects from the given iterable
     */
    public static <T> ObjectList<T> objectList(Collection<T> values)
    {
        return new ObjectList<T>(values);
    }

    /**
     * @return A list of objects from the given iterator
     */
    public static <T> ObjectList<T> objectList(Iterator<T> values)
    {
        var list = new ObjectList<T>();
        list.appendAll(values);
        return list;
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
        this(MAXIMUM);
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
    public ObjectList(Collection<Value> collection)
    {
        super(collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> appendAllThen(Iterable<? extends Value> values)
    {
        return (ObjectList<Value>) super.appendAllThen(values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> appendThen(Value value)
    {
        return (ObjectList<Value>) super.appendThen(value);
    }

    /**
     * Returns a copy of this list
     *
     * @return The copy
     */
    @Override
    public ObjectList<Value> copy()
    {
        return (ObjectList<Value>) super.copy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> first(Count count)
    {
        return (ObjectList<Value>) super.first(count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> first(int count)
    {
        return (ObjectList<Value>) super.first(count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> last(int count)
    {
        return (ObjectList<Value>) super.last(count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> leftOf(int index)
    {
        return (ObjectList<Value>) super.leftOf(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <To> ObjectList<To> mapped(Function<Value, To> mapper)
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
    public ObjectList<Value> matching(Matcher<Value> matcher)
    {
        return (ObjectList<Value>) super.matching(matcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> maybeReversed(boolean reverse)
    {
        return (ObjectList<Value>) super.maybeReversed(reverse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onAppend(Value value)
    {
        return super.add(value);
    }

    /**
     * @return This object list partitioned in to n object lists
     */
    public ObjectList<ObjectList<Value>> partition(Count partitions)
    {
        var lists = new ObjectList<ObjectList<Value>>(maximumSize());
        var i = 0;
        var list = -1;
        var every = (int) Math.round((double) size() / (double) partitions.asInt());
        for (var object : this)
        {
            if (i++ % every == 0 && list < partitions.asInt() - 1)
            {
                lists.add(new ObjectList<>(MAXIMUM));
                list++;
            }
            lists.get(list).add(object);
        }
        return lists;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> reversed()
    {
        return (ObjectList<Value>) super.reversed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> rightOf(int index)
    {
        return (ObjectList<Value>) super.rightOf(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> sorted()
    {
        return (ObjectList<Value>) super.sorted();
    }

    /**
     * Creates a list containing the unique objects in this list
     *
     * @return The unique objects
     */
    @Override
    @SuppressWarnings("SpellCheckingInspection")
    public ObjectList<Value> uniqued()
    {
        return (ObjectList<Value>) super.uniqued();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> with(Value value)
    {
        return (ObjectList<Value>) super.with(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> without(Matcher<Value> matcher)
    {
        return (ObjectList<Value>) super.without(matcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BaseList<Value> onNewList()
    {
        return objectList();
    }
}
