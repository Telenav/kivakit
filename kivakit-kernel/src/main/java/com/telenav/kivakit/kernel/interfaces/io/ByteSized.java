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

package com.telenav.kivakit.kernel.interfaces.io;

import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An interface to any object whose size can be measured in bytes.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
@LexakaiJavadoc(complete = true)
public interface ByteSized
{
    /**
     * @return The number of bytes in this object
     */
    Bytes bytes();

    default boolean isLargerThan(final ByteSized that)
    {
        return bytes().isGreaterThan(that.bytes());
    }

    default boolean isSmallerThan(final ByteSized that)
    {
        return bytes().isLessThan(that.bytes());
    }
}
