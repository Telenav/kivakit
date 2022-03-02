package com.telenav.kivakit.resource;

import com.telenav.kivakit.core.path.PackagePath;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.kivakit.resource.resources.packaged.PackageResource;

/**
 * Trait containing methods for working with packages
 *
 * @author jonathanl (shibo)
 */
public interface PackageTrait extends Repeater
{
    /**
     * @return The package for this object
     */
    default PackagePath packagePath()
    {
        return PackagePath.packagePath(getClass());
    }

    /**
     * @return The resource at the given path relative to this component's class
     */
    default Resource packageResource(String path)
    {
        return PackageResource.packageResource(this, getClass(), path);
    }

    /**
     * Gets the package with the given relative path
     *
     * @param path The relative path
     * @return The given package relative to this class' package
     */
    default Package relativePackage(String path)
    {
        return Package.packageFrom(this, getClass(), path);
    }
}
