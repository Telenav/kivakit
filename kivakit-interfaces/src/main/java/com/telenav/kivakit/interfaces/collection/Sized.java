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
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramNumeric;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_REQUIRED;

/**
 * An object (usually some sort of collection or store of values) which has a {@link #size()}. The object can be
 * {@link #isEmpty()} if the size is zero or {@link #isNonEmpty()} if the size is non-zero.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramNumeric.class)
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_REQUIRED,
            documentation = DOCUMENTED)
public interface Sized extends Emptiness
{
    /**
     * Returns true if the {@link #size()} of this object is zero
     *
     * @return True if this object has a {@link #size()} of zero
     */
    default boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * Gets the size of this object
     *
     * @return The size of this object
     */
    int size();
}
