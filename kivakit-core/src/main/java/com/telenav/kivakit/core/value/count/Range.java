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

import com.telenav.kivakit.core.lexakai.DiagramCount;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.test.RandomValueFactory;
import com.telenav.kivakit.interfaces.code.LoopBody;
import com.telenav.kivakit.interfaces.code.RetryableLoopBody;
import com.telenav.kivakit.interfaces.collection.NextValue;
import com.telenav.kivakit.interfaces.numeric.IntegerNumeric;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.function.Consumer;

/**
 * Represents a range of Values from a minimum to a maximum.
 *
 * @param <Value> A value that must be {@link Minimizable}, {@link Maximizable} and implement {@link NextValue}.
 * @author jonathanl (shibo)
 * @see LoopBody
 */
@UmlClassDiagram(diagram = DiagramCount.class)
public class Range<Value extends IntegerNumeric<Value>>

{
    public static <Value extends IntegerNumeric<Value>> Range<Value> exclusive(Value minimum, Value maximum)
    {
        return new Range<>(minimum, maximum.next(), false);
    }

    public static <Value extends IntegerNumeric<Value>> Range<Value> inclusive(Value minimum, Value maximum)
    {
        return new Range<>(minimum, maximum, true);
    }

    private final Value minimum;

    /** This private representation is inclusive */
    private final Value maximum;

    /** True if this range was constructed with {@link Range#inclusive(IntegerNumeric, IntegerNumeric)} */
    private final boolean inclusive;

    protected Range(Value minimum, Value maximum, boolean inclusive)
    {
        this.minimum = minimum;
        this.maximum = maximum;
        this.inclusive = inclusive;
    }

    public Value constrainTo(Value value)
    {
        return maximum.minimum(minimum.maximum(value));
    }

    public boolean contains(Value value)
    {
        return false;
    }

    public Value exclusiveMaximum()
    {
        return maximum.minus(maximum.newInstance(1L));
    }

    public void forEach(Consumer<Value> consumer)
    {
        for (Value at = minimum; !at.equals(maximum); at = at.next())
        {
            consumer.accept(at);
        }
    }

    public Value inclusiveMaximum()
    {
        return maximum;
    }

    public boolean isExclusive()
    {
        return !isInclusive();
    }

    public boolean isInclusive()
    {
        return inclusive;
    }

    public void loop(LoopBody<Value> body)
    {
        body.forEach(minimum(), maximum());
    }

    public void retryLoop(RetryableLoopBody<Value> body)
    {
        body.forEach(minimum(), maximum());
    }

    public void loop(Runnable body)
    {
        forEach(ignored -> body.run());
    }

    public Value maximum()
    {
        return maximum;
    }

    public Value minimum()
    {
        return minimum;
    }

    public Value randomValue()
    {
        var width = maximum.minus(minimum);
        var random = new RandomValueFactory().randomLongExclusive(
                minimum().asLong(),
                exclusiveMaximum().asLong());

        return minimum.newInstance(random);
    }

    @Override
    public String toString()
    {
        return Formatter.format("[$ to $, $]", minimum(),
                isInclusive()
                        ? maximum()
                        : maximum().next(), isInclusive()
                        ? "inclusive"
                        : "exclusive");
    }
}
