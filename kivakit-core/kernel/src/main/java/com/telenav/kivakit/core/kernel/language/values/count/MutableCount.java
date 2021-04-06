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

package com.telenav.kivakit.core.kernel.language.values.count;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Countable;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

/**
 * A mutable count value useful in lambdas and inner classes. Can be {@link #increment()}ed, {@link #decrement()}ed,
 * added to with {@link #plus(long)} and {@link #plus(long)} and converted to different types with {@link #asCount()}
 * and {@link #asLong()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class MutableCount implements Countable, Comparable<MutableCount>, Listener
{
    private long count;

    public MutableCount()
    {
        this(0);
    }

    public MutableCount(final long count)
    {
        Ensure.ensure(count >= 0, "Negative count $", count);
        this.count = count;
    }

    public Count asCount()
    {
        return Count.count(count);
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
    public int compareTo(final MutableCount that)
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
    public boolean equals(final Object object)
    {
        if (object instanceof MutableCount)
        {
            final var that = (MutableCount) object;
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

    public boolean isGreaterThan(final MutableCount that)
    {
        return asLong() > that.asLong();
    }

    public boolean isLessThan(final MutableCount that)
    {
        return asLong() < that.asLong();
    }

    public boolean isZero()
    {
        return asLong() == 0L;
    }

    public long minus(final long that)
    {
        count -= that;
        assert count >= 0;
        return count;
    }

    @Override
    public void onMessage(final Message message)
    {
        increment();
    }

    public Percent percentOf(final Count total)
    {
        if (total.isZero())
        {
            return Percent._0;
        }
        return new Percent(asLong() * 100.0 / total.asLong());
    }

    public long plus(final long that)
    {
        count += that;
        assert count >= 0;
        return count;
    }

    public long plus(final Count that)
    {
        return plus(that.get());
    }

    public void set(final long count)
    {
        assert count >= 0;
        this.count = count;
    }

    @Override
    public String toString()
    {
        return asCount().toCommaSeparatedString();
    }
}
