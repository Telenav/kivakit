package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.resource.Resource;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.path.StringPath.parseStringPath;
import static com.telenav.kivakit.resource.packages.Package.packageForPath;
import static com.telenav.kivakit.resource.packages.PackagePath.parsePackagePath;

/**
 * A trait containing methods for working with packages. Classes implementing this interface are provided with easy
 * access to packages and resources relative to the class' package:
 * </p>
 *
 * <p><b>Package Resources</b></p>
 *
 * <ul>
 *     <li>{@link #packageResource(String)} - Returns the {@link Resource} at the given path, relative to the package containing this class</li>
 * </ul>
 *
 * <p><b>Packages</b></p>
 *
 * <ul>
 *     <li>{@link #packageForThis()} - Returns the package containing this class</li>
 *     <li>{@link #packageFor(Class)} - Returns the {@link Package} containing the given class</li>
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
        return packageForPath(this, parsePackagePath(this, type.getPackageName()));
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
     * Returns the resource at the given path relative to the given type
     *
     * @param relativePath The relative path
     */
    default PackageResource packageResource(@NotNull String relativePath)
    {
        var path = parseStringPath(this, relativePath, "\\.");
        return path == null ? null : PackageResource.packageResource(this, path);
    }
}
