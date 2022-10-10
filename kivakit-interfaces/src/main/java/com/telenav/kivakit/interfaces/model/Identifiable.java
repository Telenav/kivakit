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

package com.telenav.kivakit.interfaces.model;

import com.telenav.kivakit.interfaces.internal.lexakai.DiagramModel;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An object which has an identifier and is also {@link LongValued} as are many objects.
 *
 * @author jonathanl (shibo)
 * @see LongValued
 */
@UmlClassDiagram(diagram = DiagramModel.class)
public interface Identifiable extends LongValued
{
    /**
     * Returns the identifier for this object
     */
    long identifier();

    /**
     * Returns the identifier as a long value
     */
    @Override
    default long longValue()
    {
        return identifier();
    }
}
