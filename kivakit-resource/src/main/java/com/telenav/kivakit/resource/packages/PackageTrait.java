package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.resource.Resource;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.resource.packages.Package.parsePackage;

/**
 * A trait containing methods for working with packages. Classes implementing this interface are provided with easy
 * access to packages and resources relative to the class' package:
 * </p>
 *
 * <p><b>Package Resources</b></p>
 *
 * <ul>
 *     <li>{@link #packageResource(String)} - Returns the {@link Resource} at the given path, relative to the package containing this class</li>
 *     <li>{@link #packageResource(Class, String)} - Returns the {@link Resource} at the given path, relative to the package containing the given class</li>
 * </ul>
 *
 * <p><b>Packages</b></p>
 *
 * <ul>
 *     <li>{@link #thisPackage()} - Returns the package containing this class</li>
 *     <li>{@link #packageContaining(Class)} - Returns the {@link Package} containing the given class</li>
 *     <li>{@link #relativePackage(String)} - Returns the package at the given path, relative to this package</li>
 * </ul>
 *
 * <p><b>Package Paths</b></p>
 *
 * <ul>
 *     <li>{@link #thisPackagePath()} - Returns the {@link PackagePath} to the package containing this class</li>
 *     <li>{@link #packagePath(Class)} - Returns the {@link PackagePath} of the package containing the given class</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_DEFAULT_EXTENSIBLE,
            documentation = FULLY_DOCUMENTED,
            testing = UNTESTED)
public interface PackageTrait extends Repeater
{
    /**
     * @return The package containing the given type
     */
    default Package packageContaining(Class<?> type)
    {
        return Package.packageContaining(this, type);
    }

    /**
     * @return The path to the package containing the given type
     */
    default PackagePath packagePath(Class<?> type)
    {
        return PackagePath.packagePath(type);
    }

    /**
     * @return The resource at the given path relative to this component's class
     */
    default PackageResource packageResource(String path)
    {
        return packageResource(getClass(), path);
    }

    /**
     * @return The resource at the given path relative to this component's class
     */
    default PackageResource packageResource(Class<?> type, String path)
    {
        return PackageResource.packageResource(this, type, path);
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

    /**
     * @return The package containing this class
     */
    default Package thisPackage()
    {
        return packageContaining(getClass());
    }

    /**
     * @return The path to the package containing this class
     */
    default PackagePath thisPackagePath()
    {
        return packagePath(getClass());
    }
}
