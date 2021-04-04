package com.telenav.kivakit.core.kernel.language.strings;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;

import java.util.regex.Pattern;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class Wrap
{
    /**
     * @return The given text wrapped at the given width
     */
    public static String wrap(final String text, final int width)
    {
        final var wrapped = new StringBuilder();
        int position = 0;
        for (final var word : StringList.words(text))
        {
            if (position + word.length() > width)
            {
                wrapped.append("\n");
                position = 0;
            }
            wrapped.append(position == 0 ? "" : " ").append(word);
            position += word.length();
        }
        return wrapped.toString();
    }

    /**
     * @return The given string with all text between [wrap] and [end] markers wrapped.
     */
    public static String wrapRegion(final String text, final int width)
    {
        final var matcher = Pattern.compile("\\[wrap](.*?)\\[end]", Pattern.DOTALL).matcher(text);
        return matcher.replaceAll(result -> wrap(result.group(1), width));
    }
}
