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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.string.AsciiArt.repeat;

/**
 * Aligns a string within a given width using a padding character: {@link #center(String, int, char)},
 * {@link #leftAlign(String, int, char)} or {@link #rightAlign(String, int, char)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramString.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Align
{
    /**
     * Centers the given text to a given length, with a given character used for padding
     */
    public static String center(String text, int length, char c)
    {
        if (length > text.length())
        {
            var left = (length - text.length()) / 2;
            var right = length - text.length() - left;
            return repeat(left, c) + text + repeat(right, c);
        }
        else
        {
            return text;
        }
    }

    /**
     * Right pads the given text to the given length, with the given character
     */
    public static String leftAlign(String string, int length, char c)
    {
        if (string != null && length > string.length())
        {
            return string + repeat(length - string.length(), c);
        }
        else
        {
            return string;
        }
    }

    /**
     * Left pads the given text to the given length, with the given character
     */
    public static String rightAlign(String text, int length, char c)
    {
        if (text != null && length > text.length())
        {
            return repeat(length - text.length(), c) + text;
        }
        else
        {
            return text;
        }
    }
}
