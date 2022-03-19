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

/**
 * A piece of code that can be executed in a loop. The {@link #at(Value)} method is called with the next value for each
 * execution of the loop.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramCode.class)
public interface Loopable<Value extends Minimizable<Value>
        & Maximizable<Value>
        & NextValue<Value>>
{
    /**
     * Executes the target code for next value in a sequence
     *
     * @param at The loop Value
     */
    void at(Value at);

    /**
     * Executes the loop for each value
     */
    default void forEach(Value minimum, Value maximum)
    {
        for (Value at = minimum; !at.equals(maximum); at = at.next())
        {
            at(at);
        }
    }
}
