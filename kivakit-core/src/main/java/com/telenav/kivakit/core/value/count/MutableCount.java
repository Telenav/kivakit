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

import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.core.lexakai.DiagramCount;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * A mutable count value useful in lambdas and inner classes. Can be {@link #increment()}ed, {@link #decrement()}ed,
 * added to with {@link #plus(long)} and {@link #plus(long)} and converted to different types with {@link #asCount()}
 * and {@link #asLong()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCount.class)
public class MutableCount implements Countable, Comparable<MutableCount>
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

    public Count asCount()
    {
        return Count.count(count);
    }

    public int asInt()
    {
        return (int) count;
    }

    public long asLong()
    {
        return count;
    }

    public void clear()
    {
        count = 0;
    }

    @Override
    public int compareTo(MutableCount that)
    {
        return (int) (asLong() - that.asLong());
    }

    @Override
    public Count count()
    {
        return Count.count(asLong());
    }

    public long decrement()
    {
        assert count > 0;
        return count--;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof MutableCount)
        {
            var that = (MutableCount) object;
            return asLong() == that.asLong();
        }
        return false;
    }

    public long get()
    {
        return count;
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(get());
    }

    public long increment()
    {
        assert count + 1 >= 0;
        return count++;
    }

    public boolean isGreaterThan(MutableCount that)
    {
        return asLong() > that.asLong();
    }

    public boolean isLessThan(MutableCount that)
    {
        return asLong() < that.asLong();
    }

    public boolean isZero()
    {
        return asLong() == 0L;
    }

    public long minus(long that)
    {
        count -= that;
        assert count >= 0;
        return count;
    }

    public Percent percentOf(Count total)
    {
        if (total.isZero())
        {
            return Percent._0;
        }
        return Percent.of(asLong() * 100.0 / total.asLong());
    }

    public long plus(long that)
    {
        count += that;
        assert count >= 0;
        return count;
    }

    public long plus(Count that)
    {
        return plus(that.get());
    }

    public void set(long count)
    {
        assert count >= 0;
        this.count = count;
    }

    @Override
    public int size()
    {
        return asInt();
    }

    @Override
    public String toString()
    {
        return asCount().asCommaSeparatedString();
    }
}
