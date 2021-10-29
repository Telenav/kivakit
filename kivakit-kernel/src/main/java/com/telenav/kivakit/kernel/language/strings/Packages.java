package com.telenav.kivakit.kernel.language.strings;

import com.telenav.kivakit.kernel.language.collections.list.StringList;

/**
 * @author jonathanl (shibo)
 */
public class Packages
{
    public static String toPath(String packagePath)
    {
        var path = new StringList();
        var type = new StringList();
        for (var at : Split.split(packagePath, '.'))
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
