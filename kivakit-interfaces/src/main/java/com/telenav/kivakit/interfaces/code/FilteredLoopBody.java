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
import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.kivakit.interfaces.lexakai.DiagramCode;
import com.telenav.kivakit.interfaces.numeric.IntegerNumeric;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A piece of code that can be executed in a loop, and can filter out values.
 *
 * <p>
 * Looping is implemented by {@link #forCount(Value, Value, long)}, which calls {@link #at(Value)} starting at the given
 * minimum value until the required number of values are accepted. The next value for each iteration of the loop is
 * determined by {@link NextValue#next()}. The loop can terminate before reaching the requested count of values if
 * next() returns null. T
 * </p>
 *
 * <p><b>Filtering</b></p>
 *
 * <p>
 * Values presented to {@link #at(Minimizable)} can be rejected by returning {@link FilterAction#REJECT}. In this case,
 * the value is skipped, and does not count towards the requested number of values. This allows the implementation of
 * at() to filter out values while still obtaining the requested count. <i>See RandomValueFactory in kivakit-core for an
 * example of this.</i>
 * </p>
 *
 * @author jonathanl (shibo)
 * @see LoopBody
 */
@UmlClassDiagram(diagram = DiagramCode.class)
public interface FilteredLoopBody<Value extends IntegerNumeric<Value>>
{
    /**
     * Used by forEach() to determine which values should be counted, and which should not.
     */
    enum FilterAction
    {
        /** The value is rejected and should not count towards the number of values requested */
        REJECT,

        /** The value was accepted, reducing the number of values that must be returned */
        ACCEPT
    }

    /**
     * Executes the target code with next value in the loop
     *
     * @param at The loop Value
     */
    FilterAction at(Value at);

    /**
     * Executes the loop starting at the minimum value, calling {@link #at(Value)} until the given number of values are
     * accepted.
     */
    default void forCount(Value minimum, Value exclusiveMaximum, long count)
    {
        // Loop through values from the minimum until count values are accepted,
        long accepted = 0;
        for (Value value = minimum; value != null && value.asLong() < exclusiveMaximum.asLong() && accepted < count; value = value.next())
        {
            // call the body, and if it accepted the value,
            if (at(value) == FilterAction.ACCEPT)
            {
                // increase the count of accepted values.
                accepted++;
            }
        }
    }
}
