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

package com.telenav.kivakit.interfaces.function;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.factory.Factory;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramFactory;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Interface to code that maps a parameter value to a result
 * <p>
 * For factories that do not need a parameter, see {@link Factory}
 *
 * @param <Value> The type of object to create
 * @author jonathanl (shibo)
 * @see Factory
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramFactory.class)
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface Mapper<Parameter, Value>
{
    /**
     * Creates a value by constructing an object with the given parameter
     *
     * @param parameter The constructor parameter for creating a new object
     * @return The new object instance
     */
    Value map(Parameter parameter);
}
