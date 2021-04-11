////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.collections.set;

import com.telenav.kivakit.core.kernel.interfaces.collection.Addable;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Sized;
import com.telenav.kivakit.core.kernel.interfaces.value.NewInstance;
import com.telenav.kivakit.core.kernel.language.iteration.Matching;
import com.telenav.kivakit.core.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A set with a maximum size. Adds the methods {@link #matching(Matcher)} and {@link #first()} to the usual {@link Set}
 * operations.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public abstract class BaseSet<Element> implements
        Set<Element>,
        NewInstance<BaseSet<Element>>,
        Sized,
        Addable<Element>,
        AsString
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private final Set<Element> set;

    private boolean outOfRoom;

    protected Maximum maximumSize;

    public BaseSet(final Maximum maximumSize)
    {
        this(maximumSize, new HashSet<>());
    }

    public BaseSet(final Maximum maximumSize, final Set<Element> set)
    {
        this.set = set;
        checkSize(0);
        this.maximumSize = maximumSize;
    }

    public BaseSet(final Set<Element> set)
    {
        this(Maximum.MAXIMUM, set);
    }

    protected BaseSet()
    {
        set = new HashSet<>();
    }

    @Override
    public boolean add(final Element element)
    {
        if (checkSize(1))
        {
            return set.add(element);
        }
        return false;
    }

    @Override
    public boolean addAll(final Collection<? extends Element> objects)
    {
        var success = true;
        for (final Element object : objects)
        {
            if (!add(object))
            {
                success = false;
            }
        }
        return success;
    }

    @Override
    public boolean addAll(final Iterable<? extends Element> objects)
    {
        var success = true;
        for (final Element object : objects)
        {
            if (!add(object))
            {
                success = false;
            }
        }
        return success;
    }

    @Override
    public void clear()
    {
    }

    @Override
    public boolean contains(final Object object)
    {
        return set.contains(object);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean containsAll(final Collection<?> collection)
    {
        return set.containsAll(collection);
    }

    @Override
    public Count count()
    {
        return Count.count(size());
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Set)
        {
            final var that = (Set<?>) object;
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

    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<Element> iterator()
    {
        return set.iterator();
    }

    public Iterable<Element> matching(final Matcher<Element> matcher)
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

    public Count maximumSize()
    {
        return maximumSize;
    }

    @Override
    public boolean remove(final Object object)
    {
        return set.remove(object);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean removeAll(final Collection<?> collection)
    {
        return set.removeAll(collection);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean retainAll(final Collection<?> collection)
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
    public <E> E[] toArray(final E[] array)
    {
        return set.toArray(array);
    }

    @Override
    public String toString()
    {
        return set.toString();
    }

    protected boolean checkSize(final int increase)
    {
        final var maximumSize = maximumSize();
        if (maximumSize != null && size() + increase > maximumSize.asInt())
        {
            if (!outOfRoom)
            {
                LOGGER.warning(new Throwable(),
                        "Maximum size of ${debug} elements would have been exceeded. Ignoring operation (this is not an exception, just a warning)",
                        maximumSize);
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
