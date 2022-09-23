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

package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramCollection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;
import java.util.LinkedList;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.NONE;

/**
 * An object, often a collection or related type, to which objects can be prepended.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "SpellCheckingInspection", "unused" })
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramCollection.class)
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = NONE,
            documentation = SUFFICIENT)
public interface Prependable<Value>
{
    /**
     * Adds the given value
     *
     * @return Self reference for chaining of append calls
     */
    Prependable<Value> prepend(Value value);

    /**
     * @param values A sequence of values to prepend, in order
     */
    default Prependable<Value> prependAll(Iterator<Value> values)
    {
        var reversed = new LinkedList<Value>();
        while (values.hasNext())
        {
            reversed.add(0, values.next());
        }
        for (var value : reversed)
        {
            prepend(value);
        }
        return this;
    }

    /**
     * @param values A sequence of values to prepend, in order
     */
    default Prependable<Value> prependAll(Iterable<Value> values)
    {
        return prependAll(values.iterator());
    }
}
