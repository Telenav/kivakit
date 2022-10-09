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

import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * Wraps text at a given width.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramString.class)
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class Wrap
{
    /**
     * @return The given text wrapped at the given width
     */
    public static String wrap(String text, int maximumWidth)
    {
        var wrapped = new StringBuilder();
        int position = 0;
        for (var word : Split.splitIntoWords(text))
        {
            if (position + word.length() > maximumWidth)
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
    public static String wrapRegion(String text, int maximumWidth)
    {
        var matcher = Pattern.compile("\\[wrap](.*?)\\[end]", Pattern.DOTALL).matcher(text);
        return matcher.replaceAll(result -> wrap(result.group(1), maximumWidth));
    }
}
