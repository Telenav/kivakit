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

package com.telenav.kivakit.interfaces.factory;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramFactory;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * A factory that creates an object given a parameter (it "maps" from the parameter to the new object).
 * <p>
 * For factories that do not need a parameter, see {@link Factory}
 *
 * @param <Value> The type of object to create
 * @param <Parameter> A parametric value for the implementation to use when creating the object
 * @author jonathanl (shibo)
 * @see Factory
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramFactory.class)
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = DOCUMENTATION_COMPLETE)
public interface MapFactory<Parameter, Value>
{
    /**
     * Creates a value by constructing an object with the given parameter
     *
     * @param parameter The constructor parameter for creating a new object
     * @return The new object instance
     */
    Value newInstance(Parameter parameter);
}
