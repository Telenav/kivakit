package com.telenav.kivakit.core.kernel.language.strings;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class Align
{
    /**
     * Centers the given text to a given length, with a given character used for padding
     */
    public static String center(final String text, final int length, final char c)
    {
        if (length > text.length())
        {
            final var left = (length - text.length()) / 2;
            final var right = length - text.length() - left;
            return AsciiArt.repeat(left, c) + text + AsciiArt.repeat(right, c);
        }
        else
        {
            return text;
        }
    }

    /**
     * Right pads the given text to the given length, with the given character
     */
    public static String left(final String string, final int length, final char c)
    {
        if (string != null && length > string.length())
        {
            return string + AsciiArt.repeat(length - string.length(), c);
        }
        else
        {
            return string;
        }
    }

    /**
     * Left pads the given text to the given length, with the given character
     */
    public static String right(final String text, final int length, final char c)
    {
        if (text != null && length > text.length())
        {
            return AsciiArt.repeat(length - text.length(), c) + text;
        }
        else
        {
            return text;
        }
    }
}
