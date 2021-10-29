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

package com.telenav.kivakit.kernel.language.strings.conversion;

import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramExampleBaseList;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramInterfaceString;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

/**
 * Interface to an object that can produce one or more different kinds of string representations. This can be useful
 * when the {@link Object#toString()} method is already being used or when other kinds of strings are needed for
 * specific purposes.
 * <p>
 * The {@link StringFormat} enum defines different kinds of string representations which can be retrieved with {@link
 * #asString(StringFormat)}. The object can override this method to provide multiple string representations for specific
 * purposes. The object <i>must</i> override {@link #asString()} to provide a default representation for any purpose.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceString.class)
@UmlRelation(label = "formats with", referent = StringFormat.class)
@UmlClassDiagram(diagram = DiagramExampleBaseList.class)
public interface AsString
{
    /**
     * @return A string representation of this object that is suitable for the given purpose
     */
    default String asString(StringFormat format)
    {
        switch (format.identifier())
        {
            case StringFormat.DEBUGGER_IDENTIFIER:
            case StringFormat.LOG_IDENTIFIER:
                return new ObjectFormatter(this).toString();

            default:
                return toString();
        }
    }

    /**
     * @return A string representation of this object that is suitable for any purpose
     */
    default String asString()
    {
        return asString(StringFormat.TEXT);
    }
}
