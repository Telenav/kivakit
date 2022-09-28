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

package com.telenav.kivakit.core.value.mutable;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.atomic.AtomicLong;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.value.count.Count.count;

/**
 * A thread-safe long value that can be changed. Useful for mutating something other than a field from within a lambda
 * expression.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramValue.class)
@ApiQuality(stability = STABLE_EXPANDABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class MutableLong
{
    /** The index */
    private final AtomicLong value;

    /**
     * Creates an index with the value zero
     */
    public MutableLong()
    {
        this(0);
    }

    /**
     * Creates an index with the given initial value
     */
    public MutableLong(int value)
    {
        this.value = new AtomicLong(value);
    }

    /**
     * This long value as an int
     */
    public int asInt()
    {
        return (int) get();
    }

    /**
     * Decrements this index, returning the new value
     */
    public long decrement()
    {
        return value.decrementAndGet();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object object)
    {
        return value.equals(object);
    }

    /**
     * Gets the value of this index
     */
    public long get()
    {
        return value.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return value.hashCode();
    }

    /**
     * Increments this index, returning the new value
     */
    public long increment()
    {
        return value.incrementAndGet();
    }

    /**
     * Adds the given value to this index, returning the new value
     */
    @SuppressWarnings("UnusedReturnValue")
    public long minus(int that)
    {
        return plus(-that);
    }

    /**
     * Adds the given value to this index, returning the new value
     */
    @SuppressWarnings("UnusedReturnValue")
    public long plus(int that)
    {
        return value.addAndGet(that);
    }

    /**
     * Sets this index
     */
    public void set(int index)
    {
        this.value.set(index);
    }

    @Override
    public String toString()
    {
        return count(get()).asCommaSeparatedString();
    }
}
