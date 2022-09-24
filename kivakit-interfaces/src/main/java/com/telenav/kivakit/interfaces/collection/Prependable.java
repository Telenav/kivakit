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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import static com.telenav.kivakit.annotations.code.ApiStability.MORE_EVALUATION_NEEDED;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * An object, often a collection or related type, to which objects can be prepended.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "SpellCheckingInspection", "unused" })
@UmlClassDiagram(diagram = DiagramCollection.class)
@ApiQuality(stability = MORE_EVALUATION_NEEDED,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED,
            reviews = 1,
            reviewers = "shibo")
public interface Prependable<Value> extends SpaceLimited
{
    /**
     * Adds the given value
     *
     * @return True if the prepending succeeded, false otherwise
     */
    boolean onPrepend(Value value);

    /**
     * Adds the given value
     *
     * @return True if the prepending succeeded, false otherwise
     */
    default boolean prepend(Value value)
    {
        return hasRoomFor(1) && onPrepend(value);
    }

    /**
     * @param values A sequence of values to prepend, in order
     * @return True if the prepending succeeded, false otherwise
     */
    default boolean prependAll(Iterator<? extends Value> values)
    {
        var reversed = new LinkedList<Value>();
        while (values.hasNext())
        {
            reversed.add(0, values.next());
        }
        for (var value : reversed)
        {
            if (!prepend(value))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @param values A sequence of values to prepend, in order
     * @return True if the prepending succeeded, false otherwise
     */
    default boolean prependAll(Iterable<? extends Value> values)
    {
        return prependAll(values.iterator());
    }

    /**
     * @param values A sequence of values to prepend, in order
     * @return True if the prepending succeeded, false otherwise
     */
    default boolean prependAll(Collection<? extends Value> values)
    {
        if (hasRoomFor(values.size()))
        {
            return prependAll(values.iterator());
        }
        return false;
    }

    /**
     * @param values A sequence of values to prepend, in order
     * @return True if the prepending succeeded, false otherwise
     */
    default boolean prependAll(Value[] values)
    {
        return prependAll(Arrays.asList(values));
    }

    /**
     * Prepends the given value if it is not null
     *
     * @param value The value to prepend
     * @return True if the value was added, false otherwise
     */
    default boolean prependIfNotNull(Value value)
    {
        if (value != null)
        {
            return prepend(value);
        }
        return true;
    }
}
