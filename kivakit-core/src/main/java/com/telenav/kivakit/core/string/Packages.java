package com.telenav.kivakit.core.string;

import com.telenav.kivakit.annotations.code.ApiQuality;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_STATIC_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.string.Join.join;
import static com.telenav.kivakit.core.string.Split.split;

/**
 * Utility methods to use for working with package names
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_STATIC_EXPANDABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class Packages
{
    /**
     * Returns the prefix of the given type name
     */
    public static String packagePrefix(String type)
    {
        var list = Split.split(type, "\\.")
                .stream()
                .filter(Strings::isLowerCase)
                .collect(Collectors.toList());

        return Join.join(list, ".");
    }

    /**
     * Converts the given package or qualified type to a resource path.
     *
     * @param packaged The package or type, such as <i>java.util</i> or <i>java.lang.String</i>
     * @return The path such as <i>java/util</i> or <i>java/lang/String</i>
     */
    public static String packageToPath(String packaged)
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
}
