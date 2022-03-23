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

import com.telenav.kivakit.core.collections.iteration.Matching;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Countable;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.collection.Addable;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.kivakit.interfaces.value.Instantiable;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A set with a maximum size. Adds the methods {@link #matchingAsIterable(Matcher)} and {@link #first()} to the usual
 * {@link Set} operations.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public abstract class BaseSet<Element> implements
        Set<Element>,
        Instantiable<BaseSet<Element>>,
        Countable,
        Addable<Element>,
        Stringable
{
    private boolean outOfRoom;

    private final Set<Element> set;

    protected Maximum maximumSize;

    public BaseSet(Maximum maximumSize)
    {
        this(maximumSize, new HashSet<>());
    }

    public BaseSet(Maximum maximumSize, Set<Element> set)
    {
        this.set = set;
        checkSize(0);
        this.maximumSize = maximumSize;
    }

    public BaseSet(Set<Element> set)
    {
        this(Maximum.MAXIMUM, set);
    }

    protected BaseSet()
    {
        set = new HashSet<>();
    }

    @Override
    public boolean add(Element element)
    {
        if (checkSize(1))
        {
            return set.add(element);
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Element> objects)
    {
        var success = true;
        for (Element object : objects)
        {
            if (!add(object))
            {
                success = false;
            }
        }
        return success;
    }

    @Override
    public boolean addAll(Iterable<? extends Element> objects)
    {
        var success = true;
        for (Element object : objects)
        {
            if (!add(object))
            {
                success = false;
            }
        }
        return success;
    }

    public void addAll(Element[] objects)
    {
        for (var object : objects)
        {
            add(object);
        }
    }

    public void addAllMatching(Collection<Element> values, Matcher<Element> matcher)
    {
        values.forEach(at ->
        {
            if (matcher.matches(at))
            {
                add(at);
            }
        });
    }

    @Override
    public void clear()
    {
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

    public BaseSet<Element> copy()
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

    public Element first()
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
    public Iterator<Element> iterator()
    {
        return set.iterator();
    }

    public BaseSet<Element> matching(Matcher<Element> matcher)
    {
        var matches = newInstance();
        matches.addAllMatching(this, matcher);
        return matches;
    }

    public Iterable<Element> matchingAsIterable(Matcher<Element> matcher)
    {
        return new Matching<>(matcher)
        {
            @Override
            protected Iterator<Element> values()
            {
                return set.iterator();
            }
        };
    }

    public Maximum maximumSize()
    {
        return maximumSize;
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

    public BaseSet<Element> with(Collection<Element> that)
    {
        var set = copy();
        set.addAll(that);
        return set;
    }

    protected boolean checkSize(int increase)
    {
        var maximumSize = maximumSize();
        if (maximumSize != null && size() + increase > maximumSize.asInt())
        {
            if (!outOfRoom)
            {
                Ensure.warning(new Throwable(), "Maximum size of " + maximumSize + " elements would have been exceeded. Ignoring operation (this is not an exception, just a warning)");
                outOfRoom = true;
            }
            return false;
        }
        else
        {
            outOfRoom = false;
        }
        return true;
    }
}
