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

package com.telenav.kivakit.kernel.interfaces.comparison;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramInterfaceComparison;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.function.Predicate;

/**
 * A matcher when the term predicate is often not the right meaning to use. Implements Predicate for interoperation.
 *
 * @param <Value> The type of value to match
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceComparison.class)
public interface Matcher<Value> extends Predicate<Value>
{
    /**
     * @return True if the given value matches
     */
    boolean matches(Value value);

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean test(final Value value)
    {
        return matches(value);
    }
}
