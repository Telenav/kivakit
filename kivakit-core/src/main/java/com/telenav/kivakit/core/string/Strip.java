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

package com.telenav.kivakit.core.string;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Strips leading and ending values, package prefixes and quotes from strings.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramString.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Strip
{
    /**
     * Strips the ending from the string <code>text</code>.
     *
     * @param text The text to strip
     * @param ending The ending to strip off
     * @return The stripped string or the original string if the ending did not exist
     */
    public static String stripEnding(String text, String ending)
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
        var endingLength = ending.length();
        var stringLength = text.length();

        // When the length of the ending string is larger
        // than the original string, the original string is returned.
        if (endingLength > stringLength)
        {
            return text;
        }
        var index = text.lastIndexOf(ending);
        var end = stringLength - endingLength;

        if (index == end)
        {
            return text.substring(0, end);
        }

        return text;
    }

    /**
     * Returns the text with the leading string stripped if it exists
     */
    public static String stripLeading(String text, String leading)
    {
        if (text.startsWith(leading))
        {
            return text.substring(leading.length());
        }
        return text;
    }

    public static String stripQuotes(String text)
    {
        var substring = text.substring(1, text.length() - 1);
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
     * Returns the text with the trailing string stripped if it exists
     */
    public static String stripTrailing(String text, String trailer)
    {
        if (text.endsWith(trailer))
        {
            return text.substring(0, text.length() - trailer.length());
        }
        return text;
    }
}
