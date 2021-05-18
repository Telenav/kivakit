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

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts between different styles of casing:
 *
 * <ul>
 *     <li>camelCase</li>
 *     <li>Capitalized</li>
 *     <li>lower-hyphenated</li>
 *     <li>UPPER_UNDERSCORE</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
@LexakaiJavadoc(complete = true)
public class CaseFormat
{
    /**
     * "WebServer -&gt; "web-server" and "webServer -&gt; "web-server"
     */
    public static String camelCaseToHyphenated(final String text)
    {
        assert text != null;

        return Strip.leading(text.replaceAll("(?=[A-Z][a-z0-9])", "-"), "-").toLowerCase();
    }

    /**
     * "webServer" -&gt; "WebServer"
     */
    public static String capitalize(final String text)
    {
        if (text.length() >= 1)
        {
            return Character.toUpperCase(text.charAt(0)) + text.substring(1);
        }
        return text;
    }

    /**
     * "webServer" -&gt; "Webserver"
     */
    public static String capitalizeOnlyFirstLetter(final String text)
    {
        if (text != null && text.length() >= 1)
        {
            return Character.toUpperCase(text.charAt(0)) + text.toLowerCase().substring(1);
        }
        return text;
    }

    /**
     * "WebServer" -&gt; "webServer"
     */
    public static String decapitalize(final String text)
    {
        if (text.length() >= 1)
        {
            return Character.toLowerCase(text.charAt(0)) + text.substring(1);
        }
        return text;
    }

    /**
     * WebServer -&gt; "web-server"
     *
     * @return The simple name of the given type in hyphenated form
     */
    public static String hyphenatedName(final Class<?> type)
    {
        return camelCaseToHyphenated(type.getSimpleName());
    }

    /**
     * "web-server" -&gt; "webServer"
     */
    public static String hyphenatedToCamel(final String string)
    {
        if (string.contains("-"))
        {
            final Matcher matcher = Pattern.compile("-[a-zA-Z]").matcher(string);
            return matcher.replaceAll(result -> result.group().substring(1).toUpperCase());
        }
        return string;
    }

    /**
     * @return True if the text starts with an uppercase letter or non-letter, false otherwise
     */
    public static boolean isCapitalized(final String text)
    {
        if (text != null && !text.isEmpty())
        {
            return !Character.isLowerCase(text.charAt(0));
        }
        return false;
    }

    /**
     * @return True if the given text contains a hyphen
     */
    public static boolean isHyphenated(final String text)
    {
        return text.contains("-");
    }

    public static boolean isLowercase(final String text)
    {
        return text.equals(text.toLowerCase());
    }

    /**
     * "web-server" -&gt; "WEB_SERVER"
     */
    public static String lowerHyphenToUpperUnderscore(final String string)
    {
        return string.replaceAll("-", "_").toUpperCase();
    }

    /**
     * "WEB_SERVER" -&gt; "web-server"
     */
    public static String upperUnderscoreToLowerHyphen(final String string)
    {
        if (!string.contains("-"))
        {
            return string.replaceAll("_", "-").toLowerCase();
        }
        return string;
    }
}
