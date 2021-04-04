package com.telenav.kivakit.core.kernel.language.paths;

/**
 * @author jonathanl (shibo)
 */
public interface PackagePathed
{
    default PackagePath packagePath()
    {
        return PackagePath.packagePath(getClass());
    }
}
