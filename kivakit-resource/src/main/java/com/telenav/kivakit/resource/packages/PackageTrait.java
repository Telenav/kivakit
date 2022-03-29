package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.core.messaging.Repeater;

import static com.telenav.kivakit.resource.packages.Package.parsePackage;

/**
 * Trait containing methods for working with packages
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public interface PackageTrait extends Repeater
{
    default Package packageContaining(Class<?> type)
    {
        return Package.packageContaining(this, type);
    }

    default PackagePath packagePath(Class<?> type)
    {
        return PackagePath.packagePath(type);
    }

    /**
     * @return The resource at the given path relative to this component's class
     */
    default PackageResource packageResource(String path)
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
        return parsePackage(this, getClass(), path);
    }

    default Package thisPackage()
    {
        return packageContaining(getClass());
    }

    default PackagePath thisPackagePath()
    {
        return packagePath(getClass());
    }
}
