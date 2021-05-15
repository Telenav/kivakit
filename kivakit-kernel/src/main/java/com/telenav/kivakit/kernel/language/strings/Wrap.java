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

package com.telenav.kivakit.kernel.language.strings;

import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.regex.Pattern;

/**
 * Wraps text at a given width.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
@LexakaiJavadoc(complete = true)
public class Wrap
{
    /**
     * @return The given text wrapped at the given width
     */
    public static String wrap(final String text, final int width)
    {
        final var wrapped = new StringBuilder();
        int position = 0;
        for (final var word : StringList.words(text))
        {
            if (position + word.length() > width)
            {
                wrapped.append("\n");
                position = 0;
            }
            wrapped.append(position == 0 ? "" : " ").append(word);
            position += word.length();
        }
        return wrapped.toString();
    }

    /**
     * @return The given string with all text between [wrap] and [end] markers wrapped.
     */
    public static String wrapRegion(final String text, final int width)
    {
        final var matcher = Pattern.compile("\\[wrap](.*?)\\[end]", Pattern.DOTALL).matcher(text);
        return matcher.replaceAll(result -> wrap(result.group(1), width));
    }
}