package com.telenav.kivakit.core.string;

import com.telenav.kivakit.annotations.code.CodeQuality;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.string.Join.join;
import static com.telenav.kivakit.core.string.Split.split;

/**
 * Utility methods to use for working with package names
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
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
     * Returns the prefix of the given type name
     */
    public static String packageType(String type)
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
