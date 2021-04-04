package com.telenav.kivakit.core.kernel.language.strings;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class PathStrings
{
    /**
     * @return The first element in the given path up to the separator or null if no separator is found
     */
    public static String head(final String path, final char separator)
    {
        if (path != null)
        {
            final var index = path.indexOf(separator);
            if (index >= 0)
            {
                return path.substring(0, index);
            }
            return path;
        }
        return null;
    }

    /**
     * @return The first element in the given path up to the separator or null if the separator is not found
     */
    public static String head(final String path, final String separator)
    {
        if (path != null)
        {
            final var index = path.indexOf(separator);
            if (index > 0 && index < path.length() - 1)
            {
                return path.substring(0, index);
            }
        }
        return null;
    }

    /**
     * @return The first element in the given path up to the separator or the path itself if the separator is not found
     */
    public static String optionalHead(final String path, final String separator)
    {
        return StringTo.string(head(path, separator), path);
    }

    /**
     * @return The first element in the given path up to the separator or the path itself if no separator is found
     */
    public static String optionalHead(final String path, final char separator)
    {
        return StringTo.string(head(path, separator), path);
    }

    /**
     * @return The last element in the given path up to the separator or the path itself if no separator is found
     */
    public static String optionalSuffix(final String path, final char separator)
    {
        if (path != null)
        {
            final var index = path.lastIndexOf(separator);
            return index == -1 ? path : path.substring(index + 1);
        }
        return null;
    }

    /**
     * @return All but the first element in the path or null if the value does not occur
     */
    public static String tail(final String string, final String value)
    {
        if (string != null)
        {
            final var index = string.indexOf(value);
            if (index >= 0 && index < string.length() - 1)
            {
                return string.substring(index + value.length());
            }
        }
        return null;
    }

    /**
     * @return All but the first element in the path or null if the value does not occur
     */
    public static String tail(final String text, final char separator)
    {
        if (text != null)
        {
            final var index = text.indexOf(separator);
            if (index >= 0)
            {
                return text.substring(index + 1);
            }
        }
        return null;
    }

    /**
     * @return The last element in the given path up to the separator or the path itself if no separator is found
     */
    public static String withoutOptionalSuffix(final String path, final char separator)
    {
        return StringTo.string(withoutSuffix(path, separator), path);
    }

    /**
     * @return The last element in the given path up to the separator or null if no separator is found
     */
    public static String withoutSuffix(final String path, final char separator)
    {
        if (path != null)
        {
            final var index = path.lastIndexOf(separator);
            if (index >= 0)
            {
                return path.substring(0, index);
            }
        }
        return null;
    }
}
