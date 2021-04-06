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

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class Normalize
{
    private static final Pattern SYMBOLS_AND_ACCENTS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    /**
     * Normalizes all non-English accented and symbol characters in a String.
     */
    public static String normalizeSymbolsAndAccents(String string)
    {
        if (Strings.isEmpty(string))
        {
            string = "";
        }
        // We remove the degree' and 'german sharp s' characters, which cause parsing problems on
        // traffic client
        string = Strings.replaceAll(string.replace('\u00B0', 'o'), "\u00DF", "ss");
        final var normalized = Normalizer.normalize(string, Normalizer.Form.NFD);
        return SYMBOLS_AND_ACCENTS.matcher(normalized).replaceAll("");
    }
}
