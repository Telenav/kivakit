////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.strings;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class Strip
{
    /**
     * Strips the ending from the string <code>s</code>.
     *
     * @param text The text to strip
     * @param ending The ending to strip off
     * @return The stripped string or the original string if the ending did not exist
     */
    public static String ending(final String text, final String ending)
    {
        if (text == null)
        {
            return null;
        }

        // Stripping a null or empty string from the end returns the
        // original string.
        if ((ending == null) || "".equals(ending))
        {
            return text;
        }
        final var endingLength = ending.length();
        final var stringLength = text.length();

        // When the length of the ending string is larger
        // than the original string, the original string is returned.
        if (endingLength > stringLength)
        {
            return text;
        }
        final var index = text.lastIndexOf(ending);
        final var endpos = stringLength - endingLength;

        if (index == endpos)
        {
            return text.substring(0, endpos);
        }

        return text;
    }

    /**
     * @return The text with the leading string stripped if it exists
     */
    public static String leading(final String text, final String leading)
    {
        if (text.startsWith(leading))
        {
            return text.substring(leading.length());
        }
        return text;
    }

    public static String quotes(final String text)
    {
        final var substring = text.substring(1, text.length() - 1);
        if (text.startsWith("\"") && text.endsWith(("\"")))
        {
            return substring;
        }
        if (text.startsWith("'") && text.endsWith(("'")))
        {
            return substring;
        }
        return text;
    }

    /**
     * @return The text with the trailing string stripped if it exists
     */
    public static String trailing(final String text, final String trailer)
    {
        if (text.endsWith(trailer))
        {
            return text.substring(0, text.length() - trailer.length());
        }
        return text;
    }
}
