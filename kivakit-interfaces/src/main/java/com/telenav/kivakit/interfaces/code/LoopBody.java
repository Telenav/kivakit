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

package com.telenav.kivakit.interfaces.code;

import com.telenav.kivakit.interfaces.collection.NextValue;
import com.telenav.kivakit.interfaces.lexakai.DiagramCode;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static java.util.Objects.requireNonNull;

/**
 * A piece of code that can be executed in a loop.
 *
 * <p>
 * Looping is implemented by {@link #forEachExclusive(Minimizable, Minimizable)}, which calls {@link #at(Value)} from
 * the given minimum value to the given maximum value (exclusive), by increments determined by {@link NextValue#next()}.
 * The loop can terminate before reaching (maximum - 1) if next returns null.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see FilteredLoopBody
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCode.class)
@FunctionalInterface
public interface LoopBody<Value extends Minimizable<Value>
        & Maximizable<Value>
        & Comparable<Value>
        & NextValue<Value>>
{
    /**
     * Executes the target code for next value in a sequence
     *
     * @param at The loop Value
     */
    void at(Value at);

    /**
     * Executes the loop for each value, <i>exclusive</i>
     */
    default void forEachExclusive(Value minimum, Value exclusiveMaximum)
    {
        requireNonNull(minimum);
        requireNonNull(exclusiveMaximum);

        // Loop through values from minimum to maximum, exclusive,
        for (Value value = minimum; value != null && value.compareTo(exclusiveMaximum) < 0; value = value.next())
        {
            // and call the body.
            at(value);
        }
    }

    /**
     * Executes the loop for each value, <i>exclusive</i>
     */
    default void forEachInclusive(Value minimum, Value inclusiveMaximum)
    {
        requireNonNull(minimum);
        requireNonNull(inclusiveMaximum);

        // Loop through values from minimum to maximum, exclusive,
        for (Value value = minimum; value != null && value.compareTo(inclusiveMaximum) <= 0; value = value.next())
        {
            // and call the body.
            at(value);
        }
    }
}
