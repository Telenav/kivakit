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
 * An object (usually some sort of collection or store of values) which has a {@link #size()}. The object can be {@link
 * #isEmpty()} if the size is zero or {@link #isNonEmpty()} if the size is non-zero. The size can also be accessed as
 * {@link Count} with the {@link #count()} method.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceNumeric.class)
@UmlClassDiagram(diagram = DiagramExampleBaseList.class)
@LexakaiJavadoc(complete = true)
public interface Sized extends Countable
{
    /**
     * The size of this object as a {@link Count}
     *
     * @return This object's size as a {@link Count}
     */
    @Override
    default Count count()
    {
        return Count.count(size());
    }

    /**
     * Returns true if the {@link #size()} of this object is zero
     *
     * @return True if this object has a {@link #size()} of zero
     */
    @JsonIgnore
    default boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * Returns true if the size of this object is non-zero.
     *
     * @return True if this object has a non-zero {@link #size()}
     */
    @JsonIgnore
    default boolean isNonEmpty()
    {
        return !isEmpty();
    }

    /**
     * Gets the size of this object
     *
     * @return The size of this object
     */
    int size();
}
