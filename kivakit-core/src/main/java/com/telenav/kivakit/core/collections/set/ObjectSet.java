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
import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.collections.list.ObjectList.objectList;
import static com.telenav.kivakit.core.value.count.Maximum.MAXIMUM;

/**
 * A set of objects with an arbitrary backing set. See {@link BaseSet} for details on available methods.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #objectSet(Object[])}</li>
 *     <li>{@link #objectSet(Collection)}</li>
 *     <li>{@link #emptyObjectSet()}</li>
 *     <li>{@link ObjectSet#ObjectSet()}</li>
 *     <li>{@link ObjectSet#ObjectSet(Maximum)}</li>
 *     <li>{@link ObjectSet#ObjectSet(Maximum, Collection)}</li>
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
    public static <T> ObjectSet<T> emptyObjectSet()
    {
        return new ObjectSet<>();
    }

    /**
     * Returns an {@link ObjectSet} with the given values in it
     *
     * @param values The values to add to the set
     */
    @SafeVarargs
    public static <T> ObjectSet<T> objectSet(T... values)
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
    public static <T> ObjectSet<T> objectSet(Collection<T> values)
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
        return objectList(super.asList());
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
        return objectSet();
    }
}
