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
import com.telenav.kivakit.core.collections.iteration.Matching;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.string.StringTo;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Countable;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.collection.Addable;
import com.telenav.kivakit.interfaces.collection.Copyable;
import com.telenav.kivakit.interfaces.collection.Joinable;
import com.telenav.kivakit.interfaces.collection.Sequence;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.factory.Factory;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.telenav.kivakit.core.value.count.Maximum.maximum;

/**
 * A set with a maximum size. Adds the methods {@link #matchingAsIterable(Matcher)} and {@link #first()} to the usual
 * {@link Set} operations.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
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

    @Override
    public BaseSet<Value> matching(Matcher<Value> matcher)
    {
        var matches = newInstance();
        matches.addAllMatching(this, matcher);
        return matches;
    }

    public Iterable<Value> matchingAsIterable(Matcher<Value> matcher)
    {
        return new Matching<>(matcher)
        {
            @Override
            protected Iterator<Value> values()
            {
                return set.iterator();
            }
        };
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

    public BaseSet<Value> with(Collection<Value> that)
    {
        var set = copy();
        set.addAll(that);
        return set;
    }
}
