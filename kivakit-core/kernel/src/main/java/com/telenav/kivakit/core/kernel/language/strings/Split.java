package com.telenav.kivakit.core.kernel.language.strings;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.iteration.Iterables;
import com.telenav.kivakit.core.kernel.language.iteration.Next;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class Split
{
    /**
     * @return The sequence of strings resulting from splitting the given text using the given delimiter
     */
    public static Iterable<String> split(final String text, final char delimeter)
    {
        return Iterables.iterable(() -> new Next<>()
        {
            int pos;

            int next;

            @Override
            public String onNext()
            {
                // If we still have string left to search
                if (pos >= 0)
                {
                    // find the next delimiter location
                    next = text.indexOf(delimeter, pos);

                    // If there's no delimiter
                    if (next == -1)
                    {
                        // return the tail and we're done
                        final var tail = text.substring(pos);
                        pos = -1;
                        return tail;
                    }
                    else
                    {
                        // Return the value up to the delimiter and advance to the next
                        final var mid = text.substring(pos, next);
                        pos = next + 1;
                        return mid;
                    }
                }
                return null;
            }
        });
    }

    /**
     * @return The sequence of strings resulting from splitting the given text using the given delimiter
     */
    public static Iterable<String> split(final String value, final String delimeter)
    {
        return Iterables.iterable(() -> new Next<>()
        {
            int pos;

            int next;

            @Override
            public String onNext()
            {
                // If we still have string left to search
                if (pos >= 0)
                {
                    // find the next delimiter location
                    next = value.indexOf(delimeter, pos);

                    // If there's no delimiter
                    if (next == -1)
                    {
                        // return the tail and we're done
                        final var tail = value.substring(pos);
                        pos = -1;
                        return tail;
                    }
                    else
                    {
                        // Return the value up to the delimiter and advance to the next
                        final var mid = value.substring(pos, next);
                        pos = next + 1;
                        return mid;
                    }
                }
                return null;
            }
        });
    }

    /**
     * @return The two strings resulting from splitting the given text using the given separator
     */
    public static String[] splitOnFirst(final String text, final char separator)
    {
        final var index = text.indexOf(separator);
        if (index >= 0)
        {
            final var values = new String[2];
            values[0] = text.substring(0, index);
            values[1] = text.substring(index + 1);
            return values;
        }
        return null;
    }
}
