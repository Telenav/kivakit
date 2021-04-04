package com.telenav.kivakit.core.kernel.language.strings;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class Normalize
{
    private static final Pattern SYMBOLS_AND_ACCENTS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    /**
     * Normalizes all non-English accented and symbol characters in a String.
     */
    public static String normalizeSymbolsAndAccents(String string)
    {
        if (Strings.isEmpty(string))
        {
            string = "";
        }
        // We remove the degree' and 'german sharp s' characters, which cause parsing problems on
        // traffic client
        string = Strings.replaceAll(string.replace('\u00B0', 'o'), "\u00DF", "ss");
        final var normalized = Normalizer.normalize(string, Normalizer.Form.NFD);
        return SYMBOLS_AND_ACCENTS.matcher(normalized).replaceAll("");
    }
}
