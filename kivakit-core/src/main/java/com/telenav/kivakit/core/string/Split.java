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

import com.telenav.kivakit.core.project.lexakai.DiagramString;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * String splitting utilities.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramString.class)
@LexakaiJavadoc(complete = true)
public class Split
{
    /**
     * Splits the given text on the given delimiter, returning a list of strings
     *
     * @param text The text to split
     * @param delimiter The delimiter to split on
     * @return The list of strings
     */
    public static List<String> split(String text, String delimiter)
    {
        return Arrays.stream(text.split(delimiter, Integer.MAX_VALUE))
                .collect(Collectors.toList());
    }

    /**
     * @return The two strings resulting from splitting the given text using the given separator
     */
    public static String[] splitOnFirst(String text, char separator)
    {
        var index = text.indexOf(separator);
        if (index >= 0)
        {
            var values = new String[2];
            values[0] = text.substring(0, index);
            values[1] = text.substring(index + 1);
            return values;
        }
        return null;
    }

    /**
     * @return A list of words from the given text with word breaks occurring on whitespace
     */
    public static List<String> words(String text)
    {
        var list = new ArrayList<String>();

        var startOfWord = -1;
        var length = text.length();
        for (int at = 0; at < length; at++)
        {
            switch (text.charAt(at))
            {
                case ' ':
                case '\t':
                case '\n':
                    if (startOfWord >= 0)
                    {
                        list.add(text.substring(startOfWord, at));
                        startOfWord = -1;
                    }
                    break;

                default:
                    if (startOfWord < 0)
                    {
                        startOfWord = at;
                    }
                    break;
            }
        }

        if (startOfWord >= 0)
        {
            list.add(text.substring(startOfWord));
        }

        return list;
    }
}
