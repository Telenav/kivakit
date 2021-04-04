package com.telenav.kivakit.core.kernel.language.strings;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class Indent
{
    /**
     * @return The given text indented by the given number of spaces
     */
    public static String by(final int spaces, final String text)
    {
        return with(AsciiArt.repeat(spaces, ' '), text);
    }

    /**
     * @return The given text indented with the given indentation string
     */
    public static String with(final String indentation, final String text)
    {
        return (text.startsWith("\n") ? "" : indentation) + text.replaceAll("\n", "\n" + indentation);
    }
}
