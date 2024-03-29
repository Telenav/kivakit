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

package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.internal.lexakai.DiagramTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Interface to an object that has a time of last modification property that can be set
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramTime.class)
public interface Modifiable
{
    /**
     * Sets the time of last modification
     */
    default boolean lastModified(Time time)
    {
        throw new UnsupportedOperationException("Modification of " + getClass() + " is not supported");
    }
}
