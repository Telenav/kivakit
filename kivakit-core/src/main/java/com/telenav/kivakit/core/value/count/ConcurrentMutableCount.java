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

package com.telenav.kivakit.core.value.count;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.internal.lexakai.DiagramCount;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.interfaces.collection.Clearable;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A mutable count value useful in lambdas and inner classes. Can be {@link #increment()}ed, {@link #decrement()}ed,
 * added to with {@link #add(long)} and {@link #add(long)} and converted to different types with {@link #asCount()}
 * {@link #asInt()} and {@link #asLong()}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramCount.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ConcurrentMutableCount implements
        Countable,
        Clearable,
        LongValued,
        Comparable<ConcurrentMutableCount>
{
    /** The current count */
    private final AtomicLong count;

    public ConcurrentMutableCount()
    {
        this(0);
    }

    public ConcurrentMutableCount(long count)
    {
        Ensure.ensure(count >= 0, "Negative count " + count);
        this.count = new AtomicLong(count);
    }

    /**
     * Adds the given count to this one
     */
    public long add(long that)
    {
        return count.addAndGet(that);
    }

    /**
     * Adds the given count to this one
     */
    public long add(BaseCount<?> that)
    {
        return add(that.get());
    }

    /**
     * Returns this mutable count as a {@link Count}
     */
    public Count asCount()
    {
        return Count.count(asLong());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long asLong()
    {
        return count.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear()
    {
        count.set(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull ConcurrentMutableCount that)
    {
        return (int) (asLong() - that.count().asLong());
    }

    /**
     * Returns this mutable count as a {@link Count}
     */
    @Override
    public Count count()
    {
        return Count.count(asInt());
    }

    /**
     * Returns this count minus one
     */
    public long decrement()
    {
        var value = count.decrementAndGet();
        Ensure.ensure(value >= 0);
        return value;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof ConcurrentMutableCount)
        {
            var that = (ConcurrentMutableCount) object;
            return asInt() == that.asInt();
        }
        return false;
    }

    /**
     * Returns this count
     */
    public long get()
    {
        return count.get();
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(get());
    }

    /**
     * Returns this count plus one
     */
    public long increment()
    {
        return count.getAndIncrement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isZero()
    {
        return asInt() == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long longValue()
    {
        return asLong();
    }

    /**
     * Returns the percentage of the given count that this count represents
     */
    public Percent percentOf(Count total)
    {
        if (total.isZero())
        {
            return Percent._0;
        }
        return Percent.percent(asInt() * 100.0 / total.asInt());
    }

    /**
     * Adds the given value to this count
     */
    public long plus(long that)
    {
        return count.addAndGet(that);
    }

    /**
     * Adds the given value to this count
     */
    public long plus(LongValued that)
    {
        return plus(that.longValue());
    }

    /**
     * Adds the count
     */
    public void set(long count)
    {
        this.count.set(count);
    }

    @Override
    public String toString()
    {
        return asCount().asCommaSeparatedString();
    }
}
