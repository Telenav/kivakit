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

package com.telenav.kivakit.kernel.interfaces.factory;

import com.telenav.kivakit.kernel.interfaces.value.Source;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramInterfaceFactory;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * A factory that creates an object.
 * <p>
 * For factories that require a parameter to create the object, see {@link MapFactory}.
 *
 * @param <Value> The type of object to create
 * @author jonathanl (shibo)
 * @see MapFactory
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceFactory.class)
public interface Factory<Value> extends Source<Value>
{
    /**
     * @return New instance of the given type
     */
    @Override
    @UmlExcludeMember
    default Value get()
    {
        return newInstance();
    }

    /**
     * @return A new instance of the given type
     */
    Value newInstance();
}
