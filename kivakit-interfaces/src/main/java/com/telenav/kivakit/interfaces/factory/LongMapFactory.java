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

import com.telenav.kivakit.interfaces.lexakai.DiagramFactory;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A {@link MapFactory} that maps a primitive long value to an object.
 *
 * @param <Value> The type of object to create
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramFactory.class)
public interface LongMapFactory<Value>
{
    /**
     * Creates a value by constructing an object with the given parameter
     *
     * @param parameter The constructor parameter for creating a new object
     * @return The new object instance
     */
    Value newInstance(long parameter);
}
