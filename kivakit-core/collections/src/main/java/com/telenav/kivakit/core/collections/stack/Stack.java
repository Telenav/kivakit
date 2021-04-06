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

package com.telenav.kivakit.core.collections.stack;

import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramStack;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;

/**
 * An object list with {@link #push(Object)} and {@link #pop()}
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramStack.class)
public class Stack<T> extends ObjectList<T>
{
    public Stack()
    {
    }

    public Stack(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    /**
     * @return Pop the value off of the top of the stack
     */
    public T pop()
    {
        return isEmpty() ? null : removeLast();
    }

    /**
     * Push the given value onto the top of the stack
     */
    public void push(final T value)
    {
        add(value);
    }
}
