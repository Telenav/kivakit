package com.telenav.kivakit.core.kernel.language.strings;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class Escape
{
    /**
     * @return The given text with single quotes escaped
     */
    public static String javaScriptString(final String text)
    {
        return Strings.replaceAll(text, "'", "\\'");
    }

    /**
     * @return The given string with single quotes escaped
     */
    public static String sqlString(final String text)
    {
        return Strings.replaceAll(text, "'", "''");
    }

    /**
     * @return The given xml string with escaping removed
     */
    public static String unescapeXml(final String xml)
    {
        final var u1 = Strings.replaceAll(xml, "&quot;", "\"");
        final var u2 = Strings.replaceAll(u1, "&amp;", "&");
        final var u3 = Strings.replaceAll(u2, "&apos;", "'");
        final var u4 = Strings.replaceAll(u3, "&lt;", "<");
        return Strings.replaceAll(u4, "&gt;", ">");
    }

    /**
     * @return The given XML text with special characters escaped
     */
    public static String xml(final String xml)
    {
        final var u1 = Strings.replaceAll(unescapeXml(xml), "\"", "&quot;");
        final var u2 = Strings.replaceAll(u1, "&", "&amp;");
        final var u3 = Strings.replaceAll(u2, "'", "&apos;");
        final var u4 = Strings.replaceAll(u3, "<", "&lt;");
        return Strings.replaceAll(u4, ">", "&gt;");
    }
}
