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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.BaseCollection;
import com.telenav.kivakit.core.value.count.Countable;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.collection.Addable;
import com.telenav.kivakit.interfaces.collection.Copyable;
import com.telenav.kivakit.interfaces.collection.Joinable;
import com.telenav.kivakit.interfaces.collection.Sequence;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.factory.Factory;
import com.telenav.kivakit.interfaces.string.StringFormattable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;
import static java.util.Collections.emptySet;

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
 * <p><b>Adding</b></p>
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
 *     <li>{@link #asIterable(Matcher)}</li>
 *     <li>{@link #asIterator(Matcher)}</li>
 *     <li>{@link #asList()}</li>
 *     <li>{@link #asSet()}</li>
 *     <li>{@link #asString(Format)}</li>
 * </ul>
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link #first()}</li>
 *     <li>{@link #matching(Matcher)}</li>
 *     <li>{@link #map(Function)}</li>
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
 *     <li>{@link #map(Function)}</li>
 *     <li>{@link #matching(Matcher)} - A copy of this list filtered to matching elements</li>
 *     <li>{@link #with(Object)}</li>
 *     <li>{@link #with(Collection)}</li>
 *     <li>{@link #without(Matcher)} - This list without the matching elements</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see BaseCollection
 * @see Copyable
 * @see Factory
 * @see Set
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTED)
public abstract class BaseSet<Value> extends BaseCollection<Value> implements
        Copyable<Value, BaseSet<Value>>,
        Set<Value>
{
    /** The backing set */
    private final Set<Value> backingSet;

    /**
     * Construct a set with a maximum number of elements
     *
     * @param maximumSize The maximum size
     */
    protected BaseSet(Maximum maximumSize)
    {
        this(maximumSize, emptySet());
    }

    /**
     * @param maximumSize The maximum size of this set
     * @param values The initial values for this set
     */
    protected BaseSet(Maximum maximumSize, Collection<Value> values)
    {
        super(maximumSize);

        // If there is room for the initial values,
        if (values.size() < maximumSize.asInt())
        {
            // save the set.
            this.backingSet = newBackingSet();
            this.backingSet.addAll(values);
        }
        else
        {
            // otherwise, signal that we're out of
            onOutOfRoom(values.size());

            // and leave the set empty.
            this.backingSet = newBackingSet();
        }
    }

    protected BaseSet()
    {
        this(MAXIMUM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseSet<Value> copy()
    {
        return Copyable.super.copy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <Output> BaseSet<Output> map(Function<Value, Output> mapper)
    {
        return (BaseSet<Output>) super.map(mapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseSet<Value> matching(Matcher<Value> matcher)
    {
        return Copyable.super.matching(matcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final BaseSet<Value> onNewInstance()
    {
        return (BaseSet<Value>) newCollection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseSet<Value> with(Value value)
    {
        return (BaseSet<Value>) super.with(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final Set<Value> backingCollection()
    {
        return backingSet;
    }

    /**
     * Returns a new backing set to store values in
     */
    protected final Set<Value> newBackingSet()
    {
        return onNewBackingSet();
    }

    /**
     * Returns a new backing set to store values in
     */
    protected abstract Set<Value> onNewBackingSet();
}
