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
 *     <li>{@link #packageForThis()} - Returns the package containing this class</li>
 *     <li>{@link #packageFor(Class)} - Returns the {@link Package} containing the given class</li>
 *     <li>{@link #packageForRelativePath(String)} - Returns the package at the given path, relative to this package</li>
 * </ul>
 *
 * <p><b>Package Paths</b></p>
 *
 * <ul>
 *     <li>{@link #packagePathForThis()} - Returns the {@link PackagePath} to the package containing this class</li>
 *     <li>{@link #packagePathFor(Class)} - Returns the {@link PackagePath} of the package containing the given class</li>
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
     * Returns the package containing the given type
     */
    default Package packageFor(Class<?> type)
    {
        return Package.packageContaining(this, type);
    }

    /**
     * Gets the package with the given relative path
     *
     * @param relativePath The relative path
     * @return The given package relative to this class' package
     */
    default Package packageForRelativePath(String relativePath)
    {
        return parsePackage(this, getClass(), relativePath);
    }

    /**
     * Returns the package containing this class
     *
     * @return This package
     */
    default Package packageForThis()
    {
        return packageFor(getClass());
    }

    /**
     * Returns the path to the package containing the given type
     *
     * @param type The type
     * @return The package path
     */
    default PackagePath packagePathFor(Class<?> type)
    {
        return PackagePath.packagePath(type);
    }

    /**
     * Returns the path to the package containing this class
     */
    default PackagePath packagePathForThis()
    {
        return packagePathFor(getClass());
    }

    /**
     * Returns the resource at the given path relative to the given type
     *
     * @param type The type
     * @param relativePath The relative path
     */
    default PackageResource packageResource(Class<?> type, String relativePath)
    {
        return PackageResource.packageResource(this, type, relativePath);
    }

    /**
     * Returns the resource at the given path relative to this component's class
     *
     * @param relativePath The path relative to this object's class
     */
    default PackageResource packageResource(String relativePath)
    {
        return packageResource(getClass(), relativePath);
    }
}
