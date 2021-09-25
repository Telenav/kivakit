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

package com.telenav.kivakit.kernel.interfaces.numeric;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramExampleBaseList;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramInterfaceNumeric;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An object (usually some sort of collection or store of values) which has a size.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceNumeric.class)
@UmlClassDiagram(diagram = DiagramExampleBaseList.class)
@LexakaiJavadoc(complete = true)
public interface Sized extends Countable
{
    @Override
    default Count count()
    {
        return Count.count(size());
    }

    /**
     * @return True if the size is zero
     */
    @JsonIgnore
    default boolean isEmpty()
    {
        return size() == 0;
    }

    @JsonIgnore
    default boolean isNonEmpty()
    {
        return !isEmpty();
    }

    /**
     * @return The size of the object
     */
    int size();
}
