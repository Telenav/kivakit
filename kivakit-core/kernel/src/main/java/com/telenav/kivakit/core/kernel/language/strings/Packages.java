package com.telenav.kivakit.core.kernel.language.strings;

import com.telenav.kivakit.core.kernel.language.collections.list.StringList;

/**
 * @author jonathanl (shibo)
 */
public class Packages
{
    public static String toPath(final String packagePath)
    {
        final var path = new StringList();
        final var type = new StringList();
        for (final var at : Split.split(packagePath, '.'))
        {
            if (Strings.isLowerCase(at))
            {
                path.add(at);
            }
            else
            {
                type.add(at);
            }
        }
        return path.join("/") + "/" + type.join(".");
    }
}
