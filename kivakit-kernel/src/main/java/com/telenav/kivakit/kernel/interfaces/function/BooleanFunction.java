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

package com.telenav.kivakit.kernel.interfaces.function;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramInterfaceFunction;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.function.Predicate;

/**
 * A boolean functional interface that returns true or false for a given value.
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceFunction.class)
@LexakaiJavadoc(complete = true)
public interface BooleanFunction<Value> extends Predicate<Value>
{
    /**
     * @return True if the value evaluates to true
     */
    default boolean isTrue(Value value)
    {
        return test(value);
    }
}
