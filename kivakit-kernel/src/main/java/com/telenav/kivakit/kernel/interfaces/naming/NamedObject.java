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

package com.telenav.kivakit.kernel.interfaces.naming;

import com.telenav.kivakit.kernel.language.values.name.Name;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramInterfaceNaming;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An object with a <i>programmatic</i> name. An object that is a {@link NamedObject} differs from an object that is
 * {@link Named} in that its name is meant for use by programmers and not end users. If {@link #objectName()} is not
 * implemented, a synthetic name for the object is created with {@link Name#synthetic(Object)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceNaming.class)
public interface NamedObject
{
    default boolean hasSyntheticName()
    {
        return Name.isSynthetic(objectName());
    }

    /**
     * @return The name of this object for use in programming and debugging. If this method is not overridden, the name
     * will be the simple class name in hyphenated form followed by this object's identity hash code in hexadecimal.
     */
    default String objectName()
    {
        return Name.synthetic(this);
    }

    /**
     * Sets the name of this object
     */
    default void objectName(String name)
    {
    }
}
