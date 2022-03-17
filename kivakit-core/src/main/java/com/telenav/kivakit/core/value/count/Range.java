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
import com.telenav.kivakit.interfaces.code.Loopable;
import com.telenav.kivakit.interfaces.collection.NextValue;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Represents a range of Values from a minimum to a maximum.
 *
 * @param <Value> A value that must be {@link Minimizable}, {@link Maximizable} and implement {@link NextValue}.
 * @author jonathanl (shibo)
 * @see Loopable
 */
@UmlClassDiagram(diagram = DiagramCount.class)
public class Range<Value extends Minimizable<Value>
        & Maximizable<Value>
        & NextValue<Value>>

{
    public static <Value extends Minimizable<Value>
            & Maximizable<Value>
            & NextValue<Value>>
    Range<Value> exclusive(Value minimum, Value maximum)
    {
        return new Range<>(minimum, maximum);
    }

    public static <Value extends Minimizable<Value>
            & Maximizable<Value>
            & NextValue<Value>>
    Range<Value> inclusive(Value minimum, Value maximum)
    {
        return new Range<>(minimum, maximum.next());
    }

    private final Value minimum;

    private final Value maximum;

    public Range(Value minimum, Value maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public Value constrainTo(Value value)
    {
        return maximum.minimum(minimum.maximum(value));
    }

    public boolean contains(Value value)
    {
        return false;
    }

    public void loop(Loopable<Value> body)
    {
        body.forEach(minimum(), maximum());
    }

    public Value maximum()
    {
        return maximum;
    }

    public Value minimum()
    {
        return minimum;
    }
}
