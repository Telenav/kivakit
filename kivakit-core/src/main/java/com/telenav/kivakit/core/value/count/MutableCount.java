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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramCount;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.interfaces.collection.Clearable;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.value.level.Percent.percent;

/**
 * A mutable count value useful in lambdas and inner classes. Can be {@link #increment()}ed, {@link #decrement()}ed,
 * added to with {@link #plus(long)} and {@link #plus(long)} and converted to different types with {@link #asCount()}
 * and {@link #asLong()}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCount.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class MutableCount implements
        Countable,
        Clearable,
        LongValued,
        Comparable<Countable>
{
    private long count;

    public MutableCount()
    {
        this(0);
    }

    public MutableCount(long count)
    {
        ensure(count >= 0, "Negative count " + count);

        this.count = count;
    }

    /**
     * Returns this count as a {@link Count}
     */
    public Count asCount()
    {
        return Count.count(count);
    }

    /**
     * Returns this count as an int
     */
    @Override
    public int asInt()
    {
        return (int) count;
    }

    /**
     * Returns this count as a long value
     */
    @Override
    public long asLong()
    {
        return count;
    }

    /**
     * Clears this count
     */
    @Override
    public void clear()
    {
        count = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Countable that)
    {
        return (int) (longValue() - that.count().longValue());
    }

    /**
     * Returns this mutable count as a {@link Count}
     */
    @Override
    public Count count()
    {
        return Count.count(asLong());
    }

    /**
     * Post-decrement. Returns this count, updating the count to count minus one.
     */
    public long decrement()
    {
        assert count > 0;
        return count--;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof MutableCount that)
        {
            return asLong() == that.asLong();
        }
        return false;
    }

    /**
     * Returns this count
     */
    public long get()
    {
        return count;
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(get());
    }

    /**
     * Post increment. Returns this count. Increases the count to count plus one
     */
    public long increment()
    {
        assert count + 1 >= 0;
        return count++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isZero()
    {
        return asLong() == 0L;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long longValue()
    {
        return count;
    }

    /**
     * Returns this count minus the given value
     */
    public long minus(long that)
    {
        count -= that;
        assert count >= 0;
        return count;
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
        return percent(asLong() * 100.0 / total.asLong());
    }

    /**
     * Returns this count plus the given value
     */
    public long plus(long that)
    {
        count += that;
        assert count >= 0;
        return count;
    }

    /**
     * Returns this count plus the given value
     */
    public long plus(LongValued that)
    {
        return plus(that.longValue());
    }

    /**
     * Sets this count
     */
    public void set(long count)
    {
        assert count >= 0;
        this.count = count;
    }

    @Override
    public String toString()
    {
        return asCount().toString();
    }
}
