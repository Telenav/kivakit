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

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

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
@UmlClassDiagram(diagram = DiagramLanguageString.class)
@LexakaiJavadoc(complete = true)
public class AsciiArt
{
    private static final boolean isMac = OperatingSystem.get().isMac();

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
     * @return The message centered on a line as a banner
     */
    public static String banner(final String message)
    {
        return Align.center(" " + message + " ", LINE_LENGTH, HORIZONTAL_LINE_CHARACTER);
    }

    public static String bottomLine(final int width)
    {
        return BOTTOM_LEFT_LINE_CHARACTER + line(width - 2) + BOTTOM_RIGHT_LINE_CHARACTER;
    }

    public static String bottomLine(final int extraWidth, String message, final Object... arguments)
    {
        message = " " + Message.format(message, arguments) + " ";
        return BOTTOM_LEFT_LINE_CHARACTER + line(4) + message
                + line(Math.max(4, LINE_LENGTH + extraWidth - 6 - message.length()))
                + BOTTOM_RIGHT_LINE_CHARACTER;
    }

    public static String bottomLine(final String message, final Object... arguments)
    {
        return bottomLine(0, message, arguments);
    }

    public static String bottomLine()
    {
        return bottomLine(LINE_LENGTH);
    }

    /**
     * @return A box using the given horizontal and vertical line drawing characters that contains the given message
     */
    public static String box(final String message, final char horizontal, final char vertical)
    {
        final var builder = new StringBuilder();
        final var width = widestLine(message);
        builder.append(repeat(width + 6, horizontal));
        builder.append('\n');
        final var lines = message.split("\n");
        for (final var line : lines)
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
    public static String box(final String message, final Object... arguments)
    {
        return box(Message.format(message, arguments),
                HORIZONTAL_LINE_CHARACTER, VERTICAL_LINE_CHARACTER);
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
    public static String bulleted(final Collection<?> values)
    {
        return bulleted(1, values);
    }

    /**
     * @return Collection of values as a bulleted list using the given bullet
     */
    public static String bulleted(final Collection<?> values, final String bullet)
    {
        return bulleted(1, values, bullet);
    }

    /**
     * @return Collection of values as an indented bulleted list
     */
    public static String bulleted(final int indent, final Collection<?> values)
    {
        return bulleted(indent, values, bullet());
    }

    /**
     * @return Collection of values as an indented bulleted list using the given bullet
     */
    public static String bulleted(final int indent, final Collection<?> values, final String bullet)
    {
        if (values.isEmpty())
        {
            return "";
        }
        else
        {
            final var prefix = spaces(indent) + bullet + " ";
            return prefix + Join.join(values, "\n" + prefix);
        }
    }

    /**
     * @return The given text clipped at n characters with "[...]" appended if it is longer than n characters
     */
    public static String clip(final String text, final int n)
    {
        if (text.length() < n)
        {
            return text;
        }
        final var suffix = " [...]";
        return Strings.leading(text, n - suffix.length()) + suffix;
    }

    /**
     * Returns true if a string is non-empty and contains only digits
     *
     * @param string The string to check
     * @return True if it's a number
     */
    public static boolean isNaturalNumber(final String string)
    {
        if (Strings.isEmpty(string))
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
     * @return A line
     */
    public static String line()
    {
        return line(LINE_LENGTH);
    }

    /**
     * @return A line of the given number of characters
     */
    public static String line(final int length)
    {
        return repeat(length, HORIZONTAL_LINE_CHARACTER);
    }

    /**
     * @return A left-justified line with the given message
     */
    public static String line(final String message)
    {
        return line(4) + " " + message + " " + line(Math.max(4, LINE_LENGTH - 6 - message.length()));
    }

    /**
     * @return The number of lines in the string
     */
    public static int lineCount(final String string)
    {
        return Strings.occurrences(string, '\n') + 1;
    }

    /**
     * @param times Number of times to repeat character
     * @param c Character to repeat
     * @return Repeated character string
     */
    public static String repeat(final int times, final char c)
    {
        Ensure.ensure(times >= 0, "Times cannot be " + times);
        final var buffer = new char[times];
        Arrays.fill(buffer, c);
        return new String(buffer);
    }

    /**
     * @param times Number of times to repeat string
     * @param string String to repeat
     * @return Repeated string
     */
    public static String repeat(final int times, final String string)
    {
        Ensure.ensure(times >= 0);
        return string.repeat(times);
    }

    /**
     * @return The given number of spaces
     */
    public static String spaces(final int count)
    {
        return repeat(count, ' ');
    }

    /**
     * @return An ASCII art box with the given title and message
     */
    public static String textBox(String title, String message, final Object... arguments)
    {
        title = title(title);
        message = Message.format(message, arguments);
        final var width = widestLine(title + "\n" + message) + 4;
        final var builder = new StringBuilder();
        builder.append(" \n");
        builder.append(TOP_LEFT_LINE_CHARACTER).append(Align.center(title, width - 2, HORIZONTAL_LINE_CHARACTER)).append(TOP_RIGHT_LINE_CHARACTER).append("\n");
        for (final var line : message.split("\n"))
        {
            builder.append(VERTICAL_LINE_CHARACTER).append(" ");
            builder.append(Align.left(line, width - 4, ' '));
            builder.append(" ").append(VERTICAL_LINE_CHARACTER);
            builder.append('\n');
        }
        builder.append(BOTTOM_LEFT_LINE_CHARACTER).append(line(width - 2)).append(BOTTOM_RIGHT_LINE_CHARACTER);
        builder.append("\n ");
        return builder.toString();
    }

    @NotNull
    public static String title(final String title)
    {
        return TITLE_LEFT_CHARACTER + " " + title + " " + TITLE_RIGHT_CHARACTER;
    }

    public static String topLine(final String title, final Object... arguments)
    {
        return topLine(0, title, arguments);
    }

    public static String topLine(final int extraWidth, String title, final Object... arguments)
    {
        title = title(Message.format(title, arguments));
        return (extraWidth > 0 ? " \n" : "")
                + TOP_LEFT_LINE_CHARACTER + line(4) + title
                + line(Math.max(4, LINE_LENGTH + extraWidth - 6 - title.length()))
                + TOP_RIGHT_LINE_CHARACTER;
    }

    /**
     * @return The length of the widest line in potentially multi-line text
     */
    public static int widestLine(final String text)
    {
        var width = 0;
        final var lines = text.split("\n");
        for (final var line : lines)
        {
            width = Math.max(width, line.length());
        }
        return width;
    }
}
