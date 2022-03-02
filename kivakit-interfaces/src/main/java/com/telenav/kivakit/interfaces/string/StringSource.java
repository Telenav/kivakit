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

package com.telenav.kivakit.interfaces.string;

import com.telenav.kivakit.interfaces.project.lexakai.DiagramString;
import com.telenav.kivakit.interfaces.value.Source;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Sadly, the only reason for this class is Java type erasures. Because you cannot implement more than one {@link
 * Source} interface on an object at a time due to type erasure collisions, creating an interface like this one is the
 * only work-around.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramString.class)
public interface StringSource
{
    static StringSource of(String string)
    {
        return () -> string;
    }

    /**
     * @return The string value
     */
    String asString();
}
