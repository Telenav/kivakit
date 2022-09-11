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

package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.language.module.PackageReference;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.Path;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResource;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourcePath;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.nio.file.Files;
import java.security.CodeSource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.telenav.kivakit.core.language.module.PackageReference.packageReference;
import static com.telenav.kivakit.resource.packages.Package.packageForPath;

/**
 * A {@link ResourcePath} to a {@link Package}.
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #isPackagePath(String)} - True if the given string can be parsed as a package path</li>
 *     <li>{@link #parsePackagePath(Listener listener, String)} - The specified package path, separated by either '/' or '.'</li>
 *     <li>{@link #parsePackagePath(Listener listener, Class, String)} - The specified package path, relative to the given class and separated by either '/' or '.'</li>
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
 * PackagePath.parsePackagePath("com.telenav.kivakit/core")
 * PackagePath.parsePackagePath("com.telenav.kivakit.core")
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "DuplicatedCode" })
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlClassDiagram(diagram = DiagramResourcePath.class)
public final class PackagePath extends ResourcePath
{
    public static final PackagePath TELENAV = parsePackagePath(Listener.emptyListener(), "com.telenav");

    public static boolean isPackagePath(String path)
    {
        return path.matches("(?x) [a-z][a-z0-9_]* ( \\. [a-z][a-z0-9_]* ) *");
    }

    /**
     * @return A package path for the package that contains the given class
     */
    public static PackagePath packagePath(Class<?> type, StringPath path)
    {
        return new PackagePath(type, path);
    }

    /**
     * @return A package path for the package that contains the given class
     */
    public static PackagePath packagePath(PackageReference reference)
    {
        return new PackagePath(reference.packageType(), reference);
    }

    /**
     * @return Package path for the given Java path object
     */
    public static PackagePath packagePath(StringPath path)
    {
        return new PackagePath(null, path);
    }

    /**
     * @return A package path for the package that contains the given class
     */
    public static PackagePath packagePath(Class<?> type)
    {
        return packagePath(type, parseStringPath(Listener.emptyListener(), type.getName(), null, "\\.").withoutLast());
    }

    /**
     * @return The package path specified by the given path. The path may be separated by either '.' or '/'.
     */
    public static PackagePath parsePackagePath(Listener listener, String path)
    {
        return packagePath(path(path));
    }

    /**
     * @return A package path relative to the package containing the given class
     */
    public static PackagePath parsePackagePath(Listener listener, Class<?> type, String relativePath)
    {
        var parent = parseStringPath(listener, type.getPackageName(), "\\.");
        var child = parsePackagePath(listener, relativePath);
        return new PackagePath(type, parent.withChild(child));
    }

    /** A class where the package is defined */
    private Class<?> packageType;

    private PackagePath(Class<?> packageType, Path<String> path)
    {
        super(StringList.stringList(), path.elements());
        this.packageType = packageType;
    }

    /**
     * Copy constructor
     */
    private PackagePath(PackagePath that)
    {
        super(that);
        packageType = that.packageType;
    }

    public Package asPackage(Listener listener)
    {
        return packageForPath(listener, this);
    }

    public PackageReference asPackageReference()
    {
        return packageReference(packageType, this);
    }

    /**
     * @return A list of sub packages under this package from the directories in classpath
     */
    public Set<PackagePath> directorySubPackages()
    {
        // Get the code source for the package type class,
        var packages = new HashSet<PackagePath>();
        CodeSource source = packageType().getProtectionDomain().getCodeSource();
        if (source != null)
        {
            try
            {
                // and if the location URL ends in "/",
                URL location = source.getLocation();
                if (location != null && location.toString().endsWith("/"))
                {
                    var filepath = join("/") + "/";

                    var directory = StringPath.stringPath(location.toURI()).withChild(filepath).asJavaPath();

                    if (Files.exists(directory))
                    {
                        Files.list(directory)
                                .filter(Files::isDirectory)
                                .forEach(path -> packages.add(withChild(path.getFileName().toString())));
                    }
                }
            }
            catch (Exception ignored)
            {
                Ensure.warning("Exception thrown while searching directory sub packages from $", this);
            }
        }
        return packages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath first(int n)
    {
        return (PackagePath) super.first(n);
    }

    /**
     * @return Returns true if this path has an associated type
     */
    public boolean hasPackageType()
    {
        return packageType != null;
    }

    /**
     * @return A list of sub packages under this package from the jars in classpath
     */
    public Set<PackagePath> jarSubPackages()
    {
        // Get the code source for the package type class,
        var packages = new HashSet<PackagePath>();
        CodeSource source = packageType().getProtectionDomain().getCodeSource();
        if (source != null)
        {
            try
            {
                // and if the location URL ends in ".jar",
                URL location = source.getLocation();
                if (location != null && location.toString().endsWith(".jar"))
                {
                    // then open the jar as a zip input stream,
                    var urlConnection = location.openConnection();
                    ZipInputStream zip = new ZipInputStream(urlConnection.getInputStream());

                    // form a file path from the package path,
                    var filepath = join("/") + "/";

                    // and loop,
                    while (true)
                    {
                        // reading the next entry,
                        ZipEntry e = zip.getNextEntry();

                        // until we are out.
                        if (e == null)
                        {
                            break;
                        }

                        // Get the entry's name
                        String name = e.getName();

                        // and if it is a folder, and it starts with the file path for the package,
                        if (name.endsWith("/") && name.startsWith(filepath))
                        {
                            // then strip off the leading filepath,
                            var suffix = Strip.leading(name, filepath);
                            suffix = Strip.ending(suffix, "/");

                            // and if we have only a folder name left,
                            if (!suffix.contains("/") && !suffix.isEmpty())
                            {
                                // then the entry is in the package, so add it to the list
                                packages.add(withChild(suffix));
                            }
                        }
                    }
                }
            }
            catch (Exception ignored)
            {
                Ensure.warning("Exception thrown while searching jar sub packages from $", this);
            }
        }
        return packages;
    }

    @Override
    public PackagePath last(int n)
    {
        return (PackagePath) super.last(n);
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

    @Override
    public PackagePath root()
    {
        return (PackagePath) super.root();
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
     * {@inheritDoc}
     */
    @Override
    public PackagePath subpath(int start, int end)
    {
        return (PackagePath) super.subpath(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath transformed(Function<String, String> consumer)
    {
        return (PackagePath) super.transformed(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withChild(Path<String> that)
    {
        return (PackagePath) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withChild(String path)
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
    public PackagePath withParent(String path)
    {
        return (PackagePath) super.withParent(PackagePath.parsePackagePath(Listener.throwingListener(), path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withParent(Path<String> that)
    {
        return (PackagePath) super.withParent(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withRoot(String root)
    {
        return (PackagePath) super.withRoot(root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withSeparator(String separator)
    {
        return (PackagePath) super.withSeparator(separator);
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
    public PackagePath withoutOptionalPrefix(Path<String> prefix)
    {
        return (PackagePath) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withoutOptionalSuffix(Path<String> suffix)
    {
        return (PackagePath) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withoutPrefix(Path<String> prefix)
    {
        return (PackagePath) super.withoutPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withoutRoot()
    {
        return (PackagePath) super.withoutRoot();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withoutSuffix(Path<String> suffix)
    {
        return (PackagePath) super.withoutSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PackagePath onCopy(String root, List<String> elements)
    {
        return new PackagePath(packageType, stringPath(root, elements));
    }

    @NotNull
    private static StringPath path(String path)
    {
        if (path.contains("/"))
        {
            return parseStringPath(Listener.throwingListener(), path, "/");
        }
        return parseStringPath(Listener.throwingListener(), path, "\\.");
    }
}
