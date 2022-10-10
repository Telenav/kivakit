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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

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
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramString.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class CaseFormat
{
    /**
     * "WebServer -&gt; "web-server" and "webServer -&gt; "web-server"
     */
    public static String camelCaseToHyphenated(String text)
    {
        assert text != null;

        return Strip.leading(text.replaceAll("(?=[A-Z][a-z0-9])", "-"), "-").toLowerCase();
    }

    /**
     * "webServer" -&gt; "WebServer"
     */
    public static String capitalize(String text)
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
    public static String capitalizeOnlyFirstLetter(String text)
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
    @SuppressWarnings("SpellCheckingInspection")
    public static String decapitalize(String text)
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
    public static String hyphenatedName(Class<?> type)
    {
        return camelCaseToHyphenated(type.getSimpleName());
    }

    /**
     * "web-server" -&gt; "webServer"
     */
    public static String hyphenatedToCamel(String string)
    {
        if (string.contains("-"))
        {
            Matcher matcher = Pattern.compile("-[a-zA-Z]").matcher(string);
            return matcher.replaceAll(result -> result.group().substring(1).toUpperCase());
        }
        return string;
    }

    /**
     * Returns true if the text starts with an uppercase letter or non-letter, false otherwise
     */
    public static boolean isCapitalized(String text)
    {
        if (text != null && !text.isEmpty())
        {
            return !Character.isLowerCase(text.charAt(0));
        }
        return false;
    }

    /**
     * Returns true if the given text contains a hyphen
     */
    public static boolean isHyphenated(String text)
    {
        return text.contains("-");
    }

    /**
     * Returns true if the given text is in lowercase
     */
    public static boolean isLowercase(String text)
    {
        return text.equals(text.toLowerCase());
    }

    /**
     * "web-server" -&gt; "WEB_SERVER"
     */
    public static String lowerHyphenToUpperUnderscore(String string)
    {
        return string.replaceAll("-", "_").toUpperCase();
    }

    /**
     * "WEB_SERVER" -&gt; "web-server"
     */
    public static String upperUnderscoreToLowerHyphen(String string)
    {
        if (!string.contains("-"))
        {
            return string.replaceAll("_", "-").toLowerCase();
        }
        return string;
    }
}
