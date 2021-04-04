package com.telenav.kivakit.core.kernel.language.strings;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class Plural
{
    /**
     * @return The given text, pluralized if size is greater than one
     */
    public static String pluralize(final int size, final String text)
    {
        return size == 1 ? text : pluralize(text);
    }

    /**
     * <ul>
     *     <li>twenty -&gt; twenties</li>
     *     <li>edge -&gt; edges</li>
     *     <li>vertex -&gt; vertexes</li>
     * </ul>
     *
     * @return The given text as a plural
     */
    public static String pluralize(final String text)
    {
        if (text.endsWith("y"))
        {
            return withoutLastCharacter(text) + "ies";
        }
        if (text.endsWith("h"))
        {
            return text + "es";
        }
        if (text.endsWith("x"))
        {
            return withoutLastCharacter(text) + "es";
        }
        return text + "s";
    }

    /**
     * @return The given text without the last character (if any)
     */
    private static String withoutLastCharacter(final String text)
    {
        return !text.isEmpty() ? text.substring(0, text.length() - 1) : text;
    }
}
