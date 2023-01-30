package com.telenav.kivakit.core.string;

import static com.telenav.kivakit.core.string.Strip.stripLeading;
import static com.telenav.kivakit.core.string.Strip.stripTrailing;

/**
 * Adds and removes brackets around text
 *
 * @author Jonathan Locke
 */
public class Brackets
{
    /**
     * Adds brackets around the given text, if there aren't already brackets
     *
     * @param text The text
     * @return The text in brackets, if not already
     */
    public static String bracket(String text)
    {
        if (!text.trim().startsWith("{") || !text.trim().endsWith("}"))
        {
            return "{\n" + text + "\n}";
        }
        return text;
    }

    /**
     * Removes (one layer of) brackets around text
     *
     * @param text The text
     * @return The text without brackets
     */
    public static String unbracket(String text)
    {
        return stripTrailing(stripLeading(text, "{"), "}");
    }
}
