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

package com.telenav.kivakit.kernel.language.strings;

import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.iteration.Iterables;
import com.telenav.kivakit.kernel.language.iteration.Next;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Splits strings into a sequence of values on a delimiter. To convert a string into a {@link StringList} use {@link
 * StringList#split(String, char)} or {@link StringList#split(String, String)}.
 *
 * @author jonathanl (shibo)
 * @see StringList
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
@LexakaiJavadoc(complete = true)
public class Split
{
    /**
     * @return The sequence of strings resulting from splitting the given text using the given delimiter
     */
    public static Iterable<String> split(final String text, final char delimeter)
    {
        return Iterables.iterable(() -> new Next<>()
        {
            int pos;

            int next;

            @Override
            public String onNext()
            {
                // If we still have string left to search
                if (pos >= 0)
                {
                    // find the next delimiter location
                    next = text.indexOf(delimeter, pos);

                    // If there's no delimiter
                    if (next == -1)
                    {
                        // return the tail and we're done
                        final var tail = text.substring(pos);
                        pos = -1;
                        return tail;
                    }
                    else
                    {
                        // Return the value up to the delimiter and advance to the next
                        final var mid = text.substring(pos, next);
                        pos = next + 1;
                        return mid;
                    }
                }
                return null;
            }
        });
    }

    /**
     * @return The sequence of strings resulting from splitting the given text using the given delimiter
     */
    public static Iterable<String> split(final String value, final String delimeter)
    {
        return Iterables.iterable(() -> new Next<>()
        {
            int pos;

            int next;

            @Override
            public String onNext()
            {
                // If we still have string left to search
                if (pos >= 0)
                {
                    // find the next delimiter location
                    next = value.indexOf(delimeter, pos);

                    // If there's no delimiter
                    if (next == -1)
                    {
                        // return the tail and we're done
                        final var tail = value.substring(pos);
                        pos = -1;
                        return tail;
                    }
                    else
                    {
                        // Return the value up to the delimiter and advance to the next
                        final var mid = value.substring(pos, next);
                        pos = next + 1;
                        return mid;
                    }
                }
                return null;
            }
        });
    }

    /**
     * @return The two strings resulting from splitting the given text using the given separator
     */
    public static String[] splitOnFirst(final String text, final char separator)
    {
        final var index = text.indexOf(separator);
        if (index >= 0)
        {
            final var values = new String[2];
            values[0] = text.substring(0, index);
            values[1] = text.substring(index + 1);
            return values;
        }
        return null;
    }
}
