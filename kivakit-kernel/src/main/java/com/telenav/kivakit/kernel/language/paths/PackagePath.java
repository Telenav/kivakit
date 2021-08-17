////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.paths;

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.modules.ModuleResource;
import com.telenav.kivakit.kernel.language.modules.Modules;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Debug;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguagePath;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents the path to a Java package.
 *
 * <p>
 * This class contains numerous methods which down-cast the return value of the superclass to make use easier for
 * clients. Methods that are unique to this class mainly have to do with resources and modules:
 * </p>
 * <ul>
 *     <li>{@link #resources()} - A list of {@link ModuleResource}s in the package with this path</li>
 *     <li>{@link #resource(String)} - Any {@link ModuleResource} at the given relative path</li>
 *     <li>{@link #resourceStream(String)} - A stream for the resource at the given path relative to this package</li>
 *     <li>{@link #contains(ModuleResource)} - True if this package contains the given resource</li>
 *     <li>{@link #containsNested(ModuleResource)} - True if this package contains the given resource at any depth</li>
 *     <li>{@link #nestedResources()} - A list of nested module resources in and under this package</li>
 *     <li>{@link #nestedResources(Matcher)} - A list of nested module resources in and under this package that match the given matcher</li>
 *     <li>{@link #subPackages()} - The set of all package paths under this one</li>
 *     <li>#</li>
 * </ul>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #isPackagePath(String)} - True if the given string can be parsed as a package path</li>
 *     <li>{@link #parsePackagePath(String)} - The specified package path, separated by either '/' or '.'</li>
 *     <li>{@link #parsePackagePath(Class, String)} - The specified package path, relative to the given class and separated by either '/' or '.'</li>
 * </ul>
 *
 * <p><b>Factories</b></p>
 *
 * <ul>
 *     <li>{@link #packagePath(StringPath)} - The specified package path</li>
 *     <li>{@link #packagePath(Class)} - The package where the given class resides</li>
 *     <li>{@link #packagePath(Class, StringPath)} - The specified path relative to the given class</li>
 * </ul>
 *
 * <p><b>Examples</b></p>
 *
 * <pre>
 * PackagePath.of(MyClass.class)
 * PackagePath.parsePackagePath(MyClass.class, "resources/images")
 * PackagePath.parsePackagePath(MyClass.class, "resources.images")
 * PackagePath.parsePackagePath(getClass(), "resources/images")
 * PackagePath.parsePackagePath("com.telenav.kivakit/kernel")
 * PackagePath.parsePackagePath("com.telenav.kivakit.kernel")
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguagePath.class)
public final class PackagePath extends StringPath
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    public static final PackagePath TELENAV = parsePackagePath("com.telenav");

    public static boolean isPackagePath(final String path)
    {
        return path.matches("(?x) [a-z][a-z0-9_]* ( \\. [a-z][a-z0-9_]* ) *");
    }

    /**
     * @return A package path for the package that contains the given class
     */
    public static PackagePath packagePath(final Class<?> type, final StringPath path)
    {
        return new PackagePath(type, path);
    }

    /**
     * @return Package path for the given Java path object
     */
    public static PackagePath packagePath(final StringPath path)
    {
        return new PackagePath(null, path);
    }

    /**
     * @return A package path for the package that contains the given class
     */
    public static PackagePath packagePath(final Class<?> type)
    {
        return packagePath(type, parseStringPath(type.getName(), null, "\\.").withoutLast());
    }

    /**
     * @return The package path specified by the given path. The path may be separated by either '.' or '/'.
     */
    public static PackagePath parsePackagePath(final String path)
    {
        return packagePath(path(path));
    }

    /**
     * @return A package path relative to the package containing the given class
     */
    public static PackagePath parsePackagePath(final Class<?> type, final String relativePath)
    {
        final var parent = parseStringPath(type.getPackageName(), "\\.");
        final var child = parsePackagePath(relativePath);
        return new PackagePath(type, parent.withChild(child));
    }

    /** A class where the package is defined */
    private Class<?> packageType;

    protected PackagePath(final Class<?> packageType, final Path<String> path)
    {
        super(null, path.elements());
        this.packageType = packageType;
    }

    /**
     * Copy constructor
     */
    protected PackagePath(final PackagePath that)
    {
        super(that);
        packageType = that.packageType;
    }

    /**
     * @return True if the given resource is in this package
     */
    public boolean contains(final ModuleResource resource)
    {
        return resource.packagePath().equals(this);
    }

    /**
     * @return True if the given resource is in this package or any sub-package
     */
    public boolean containsNested(final ModuleResource resource)
    {
        return resource.packagePath().startsWith(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath first(final int n)
    {
        return (PackagePath) super.first(n);
    }

    @Override
    public StringPath last(final int n)
    {
        return super.last(n);
    }

    /**
     * @return A list of resources in and under this package
     */
    public List<ModuleResource> nestedResources()
    {
        final var resources = Modules.resources(this)
                .stream()
                .filter(resource -> parsePackagePath(resource.javaPath().toString()).startsWith(this))
                .collect(Collectors.toList());
        DEBUG.trace("Found nested resources:\n$", ObjectList.objectList(resources).join("\n"));
        return resources;
    }

    /**
     * The named resource in this package or null if it cannot be found
     */
    public List<ModuleResource> nestedResources(final Matcher<ModuleResource> matcher)
    {
        return Modules.nestedResources(this, matcher);
    }

    /**
     * @return A type within the package
     */
    public Class<?> packageType()
    {
        return packageType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath parent()
    {
        return (PackagePath) super.parent();
    }

    /**
     * @return The named resource in this package for the given path or null if it cannot be found. The relative path
     * must be separated by slashes, not dots because the filename may contain dots (like "a.txt").
     */
    public ModuleResource resource(final String relativePath)
    {
        final var path = parseStringPath(relativePath, "/", "/");
        Ensure.ensure(path.isRelative());
        return Modules.resource(withChild(path));
    }

    /**
     * @return An input stream to access the given resource
     */
    public InputStream resourceStream(final String path)
    {
        return packageType.getResourceAsStream(path);
    }

    /**
     * @return A list of the resources directly in the package specified by this path
     */
    public List<ModuleResource> resources()
    {
        return Modules.resources(this);
    }

    @Override
    public StringPath root()
    {
        return super.root();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String separator()
    {
        return ".";
    }

    /**
     * @return A list of resources in and under this package
     */
    public Set<PackagePath> subPackages()
    {
        final var packages = Modules.allNestedResources(this)
                .stream()
                .map(resource -> resource.packagePath().withPackageType(packageType))
                .collect(Collectors.toSet());
        DEBUG.trace("Found sub-packages:\n$", ObjectList.objectList(packages).join("\n"));
        return packages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath subpath(final int start, final int end)
    {
        return (PackagePath) super.subpath(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath transformed(final Function<String, String> consumer)
    {
        return (PackagePath) super.transformed(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withChild(final Path<String> that)
    {
        return (PackagePath) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withChild(final String path)
    {
        return (PackagePath) super.withChild(path(path));
    }

    public PackagePath withPackageType(Class<?> type)
    {
        var copy = (PackagePath) copy();
        copy.packageType = type;
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withParent(final String path)
    {
        return (PackagePath) super.withParent(PackagePath.parsePackagePath(path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withParent(final Path<String> that)
    {
        return (PackagePath) super.withParent(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withRoot(final String root)
    {
        return (PackagePath) super.withRoot(root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withSeparator(final String separator)
    {
        return super.withSeparator(separator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withoutFirst()
    {
        return (PackagePath) super.withoutFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withoutLast()
    {
        return (PackagePath) super.withoutLast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withoutOptionalPrefix(final Path<String> prefix)
    {
        return (PackagePath) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withoutOptionalSuffix(final Path<String> suffix)
    {
        return (PackagePath) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withoutPrefix(final Path<String> prefix)
    {
        return (PackagePath) super.withoutPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withoutRoot()
    {
        return super.withoutRoot();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withoutSuffix(final Path<String> suffix)
    {
        return (PackagePath) super.withoutSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Path<String> onCopy(final String root, final List<String> elements)
    {
        return new PackagePath(packageType, new StringPath(root, elements));
    }

    @NotNull
    private static StringPath path(final String path)
    {
        if (path.contains("/"))
        {
            return parseStringPath(path, "/");
        }
        return parseStringPath(path, "\\.");
    }
}
