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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.BaseCollection;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.comparison.Matcher;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;

/**
 * A set of objects with an arbitrary backing set.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #set(Object[])}</li>
 *     <li>{@link #set(Collection)}</li>
 *     <li>{@link #emptySet()}</li>
 *     <li>{@link ObjectSet#ObjectSet()}</li>
 *     <li>{@link ObjectSet#ObjectSet(Maximum)}</li>
 *     <li>{@link ObjectSet#ObjectSet(Maximum, Collection)}</li>
 * </ul>
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
 *     <li>{@link #with(Collection)}</li>
 *     <li>{@link #without(Matcher)} - This list without the matching elements</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see BaseSet
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTATION_COMPLETE)
public class ObjectSet<Value> extends BaseSet<Value>
{
    /**
     * Returns an empty {@link ObjectSet}.
     */
    public static <T> ObjectSet<T> emptySet()
    {
        return new ObjectSet<>();
    }

    /**
     * Returns an {@link ObjectSet} with the given values in it
     *
     * @param values The values to add to the set
     */
    @SafeVarargs
    public static <T> ObjectSet<T> set(T... values)
    {
        var set = new ObjectSet<T>();
        set.addAll(values);
        return set;
    }

    /**
     * Returns an {@link ObjectSet} with the given values in it
     *
     * @param values The values to add to the set
     */
    public static <T> ObjectSet<T> set(Collection<T> values)
    {
        var set = new ObjectSet<T>();
        set.addAll(values);
        return set;
    }

    /**
     * Creates an object set
     *
     * @param maximumSize The maximum size of the set
     */
    public ObjectSet(Maximum maximumSize)
    {
        super(maximumSize);
    }

    /**
     * Creates an object set
     *
     * @param maximumSize The maximum size of the set
     * @param values The initial values to add to the set
     */
    public ObjectSet(Maximum maximumSize, Collection<Value> values)
    {
        super(maximumSize, values);
    }

    /**
     * Creates an object set
     *
     * @param values The initial values to add to the set
     */
    public ObjectSet(Collection<Value> values)
    {
        super(MAXIMUM, values);
    }

    /**
     * Creates an empty object set with no maximum size
     */
    public ObjectSet()
    {
        this(MAXIMUM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Value> asList()
    {
        return list(super.asList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectSet<Value> copy()
    {
        return (ObjectSet<Value>) super.copy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectSet<Value> matching(Matcher<Value> matcher)
    {
        return (ObjectSet<Value>) super.matching(matcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectSet<Value> with(Value value)
    {
        return (ObjectSet<Value>) super.with(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectSet<Value> with(Collection<Value> that)
    {
        return (ObjectSet<Value>) super.with(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Set<Value> onNewBackingSet()
    {
        return new HashSet<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BaseCollection<Value> onNewCollection()
    {
        return set();
    }
}
