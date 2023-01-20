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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramString;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.logging.logs.text.LogFormatter.logFormatter;
import static com.telenav.kivakit.core.os.OperatingSystem.operatingSystem;
import static com.telenav.kivakit.core.string.Align.center;
import static com.telenav.kivakit.core.string.Align.leftAlign;
import static com.telenav.kivakit.core.string.AsciiArt.TextBoxStyle.CLOSED;
import static com.telenav.kivakit.core.string.AsciiArt.TextBoxStyle.OPEN;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.string.Join.join;
import static com.telenav.kivakit.core.string.Strings.leading;
import static com.telenav.kivakit.core.string.Strings.occurrences;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.max;
import static java.lang.Math.min;

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
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class AsciiArt
{
    private static final boolean isMac = operatingSystem().isMac();

    private static final char HORIZONTAL_LINE_CHARACTER = isMac ? '━' : '-';

    private static final char VERTICAL_LINE_CHARACTER = isMac ? '┋' : '|';

    private static final char TOP_LEFT_LINE_CHARACTER = isMac ? '┏' : '/';

    private static final char TOP_RIGHT_LINE_CHARACTER = isMac ? '┓' : '\\';

    private static final char BOTTOM_LEFT_LINE_CHARACTER = isMac ? '┗' : '\\';

    private static final char BOTTOM_RIGHT_LINE_CHARACTER = isMac ? '┛' : '/';

    public static final char TITLE_LEFT_CHARACTER = isMac ? '┫' : '|';

    public static final char TITLE_RIGHT_CHARACTER = isMac ? '┣' : '|';

    private static int maximumWidth = 90;

    /**
     * Returns the given message centered on a line as a banner
     */
    public static String bannerLine(String message)
    {
        return center(" " + message + " ", maximumWidth, HORIZONTAL_LINE_CHARACTER);
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
    public static String bottomLine(int width, String message, Object... arguments)
    {
        message = " " + format(message, arguments) + " ";
        return BOTTOM_LEFT_LINE_CHARACTER + line(4) + message
            + line(max(4, width - 6 - message.length()))
            + BOTTOM_RIGHT_LINE_CHARACTER;
    }

    /**
     * Returns a bottom line with the given message
     */
    public static String bottomLine(String message, Object... arguments)
    {
        return bottomLine(logFormatter().maximumColumnWidth(), message, arguments);
    }

    /**
     * Returns a bottom line
     */
    public static String bottomLine()
    {
        return bottomLine(maximumWidth);
    }

    /**
     * Returns the string to use as a bullet
     */
    public static String bullet()
    {
        return isMac ? "○" : "o";
    }

    /**
     * Returns collection of values as a bulleted list
     */
    public static String bulleted(Collection<?> values)
    {
        return bulleted(1, values);
    }

    /**
     * Returns collection of values as a bulleted list using the given bullet
     */
    public static String bulleted(Collection<?> values, String bullet)
    {
        return bulleted(1, values, bullet);
    }

    /**
     * Returns collection of values as an indented bulleted list
     */
    public static String bulleted(int indent, Collection<?> values)
    {
        return bulleted(indent, values, bullet());
    }

    /**
     * Returns collection of values as an indented bulleted list using the given bullet
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
     * Returns the given text clipped at n characters with "[...]" appended if it is longer than n characters
     */
    public static String clip(String text, int n)
    {
        if (text.length() < n)
        {
            return text;
        }
        var suffix = " [...]";
        return leading(text, n - suffix.length()) + suffix;
    }

    /**
     * Returns a line
     */
    public static String line()
    {
        return line(maximumWidth);
    }

    /**
     * Returns a line of the given number of characters
     */
    public static String line(int length)
    {
        return repeat(length, HORIZONTAL_LINE_CHARACTER);
    }

    /**
     * Returns a left-justified line with the given message
     */
    public static String line(String message)
    {
        return line(4)
            + " "
            + message
            + " "
            + line(max(4, maximumWidth - 6 - message.length()));
    }

    /**
     * Returns the number of lines in the string
     */
    public static int lineCount(String string)
    {
        return occurrences(string, '\n') + 1;
    }

    public static int maximumWidth()
    {
        return maximumWidth;
    }

    /**
     * Sets the maximum width for ascii art
     *
     * @param maximumWidth The maximum width
     */
    public static void maximumWidth(int maximumWidth)
    {
        AsciiArt.maximumWidth = maximumWidth;
    }

    /**
     * @param times Number of times to repeat character
     * @param c Character to repeat
     * @return Repeated character string
     */
    public static String repeat(int times, char c)
    {
        ensure(times >= 0, "Times cannot be " + times);
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
        ensure(times >= 0);
        return string.repeat(times);
    }

    /**
     * Returns the given number of spaces
     */
    public static String spaces(int count)
    {
        return repeat(count, ' ');
    }

    public static String textBox(String message, char horizontal, char vertical)
    {
        return textBox(CLOSED, message, horizontal, vertical);
    }

    /**
     * Returns a box using the given horizontal and vertical line drawing characters that contains the given message
     */
    public static String textBox(TextBoxStyle style, String message, char horizontal, char vertical)
    {
        var builder = new StringBuilder();
        var width = min(maximumWidth, widestLine(message));
        builder.append(repeat(width + 6, horizontal));
        builder.append('\n');
        var lines = message.split("\n");
        for (var line : lines)
        {
            builder.append(vertical);
            builder.append("  ");
            builder.append(leftAlign(line, width, ' '));
            builder.append("  ");
            builder.append(vertical);
            builder.append('\n');
        }
        builder.append(repeat(width + 6, horizontal));
        return builder.toString();
    }

    public static String textBox(String message, Object... arguments)
    {
        return textBox(CLOSED, message, arguments);
    }

    /**
     * Returns an ASCII art box containing the given message
     */
    public static String textBox(TextBoxStyle style, String message, Object... arguments)
    {
        return textBox(style, format(message, arguments), HORIZONTAL_LINE_CHARACTER, VERTICAL_LINE_CHARACTER);
    }

    public static String textBox(String title, String message, Object... arguments)
    {
        return textBox(CLOSED, title, message, arguments);
    }

    /**
     * Returns an ASCII art box with the given title and message
     */
    public static String textBox(TextBoxStyle style, String title, String message, Object... arguments)
    {
        return textBox(style, maximumWidth(), title, message, arguments);
    }

    public static String textBox(int width, String title, String message, Object... arguments)
    {
        return textBox(CLOSED, width, title, message, arguments);
    }

    /**
     * Returns an ASCII art box with the given title and message
     */
    public static String textBox(TextBoxStyle style, int width, String title, String message, Object... arguments)
    {
        title = title(title);
        message = format(message, arguments);
        width = min(width, widestLine(title + "\n" + message) + 4);

        var builder = new StringBuilder();

        // Add the top of the box
        builder.append(TOP_LEFT_LINE_CHARACTER)
            .append(center(title, width - 2, HORIZONTAL_LINE_CHARACTER))
            .append(style == OPEN ? "" : TOP_RIGHT_LINE_CHARACTER)
            .append("\n");

        // For each line in the message,
        for (var line : message.split("\n"))
        {
            if (style == OPEN)
            {
                appendTextBoxLine(style, builder, line, MAX_VALUE);
            }
            else
            {
                do
                {
                    // add the line, wrapping if necessary
                    var next = line.substring(0, min(width - 4, line.length()));
                    appendTextBoxLine(style, builder, next, width - 4);
                    line = line.substring(next.length());
                }
                while (line.length() > width);

                if (!line.isEmpty())
                {
                    appendTextBoxLine(style, builder, line, width - 4);
                }
            }
        }

        // Add the bottom of the box
        builder.append(BOTTOM_LEFT_LINE_CHARACTER)
            .append(line(width - 2))
            .append(style == OPEN ? "" : BOTTOM_RIGHT_LINE_CHARACTER);

        return builder.toString();
    }

    /**
     * Returns a top line with the given title
     */
    public static String topLine(String title, Object... arguments)
    {
        return topLine(logFormatter().maximumColumnWidth(), title, arguments);
    }

    /**
     * Returns a top line with the given title
     */
    public static String topLine(int width, String title, Object... arguments)
    {
        title = title(title, arguments);
        return (width > 0 ? " \n" : "")
            + TOP_LEFT_LINE_CHARACTER + line(4) + title
            + line(max(4, width - 6 - title.length()))
            + TOP_RIGHT_LINE_CHARACTER;
    }

    /**
     * Returns the length of the widest line in potentially multi-line text
     */
    public static int widestLine(String text)
    {
        var width = 0;
        var lines = text.split("\n");
        for (var line : lines)
        {
            width = max(width, line.length());
        }
        return width;
    }

    public enum TextBoxStyle
    {
        OPEN,
        CLOSED
    }

    private static void appendTextBoxLine(TextBoxStyle style, StringBuilder builder, String text, int width)
    {
        builder.append(VERTICAL_LINE_CHARACTER).append(" ");
        if (style == CLOSED)
        {
            builder.append(leftAlign(text, width, ' '));
            builder.append(" ").append(VERTICAL_LINE_CHARACTER);
        }
        else
        {
            builder.append(text);
        }
        builder.append('\n');
    }

    @NotNull
    private static String title(String title, Object... arguments)
    {
        return TITLE_LEFT_CHARACTER + " " + format(title, arguments) + " " + TITLE_RIGHT_CHARACTER;
    }
}
