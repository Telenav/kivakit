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

package com.telenav.kivakit.core.collections.set;

import com.telenav.kivakit.core.collections.iteration.BaseIterator;
import com.telenav.kivakit.core.collections.iteration.FilteredIterable;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.string.StringTo;
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
import com.telenav.kivakit.interfaces.factory.Factory;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.telenav.kivakit.core.value.count.Maximum.maximum;

/**
 * A set with a maximum size. Adds useful methods to the usual {@link Set} operations, as well as implementing:
 *
 * <ul>
 *     <li>{@link Set}</li>
 *     <li>{@link Sequence}</li>
 *     <li>{@link Addable}</li>
 *     <li>{@link Copyable}</li>
 *     <li>{@link Countable}</li>
 *     <li>{@link Joinable}</li>
 *     <li>{@link StringFormattable}</li>
 * </ul>
 *
 *  <p><b>Adding</b></p>
 *
 * <ul>
 *     <li>{@link #add(Object)}</li>
 *     <li>{@link #addIfNotNull(Object)}</li>
 *     <li>{@link #addAll(Collection)}</li>
 *     <li>{@link #addAll(Object[])}</li>
 *     <li>{@link #addAll(Iterable)}</li>
 *     <li>{@link #addAll(Iterator)}</li>
 *     <li>{@link #addAllMatching(Object[], Matcher)}</li>
 *     <li>{@link #addAllMatching(Iterable, Matcher)}</li>
 *     <li>{@link #addAllMatching(Iterable, Matcher)}</li>
 *     <li>{@link #addAllMatching(Collection, Matcher)}</li>
 * </ul>
 *
 * <p><b>Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #asSet()}</li>
 *     <li>{@link #asIterable(Matcher)}</li>
 *     <li>{@link #asIterator(Matcher)}</li>
 *     <li>{@link #asString(Format)}</li>
 * </ul>
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link #first()}</li>
 *     <li>{@link #matching(Matcher)}</li>
 *     <li>{@link #mapped(Function)}</li>
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
 *     <li>{@link #remove(Object)}</li>
 *     <li>{@link #removeAll(Collection)}</li>
 *     <li>{@link #removeIf(Predicate)}</li>
 *     <li>{@link #removeAllMatching(Matcher)}</li>
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
 *     <li>{@link #join()} - This list joined by the list {@link #separator()}</li>
 *     <li>{@link #separator()} - The separator used when joining this list into a string</li>
 * </ul>
 *
 * <p><b>Functional Methods</b></p>
 *
 * <ul>
 *     <li>{@link #copy()} - A copy of this list</li>
 *     <li>{@link #mapped(Function)}</li>
 *     <li>{@link #matching(Matcher)} - A copy of this list filtered to matching elements</li>
 *     <li>{@link #with(Object)}</li>
 *     <li>{@link #without(Matcher)} - This list without the matching elements</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Addable
 * @see Appendable
 * @see Countable
 * @see Factory
 * @see Indexable
 * @see Joinable
 * @see List
 * @see Prependable
 * @see RandomAccess
 * @see Sectionable
 * @see StringFormattable
 * @see WriteIndexable
 */
@SuppressWarnings("unused") @LexakaiJavadoc(complete = true)
public abstract class BaseSet<Value> implements
        Set<Value>,
        Sequence<Value>,
        Addable<Value>,
        Factory<BaseSet<Value>>,
        Copyable<Value, BaseSet<Value>>,
        Countable,
        Joinable<Value>,
        StringFormattable
{
    /** True if this set ran out of room, and we've already warned about it */
    private boolean warnedAboutOutOfRoom;

    /** The backing set */
    private final Set<Value> set;

    /** The maximum number of values that can be stored in this set */
    protected int maximumSize;

    public BaseSet(Maximum maximumSize)
    {
        this(maximumSize, new HashSet<>());
    }

    /**
     * @param maximumSize The maximum size of this set
     * @param values The initial values for this set
     */
    public BaseSet(Maximum maximumSize, Collection<Value> values)
    {
        // If there is room for the initial values,
        if (hasRoomFor(values.size()))
        {
            // save the maximum size
            this.maximumSize = maximumSize.asInt();

            // and the set.
            if (values instanceof Set)
            {
                this.set = (Set<Value>) values;
            }
            else
            {
                this.set = new HashSet<>(values);
            }
        }
        else
        {
            // otherwise, signal that we're out of
            onOutOfRoom(values.size());

            // and leave the set empty.
            this.set = new HashSet<>();
        }
    }

    public BaseSet(Set<Value> set)
    {
        this(Maximum.MAXIMUM, set);
    }

    protected BaseSet()
    {
        set = new HashSet<>();
    }

    @Override
    public boolean add(Value value)
    {
        return onAdd(value);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Value> values)
    {
        return Addable.super.addAll(values);
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

    @Override
    public @NotNull Iterator<Value> asIterator(Matcher<Value> matcher)
    {
        return new BaseIterator<>()
        {
            private final Iterator<Value> iterator = set.iterator();

            @Override
            protected Value onNext()
            {
                if (iterator.hasNext())
                {
                    return iterator.next();
                }
                return null;
            }
        };
    }

    @Override
    public @NotNull Iterator<Value> asIterator()
    {
        return Copyable.super.asIterator();
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
                return join(", ", StringTo::debug);

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

    @Override
    public void clear()
    {
        set.clear();
    }

    @Override
    public boolean contains(Object object)
    {
        return set.contains(object);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean containsAll(Collection<?> collection)
    {
        return set.containsAll(collection);
    }

    @Override
    public BaseSet<Value> copy()
    {
        var set = newInstance();
        set.addAll(this);
        return set;
    }

    @Override
    public Count count()
    {
        return Count.count(size());
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Set)
        {
            var that = (Set<?>) object;
            return set.equals(that);
        }
        return false;
    }

    @Override
    public Value first()
    {
        return iterator().next();
    }

    @Override
    public int hashCode()
    {
        return set.hashCode();
    }

    @Override
    public boolean isEmpty()
    {
        return set.isEmpty();
    }

    @Override
    public Iterator<Value> iterator()
    {
        return set.iterator();
    }

    /**
     * @return This bounded list with all elements mapped by the given mapper to the mapper's target type
     */
    @SuppressWarnings("unchecked")
    public <Target> BaseSet<Target> mapped(Function<Value, Target> mapper)
    {
        var filtered = (BaseSet<Target>) newInstance();
        for (var element : this)
        {
            filtered.add(mapper.apply(element));
        }
        return filtered;
    }

    @Override
    public BaseSet<Value> matching(Matcher<Value> matcher)
    {
        var matches = newInstance();
        matches.addAllMatching(this, matcher);
        return matches;
    }

    public Iterable<Value> matchingAsIterable(Matcher<Value> matcher)
    {
        return new FilteredIterable<>(set, matcher);
    }

    public Maximum maximumSize()
    {
        return maximum(maximumSize);
    }

    @Override
    public boolean onAdd(Value value)
    {
        return set.add(value);
    }

    @Override
    public abstract BaseSet<Value> onNewInstance();

    @Override
    public void onOutOfRoom(int values)
    {
        if (!warnedAboutOutOfRoom)
        {
            warnedAboutOutOfRoom = true;
            Ensure.warning(new Throwable(), "Adding $ values, would exceed maximum size of $. Ignoring operation.", values, totalRoom());
        }
    }

    @Override
    public boolean remove(Object object)
    {
        return set.remove(object);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean removeAll(Collection<?> collection)
    {
        return set.removeAll(collection);
    }

    /**
     * Removes all values matching the given matcher
     */
    public boolean removeAllMatching(Matcher<Value> matcher)
    {
        return removeIf(matcher);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean retainAll(Collection<?> collection)
    {
        return set.retainAll(collection);
    }

    @Override
    public int size()
    {
        return set.size();
    }

    @Override
    public Object[] toArray()
    {
        return set.toArray();
    }

    @SuppressWarnings({ "SuspiciousToArrayCall" })
    @Override
    public <E> E[] toArray(E @NotNull [] array)
    {
        return set.toArray(array);
    }

    @Override
    public String toString()
    {
        return set.toString();
    }

    /**
     * Returns this list with the given value
     */
    public BaseSet<Value> with(Value value)
    {
        var copy = copy();
        copy.add(value);
        return copy;
    }

    /**
     * Returns this list with the given values
     */
    public BaseSet<Value> with(Collection<Value> that)
    {
        var set = copy();
        set.addAll(that);
        return set;
    }
}
