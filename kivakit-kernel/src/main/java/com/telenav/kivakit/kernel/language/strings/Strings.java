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

import java.util.regex.Pattern;

/**
 * General purpose utilities for strings
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
@LexakaiJavadoc(complete = true)
public class Strings
{
    /**
     * @return True if the given text contains any character in the given set of characters
     */
    public static boolean containsAnyOf(final String text, final String characters)
    {
        for (var i = 0; i < text.length(); i++)
        {
            final var c = text.charAt(i);
            for (var j = 0; j < characters.length(); j++)
            {
                if (characters.charAt(j) == c)
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return True if the string a contains b ignoring case differences
     */
    public static boolean containsIgnoreCase(final String a, final String b)
    {
        return a.toLowerCase().contains(b.toLowerCase());
    }

    /**
     * @return The number of digits in the given text
     */
    public static int digits(final String text)
    {
        var digits = 0;
        for (var i = 0; i < text.length(); i++)
        {
            if (Character.isDigit(text.charAt(i)))
            {
                digits++;
            }
        }
        return digits;
    }

    /**
     * @return The given text with the terminator appended if it doesn't already end in the terminator
     */
    public static String ensureEndsWith(final String text, final String terminator)
    {
        if (text.endsWith(terminator))
        {
            return text;
        }
        return text + terminator;
    }

    /**
     * Avoids null pointer issues when comparing the two string arguments
     *
     * @return True if the strings are equal
     */
    public static boolean equals(final String a, final String b)
    {
        if (a == null || b == null)
        {
            return a == null && b == null;
        }
        return a.equals(b);
    }

    /**
     * @return Group 1 of the first occurrence of pattern in text
     */
    public static String extract(final String text, final String pattern)
    {
        final var matcher = Pattern.compile(pattern).matcher(text);
        if (matcher.find())
        {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * @return True if every character in the given text is a byte value
     */
    public static boolean isAllBytes(final String text)
    {
        for (var i = 0; i < text.length(); i++)
        {
            if (text.charAt(i) > 255)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @return True if every character in the given text is an ASCII value
     */
    public static boolean isAscii(final String text)
    {
        for (var i = 0; i < text.length(); i++)
        {
            if (text.charAt(i) > 127)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @return True if the string is null, empty or contains only whitespace
     */
    public static boolean isEmpty(final String text)
    {
        return text == null || text.isEmpty() || text.trim().isEmpty();
    }

    /**
     * @return True if every character in the given string is an ASCII value
     */
    public static boolean isExtendedAscii(final String value)
    {
        for (var i = 0; i < value.length(); i++)
        {
            if (value.charAt(i) > 255)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @return True if the given text is a Java identifier
     */
    public static boolean isJavaIdentifier(final String text)
    {
        if (!isEmpty(text))
        {
            if (!Character.isJavaIdentifierStart(text.charAt(0)))
            {
                return false;
            }
            for (var i = 1; i < text.length(); i++)
            {
                if (!Character.isJavaIdentifierPart(text.charAt(i)))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isLowerCase(final String text)
    {
        return text.equals(text.toLowerCase());
    }

    /**
     * Returns true if a string is non-empty and contains only digits
     *
     * @param string The string to check
     * @return True if it's a number
     */
    public static boolean isNaturalNumber(final String string)
    {
        if (isEmpty(string))
        {
            return false;
        }
        for (var i = 0; i < string.length(); i++)
        {
            if (!Character.isDigit(string.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @return The leading n characters of the given text
     */
    public static String leading(final String text, final int n)
    {
        final var length = Math.min(text.length(), n);
        return text.substring(0, length);
    }

    public static String notNull(final String value)
    {
        return value == null ? "" : value;
    }

    /**
     * @return The index of the nth instance of the given character in the given text
     */
    public static int nthCharacter(final String text, int n, final char c)
    {
        for (var i = 0; i < text.length(); i++)
        {
            if (text.charAt(i) == c)
            {
                if (--n == 0)
                {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * @return The number of occurrences of the given character in the given text
     */
    public static int occurrences(final String text, final char character)
    {
        var count = 0;
        for (var i = 0; i < text.length(); i++)
        {
            if (text.charAt(i) == character)
            {
                count++;
            }
        }
        return count;
    }

    /**
     * @return The given text with the given range removed (end is exclusive)
     */
    public static String remove(final String text, final int start, final int end)
    {
        return text.substring(0, start) + text.substring(end);
    }

    /**
     * @return The given string with all occurrences of the given character removed
     */
    public static String removeAll(final String string, final char remove)
    {
        final var builder = new StringBuilder();
        for (var i = 0; i < string.length(); i++)
        {
            final var current = string.charAt(i);
            if (current != remove)
            {
                builder.append(current);
            }
        }
        return builder.toString();
    }

    /**
     * @return The given string with a range of characters replaced by the given replacement
     */
    public static String replace(final String string, final int start, final int end, final String replacement)
    {
        return string.substring(0, start) + replacement + string.substring(end);
    }

    /**
     * Replace all occurrences of one string replaceWith another string. Note that this does not require the compilation
     * of a regular expression and is faster.
     *
     * @param s The string to process
     * @param searchFor The value to search for
     * @param replaceWith The value to searchFor replaceWith
     * @return The resulting string with searchFor replaced with replaceWith
     */
    public static String replaceAll(final String s, final String searchFor, String replaceWith)
    {
        if (s == null)
        {
            return null;
        }

        // If searchFor is null or the empty string, then there is nothing to
        // replace, so returning s is the only option here.
        if (searchFor == null || searchFor.isEmpty())
        {
            return s;
        }

        // If replaceWith is null, then the searchFor should be replaced with
        // nothing, which can be seen as the empty string.
        if (replaceWith == null)
        {
            replaceWith = "";
        }

        // Look for first occurrence of searchFor
        var matchIndex = search(s, searchFor, 0);
        if (matchIndex == -1)
        {
            // No replace operation needs to happen
            return s;
        }
        else
        {
            // Allocate a StringBuilder that will hold one replacement
            // with a little extra room.
            var size = s.length();
            final var replaceWithLength = replaceWith.length();
            final var searchForLength = searchFor.length();
            if (replaceWithLength > searchForLength)
            {
                size += (replaceWithLength - searchForLength);
            }
            final var builder = new StringBuilder(size + 16);

            var pos = 0;
            do
            {
                // Append text up to the match
                builder.append(s.subSequence(pos, matchIndex));

                // Add replaceWith text
                builder.append(replaceWith);

                // Find next occurrence, if any
                pos = matchIndex + searchForLength;
                matchIndex = search(s, searchFor, pos);
            }
            while (matchIndex != -1);

            // Add tail of s
            builder.append(s.subSequence(pos, s.length()));

            // Return processed buffer
            return builder.toString();
        }
    }

    /**
     * @return True if the given text starts with the given prefix, ignoring case differences
     */
    public static boolean startsWithIgnoreCase(final String text, final String prefix)
    {
        return text.startsWith(prefix) || text.toUpperCase().startsWith(prefix.toUpperCase());
    }

    /**
     * @return The trailing n characters of the given string
     */
    public static String trailing(final String string, final int n)
    {
        final var length = Math.min(string.length(), n);
        return string.substring(string.length() - length);
    }

    /**
     * @return The index of the given search string in the given text, starting at the given position
     */
    private static int search(final CharSequence text, final String searchString, final int position)
    {
        if (text instanceof String)
        {
            return ((String) text).indexOf(searchString, position);
        }
        else if (text instanceof StringBuffer)
        {
            return ((StringBuffer) text).indexOf(searchString, position);
        }
        else if (text instanceof StringBuilder)
        {
            return ((StringBuilder) text).indexOf(searchString, position);
        }
        else
        {
            return text.toString().indexOf(searchString, position);
        }
    }
}
