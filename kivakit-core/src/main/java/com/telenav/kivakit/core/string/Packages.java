package com.telenav.kivakit.core.string;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.telenav.kivakit.core.string.Join.join;
import static com.telenav.kivakit.core.string.Split.split;

/**
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class Packages
{
    /**
     * Converts the given package or qualified type to a resource path.
     *
     * @param packaged The package or type, such as <i>java.util</i> or <i>java.lang.String</i>
     * @return The path such as <i>java/util</i> or <i>java/lang/String</i>
     */
    public static String toPath(String packaged)
    {
        var path = new ArrayList<String>();
        var type = new ArrayList<String>();
        for (var at : split(packaged, "."))
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

        return join(path, "/") + "/" + join(type, ".");
    }

    public static String packagePrefix(String type)
    {
        var list = Split.split(type, "\\.")
                .stream()
                .filter(Strings::isLowerCase)
                .collect(Collectors.toList());

        return Join.join(list, ".");
    }
}
