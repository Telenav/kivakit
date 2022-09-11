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

import com.telenav.kivakit.interfaces.internal.lexakai.DiagramString;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

/**
 * Interface to an object that can produce one or more different kinds of string representations. This can be useful
 * when the {@link Object#toString()} method is already being used or when other kinds of strings are needed for
 * specific purposes.
 *
 * @author jonathanl (shibo)
 */
@UmlRelation(label = "formats with", referent = Stringable.Format.class)
@UmlClassDiagram(diagram = DiagramString.class)
public interface Stringable extends StringSource
{
    /**
     * The type of format for a string returned by {@link Stringable#asString(Format)}
     */
    @UmlClassDiagram(diagram = DiagramString.class)
    @UmlExcludeSuperTypes enum Format
    {
        DEBUG,
        FILESYSTEM,
        HTML,
        PROGRAMMATIC,
        TEXT,
        TO_STRING,
        USER_MULTILINE,
        USER_SINGLE_LINE,
        USER_LABEL,
        COMPACT,
        LOG;

        public boolean isHtml()
        {
            return equals(HTML);
        }

        public boolean isText()
        {
            return equals(TEXT);
        }
    }

    /**
     * @return A string representation of this object that is suitable for the given purpose
     */
    String asString(Format format);

    @Override
    default String asString()
    {
        return asString(Format.TEXT);
    }
}
