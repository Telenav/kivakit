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

package com.telenav.kivakit.core.collections.list;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramCollections;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;

/**
 * {@link ObjectList} already contains push and pop functionality, but this class can make it clear when an
 * {@link ObjectList} is being used as a stack.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTATION_COMPLETE)
public class Stack<Value> extends ObjectList<Value>
{
    public Stack()
    {
    }

    public Stack(Maximum maximumSize)
    {
        super(maximumSize);
    }

    public Stack(Maximum maximumSize, Collection<Value> values)
    {
        super(maximumSize, values);
    }

    public Stack(Collection<Value> values)
    {
        super(values);
    }
}
