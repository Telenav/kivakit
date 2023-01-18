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
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.function.IntMapper;
import com.telenav.kivakit.interfaces.function.LongMapper;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;
import static java.lang.Math.round;

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
 * The methods {@link #list(Object[])} and {@link #list(Maximum, Object[])} can be used to construct constant lists.
 * The factory methods {@link #listFromInts(IntMapper, int...)} and {@link #listFromLongs(LongMapper, long...)}
 * construct lists of objects from integer and long values using the given map factories to convert the values into
 * objects. The method {@link #listFromLongs(LongMapper, Iterable)} iterates through the given {@link LongValued} object
 * values, passing each quantum to the given primitive map factory and adding the resulting object to a new object list.
 * </p>
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #emptyList()}</li>
 *     <li>{@link #list(Maximum, Iterable)}</li>
 *     <li>{@link #list(Maximum, Collection)}</li>
 *     <li>{@link #list(Maximum, Iterator)}</li>
 *     <li>{@link #list(Maximum, Object...)}</li>
 *     <li>{@link #list(Iterable)}</li>
 *     <li>{@link #list(Collection)}</li>
 *     <li>{@link #list(Iterator)}</li>
 *     <li>{@link #list(Object...)}</li>
 *     <li>{@link #listFromArray(Object[])}</li>
 *     <li>{@link #listFromLongs(long[])}</li>
 *     <li>{@link #listFromLongs(LongMapper, long...)}</li>
 *     <li>{@link #listFromLongs(LongMapper, Iterable)}</li>
 *     <li>{@link #listFromInts(IntMapper, int...)}</li>
 * </ul>
 *
 * @param <Value> The object type
 * @author jonathanl (shibo)
 * @see BaseList
 * @see LongMapper
 * @see LongMapper
 * @see IntMapper
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCollections.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTED)
public class ObjectList<Value> extends BaseList<Value>
{
    /**
     * Returns an empty object list
     */
    public static <T> ObjectList<T> emptyList()
    {
        return new ObjectList<>();
    }

    /**
     * Returns a list of objects from the given iterable
     */
    public static <T> ObjectList<T> list(Maximum maximumSize, Iterable<T> values)
    {
        var list = new ObjectList<T>(maximumSize);
        list.appendAll(values);
        return list;
    }

    /**
     * Returns a list of objects from the given iterable
     */
    public static <T> ObjectList<T> list(Maximum maximumSize, Collection<T> values)
    {
        return new ObjectList<>(values);
    }

    /**
     * Returns a list of objects from the given iterator
     */
    public static <T> ObjectList<T> list(Maximum maximumSize, Iterator<T> values)
    {
        var list = new ObjectList<T>();
        list.appendAll(values);
        return list;
    }

    /**
     * Returns a list of objects from the given iterable
     */
    public static <T> ObjectList<T> list(Iterable<T> values)
    {
        var list = new ObjectList<T>();
        list.appendAll(values);
        return list;
    }

    /**
     * Returns a list of objects from the given iterable
     */
    public static <T> ObjectList<T> list(Collection<T> values)
    {
        return new ObjectList<>(values);
    }

    /**
     * Returns a list of objects from the given iterator
     */
    public static <T> ObjectList<T> list(Iterator<T> values)
    {
        var list = new ObjectList<T>();
        list.appendAll(values);
        return list;
    }

    /**
     * Returns the given list of objects with a maximum size
     */
    @SafeVarargs
    public static <T> ObjectList<T> list(Maximum maximumSize, T... objects)
    {
        var list = new ObjectList<T>(maximumSize);
        list.addAll(objects);
        return list;
    }

    /**
     * Returns the given list of objects
     */
    @SafeVarargs
    public static <T> ObjectList<T> list(T... objects)
    {
        var list = new ObjectList<T>();
        list.addAll(objects);
        return list;
    }

    public static <T> ObjectList<T> listFromArray(T[] objects)
    {
        var list = new ObjectList<T>();
        list.addAll(objects);
        return list;
    }

    /**
     * Returns a list of elements from the given integers created using the given map factory
     */
    public static <T> ObjectList<T> listFromInts(IntMapper<T> factory, int... values)
    {
        var objects = new ObjectList<T>();
        for (var value : values)
        {
            objects.add(factory.map(value));
        }
        return objects;
    }

    /**
     * Returns a list of longs containing the values in the given array
     */
    public static ObjectList<Long> listFromLongs(long[] objects)
    {
        var list = new ObjectList<Long>();
        for (var at : objects)
        {
            list.add(at);
        }
        return list;
    }

    /**
     * Returns a list of elements from the given integers created using the given map factory
     */
    public static <T> ObjectList<T> listFromLongs(LongMapper<T> factory, Iterable<LongValued> values)
    {
        var objects = new ObjectList<T>();
        for (var value : values)
        {
            objects.add(factory.map(value.longValue()));
        }
        return objects;
    }

    /**
     * Returns a list of elements from the given integers created using the given map factory
     */
    public static <T> ObjectList<T> listFromLongs(LongMapper<T> factory, long... values)
    {
        var objects = new ObjectList<T>();
        for (var value : values)
        {
            objects.add(factory.map(value));
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
    public ObjectList(Maximum maximumSize, Collection<Value> collection)
    {
        super(maximumSize, collection);
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
    public ObjectList<Value> appending(Iterable<? extends Value> values)
    {
        return (ObjectList<Value>) super.appending(values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> appending(Value value)
    {
        return (ObjectList<Value>) super.appending(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> appendingIfNotNull(Value value)
    {
        return (ObjectList<Value>) super.appendingIfNotNull(value);
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
    public <To> ObjectList<To> map(Function<Value, To> mapper)
    {
        return (ObjectList<To>) super.map(mapper);
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
        return add(value);
    }

    /**
     * Returns this object list partitioned in to n object lists
     */
    public ObjectList<ObjectList<Value>> partition(Count partitions)
    {
        var lists = new ObjectList<ObjectList<Value>>(maximumSize());
        var i = 0;
        var list = -1;
        var every = (int) round((double) size() / (double) partitions.asInt());
        for (var object : this)
        {
            if (i++ % every == 0 && list < partitions.asInt() - 1)
            {
                lists.add(new ObjectList<>());
                list++;
            }
            lists.get(list).add(object);
        }
        return lists;
    }

    @Override
    public ObjectList<Value> prepending(Value value)
    {
        return (ObjectList<Value>) super.prepending(value);
    }

    @Override
    public ObjectList<Value> prepending(Iterable<? extends Value> values)
    {
        return (ObjectList<Value>) super.prepending(values);
    }

    @Override
    public ObjectList<Value> prependingIfNotNull(Value value)
    {
        return (ObjectList<Value>) super.prependingIfNotNull(value);
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
    public ObjectList<Value> uniqued()
    {
        return (ObjectList<Value>) super.uniqued();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> with(Iterable<Value> value)
    {
        return (ObjectList<Value>) super.with(value);
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
    public ObjectList<Value> with(Collection<Value> value)
    {
        return (ObjectList<Value>) super.with(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> with(Value[] value)
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

    @Override
    public ObjectList<Value> without(Value value)
    {
        return (ObjectList<Value>) super.without(value);
    }

    @Override
    public ObjectList<Value> without(Collection<Value> that)
    {
        return (ObjectList<Value>) super.without(that);
    }

    @Override
    public ObjectList<Value> without(Value[] that)
    {
        return (ObjectList<Value>) super.without(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BaseList<Value> onNewList()
    {
        return list();
    }
}
