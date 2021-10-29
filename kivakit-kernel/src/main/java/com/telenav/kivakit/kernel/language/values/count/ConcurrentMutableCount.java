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

package com.telenav.kivakit.kernel.language.values.count;

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.interfaces.numeric.Countable;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A mutable count value useful in lambdas and inner classes. Can be {@link #increment()}ed, {@link #decrement()}ed,
 * added to with {@link #add(long)} and {@link #add(long)} and converted to different types with {@link #asCount()}
 * {@link #asInt()} and {@link #asLong()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class ConcurrentMutableCount implements Countable, Listener
{
    private final AtomicLong count;

    public ConcurrentMutableCount()
    {
        this(0);
    }

    public ConcurrentMutableCount(long count)
    {
        Ensure.ensure(count >= 0, "Negative count $", count);
        this.count = new AtomicLong(count);
    }

    public long add(long that)
    {
        return count.addAndGet(that);
    }

    public long add(Count that)
    {
        return add(that.get());
    }

    public Count asCount()
    {
        return Count.count(asLong());
    }

    public int asInt()
    {
        return (int) asLong();
    }

    public long asLong()
    {
        return count.get();
    }

    public void clear()
    {
        count.set(0);
    }

    @Override
    public Count count()
    {
        return Count.count(asInt());
    }

    public long decrement()
    {
        var value = count.getAndDecrement();
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

    public long get()
    {
        return count.get();
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(get());
    }

    public long increment()
    {
        return count.getAndIncrement();
    }

    public boolean isGreaterThan(ConcurrentMutableCount that)
    {
        return asInt() > that.asInt();
    }

    public boolean isLessThan(ConcurrentMutableCount that)
    {
        return asInt() < that.asInt();
    }

    public boolean isZero()
    {
        return asInt() == 0;
    }

    @Override
    public void onMessage(Message message)
    {
        increment();
    }

    public Percent percentOf(Count total)
    {
        if (total.isZero())
        {
            return Percent._0;
        }
        return Percent.of(asInt() * 100.0 / total.asInt());
    }

    public void set(long count)
    {
        this.count.set(count);
    }

    @Override
    public String toString()
    {
        return asCount().toCommaSeparatedString();
    }
}
