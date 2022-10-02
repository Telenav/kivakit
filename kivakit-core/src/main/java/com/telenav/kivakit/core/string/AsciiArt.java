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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.internal.lexakai.DiagramString;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.string.Join.join;

/**
 * Provides methods to create "ASCII art", including:
 *
 * <ul>
 *     <li>Banners</li>
 *     <li>Lines</li>
 *     <li>Boxes</li>
 *     <li>Bulleted lists</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramString.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class AsciiArt
{
    private static final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");

    private static final char HORIZONTAL_LINE_CHARACTER = isMac ? '\u2501' : '-';

    private static final char VERTICAL_LINE_CHARACTER = isMac ? '\u250b' : '|';

    private static final char TOP_LEFT_LINE_CHARACTER = isMac ? '\u250f' : '/';

    private static final char TOP_RIGHT_LINE_CHARACTER = isMac ? '\u2513' : '\\';

    private static final char BOTTOM_LEFT_LINE_CHARACTER = isMac ? '\u2517' : '\\';

    private static final char BOTTOM_RIGHT_LINE_CHARACTER = isMac ? '\u251b' : '/';

    public static final char TITLE_LEFT_CHARACTER = isMac ? '\u252b' : '|';

    public static final char TITLE_RIGHT_CHARACTER = isMac ? '\u2523' : '|';

    public static final int LINE_LENGTH = 100;

    /**
     * Returns the given message centered on a line as a banner
     */
    public static String bannerLine(String message)
    {
        return Align.center(" " + message + " ", LINE_LENGTH, HORIZONTAL_LINE_CHARACTER);
    }

    /**
     * Returns a bottom line of the given width
     */
    public static String bottomLine(int width)
    {
        return BOTTOM_LEFT_LINE_CHARACTER + line(width - 2) + BOTTOM_RIGHT_LINE_CHARACTER;
    }

    /**
     * Returns a bottom line with the given message
     */
    public static String bottomLine(int extraWidth, String message, Object... arguments)
    {
        message = " " + Strings.format(message, arguments) + " ";
        return BOTTOM_LEFT_LINE_CHARACTER + line(4) + message
                + line(Math.max(4, LINE_LENGTH + extraWidth - 6 - message.length()))
                + BOTTOM_RIGHT_LINE_CHARACTER;
    }

    /**
     * Returns a bottom line with the given message
     */
    public static String bottomLine(String message, Object... arguments)
    {
        return bottomLine(0, message, arguments);
    }

    /**
     * Returns a bottom line
     */
    public static String bottomLine()
    {
        return bottomLine(LINE_LENGTH);
    }

    /**
     * @return A box using the given horizontal and vertical line drawing characters that contains the given message
     */
    public static String box(String message, char horizontal, char vertical)
    {
        var builder = new StringBuilder();
        var width = widestLine(message);
        builder.append(repeat(width + 6, horizontal));
        builder.append('\n');
        var lines = message.split("\n");
        for (var line : lines)
        {
            builder.append(vertical);
            builder.append("  ");
            builder.append(Align.left(line, width, ' '));
            builder.append("  ");
            builder.append(vertical);
            builder.append('\n');
        }
        builder.append(repeat(width + 6, horizontal));
        return builder.toString();
    }

    /**
     * @return An ASCII art box containing the given message
     */
    public static String box(String message, Object... arguments)
    {
        return box(Strings.format(message, arguments), HORIZONTAL_LINE_CHARACTER, VERTICAL_LINE_CHARACTER);
    }

    /**
     * @return The string to use as a bullet
     */
    public static String bullet()
    {
        return isMac ? "○" : "o";
    }

    /**
     * @return Collection of values as a bulleted list
     */
    public static String bulleted(Collection<?> values)
    {
        return bulleted(1, values);
    }

    /**
     * @return Collection of values as a bulleted list using the given bullet
     */
    public static String bulleted(Collection<?> values, String bullet)
    {
        return bulleted(1, values, bullet);
    }

    /**
     * @return Collection of values as an indented bulleted list
     */
    public static String bulleted(int indent, Collection<?> values)
    {
        return bulleted(indent, values, bullet());
    }

    /**
     * @return Collection of values as an indented bulleted list using the given bullet
     */
    public static String bulleted(int indent, Collection<?> values, String bullet)
    {
        if (values.isEmpty())
        {
            return "";
        }
        else
        {
            var prefix = spaces(indent) + bullet + " ";
            return prefix + join(values, "\n" + prefix);
        }
    }

    /**
     * @return The given text clipped at n characters with "[...]" appended if it is longer than n characters
     */
    public static String clip(String text, int n)
    {
        if (text.length() < n)
        {
            return text;
        }
        var suffix = " [...]";
        return Strings.leading(text, n - suffix.length()) + suffix;
    }

    /**
     * @return A line
     */
    public static String line()
    {
        return line(LINE_LENGTH);
    }

    /**
     * @return A line of the given number of characters
     */
    public static String line(int length)
    {
        return repeat(length, HORIZONTAL_LINE_CHARACTER);
    }

    /**
     * @return A left-justified line with the given message
     */
    public static String line(String message)
    {
        return line(4) + " " + message + " " + line(Math.max(4, LINE_LENGTH - 6 - message.length()));
    }

    /**
     * @return The number of lines in the string
     */
    public static int lineCount(String string)
    {
        return Strings.occurrences(string, '\n') + 1;
    }

    /**
     * @param times Number of times to repeat character
     * @param c Character to repeat
     * @return Repeated character string
     */
    public static String repeat(int times, char c)
    {
        Ensure.ensure(times >= 0, "Times cannot be " + times);
        var buffer = new char[times];
        Arrays.fill(buffer, c);
        return new String(buffer);
    }

    /**
     * @param times Number of times to repeat string
     * @param string String to repeat
     * @return Repeated string
     */
    public static String repeat(int times, String string)
    {
        Ensure.ensure(times >= 0);
        return string.repeat(times);
    }

    /**
     * @return The given number of spaces
     */
    public static String spaces(int count)
    {
        return repeat(count, ' ');
    }

    /**
     * @return An ASCII art box with the given title and message
     */
    public static String textBox(String title, String message, Object... arguments)
    {
        title = title(title);
        message = Strings.format(message, arguments);
        var width = widestLine(title + "\n" + message) + 4;
        var builder = new StringBuilder();
        builder.append(TOP_LEFT_LINE_CHARACTER).append(Align.center(title, width - 2, HORIZONTAL_LINE_CHARACTER)).append(TOP_RIGHT_LINE_CHARACTER).append("\n");
        for (var line : message.split("\n"))
        {
            builder.append(VERTICAL_LINE_CHARACTER).append(" ");
            builder.append(Align.left(line, width - 4, ' '));
            builder.append(" ").append(VERTICAL_LINE_CHARACTER);
            builder.append('\n');
        }
        builder.append(BOTTOM_LEFT_LINE_CHARACTER).append(line(width - 2)).append(BOTTOM_RIGHT_LINE_CHARACTER);
        return builder.toString();
    }

    /**
     * Returns a top line with the given title
     */
    public static String topLine(String title, Object... arguments)
    {
        return topLine(0, title, arguments);
    }

    /**
     * Returns a top line with the given title
     */
    public static String topLine(int extraWidth, String title, Object... arguments)
    {
        title = title(title, arguments);
        return (extraWidth > 0 ? " \n" : "")
                + TOP_LEFT_LINE_CHARACTER + line(4) + title
                + line(Math.max(4, LINE_LENGTH + extraWidth - 6 - title.length()))
                + TOP_RIGHT_LINE_CHARACTER;
    }

    /**
     * @return The length of the widest line in potentially multi-line text
     */
    public static int widestLine(String text)
    {
        var width = 0;
        var lines = text.split("\n");
        for (var line : lines)
        {
            width = Math.max(width, line.length());
        }
        return width;
    }

    @NotNull
    private static String title(String title, Object... arguments)
    {
        return TITLE_LEFT_CHARACTER + " " + Formatter.format(title, arguments) + " " + TITLE_RIGHT_CHARACTER;
    }
}
