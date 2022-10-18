package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.resource.Resource;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
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
@CodeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTATION_COMPLETE,
             testing = UNTESTED)
public interface PackageTrait extends Repeater
{
    /**
     * Returns the package containing the given type
     */
    default Package packageFor(@NotNull Class<?> type)
    {
        return Package.packageFor(this, type);
    }

    /**
     * Gets the package with the given relative path
     *
     * @param relativePath The relative path
     * @return The given package relative to this class' package
     */
    default Package packageForRelativePath(@NotNull String relativePath)
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
    default PackagePath packagePathFor(@NotNull Class<?> type)
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
    default PackageResource packageResource(@NotNull Class<?> type,
                                            @NotNull String relativePath)
    {
        return PackageResource.packageResource(this, type, relativePath);
    }

    /**
     * Returns the resource at the given path relative to this component's class
     *
     * @param relativePath The path relative to this object's class
     */
    default PackageResource packageResource(@NotNull String relativePath)
    {
        return packageResource(getClass(), relativePath);
    }
}
