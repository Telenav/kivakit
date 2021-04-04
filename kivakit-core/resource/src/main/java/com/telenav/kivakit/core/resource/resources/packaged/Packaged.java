package com.telenav.kivakit.core.resource.resources.packaged;

import com.telenav.kivakit.core.kernel.language.paths.PackagePath;

/**
 * @author jonathanl (shibo)
 */
public interface Packaged
{
    default Package _package()
    {
        return new Package(PackagePath.packagePath(getClass()));
    }
}
