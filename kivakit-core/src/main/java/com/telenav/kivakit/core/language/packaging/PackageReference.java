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

package com.telenav.kivakit.core.language.packaging;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramPath;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.Path;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.messaging.Listener.nullListener;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.string.Strip.stripEnding;
import static com.telenav.kivakit.core.string.Strip.stripLeading;

/**
 * Represents the path to a Java package. The PackagePath object in kivakit-resource differs from this class.
 *
 * <p>
 * This class contains numerous methods which down-cast the return value of the superclass to make use easier for
 * clients. Methods that are unique to this class mainly have to do with resources and modules:
 * </p>
 *
 * <p><b>Contains</b></p>
 *
 * <ul>
 *     <li>{@link #contains(PackageReference)} - True if this package contains the given resource</li>
 *     <li>{@link #containsNested(PackageReference)} - True if this package contains the given resource at any depth</li>
 * </ul>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #isPackageReference(String)} - True if the given string can be parsed as a package path</li>
 *     <li>{@link #parsePackageReference(Listener listener, String)} - The specified package path, separated by either '/' or '.'</li>
 *     <li>{@link #parsePackageReference(Listener listener, Class, String)} - The specified package path, relative to the given class and separated by either '/' or '.'</li>
 * </ul>
 *
 * <p><b>Factories</b></p>
 *
 * <ul>
 *     <li>{@link #packageReference(StringPath)} - The specified package path</li>
 *     <li>{@link #packageReference(Class)} - The package where the given class resides</li>
 *     <li>{@link #packageReference(Class, StringPath)} - The specified path relative to the given class</li>
 * </ul>
 *
 * <p><b>Examples</b></p>
 *
 * <pre>
 * packageReference(MyClass.class)
 * parsePackageReference(MyClass.class, "resources/images")
 * parsePackageReference(MyClass.class, "resources.images")
 * parsePackageReference(getClass(), "resources/images")
 * parsePackageReference("com.telenav.kivakit/core")
 * parsePackageReference("com.telenav.kivakit.core")
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused" })
@UmlClassDiagram(diagram = DiagramPath.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public final class PackageReference extends StringPath
{
    /** Reference to the com.telenav package */
    public static final PackageReference TELENAV = parsePackageReference(nullListener(), "com.telenav");

    /**
     * Returns true if the given path is a package reference
     */
    public static boolean isPackageReference(String path)
    {
        return path.matches("(?x) [a-z][a-z0-9_]* ( \\. [a-z][a-z0-9_]* ) *");
    }

    /**
     * Returns a package path for the package that contains the given class
     */
    public static PackageReference packageReference(Class<?> type, StringPath path)
    {
        return new PackageReference(type, path);
    }

    /**
     * Returns package path for the given Java path object
     */
    public static PackageReference packageReference(StringPath path)
    {
        return new PackageReference(null, path);
    }

    /**
     * Returns a package path for the package that contains the given class
     */
    public static PackageReference packageReference(Class<?> type)
    {
        return packageReference(type, parseStringPath(throwingListener(), type.getName(), null, "\\.")
                .withoutLast());
    }

    /**
     * Returns the package path specified by the given path. The path may be separated by either '.' or '/'.
     */
    public static PackageReference parsePackageReference(Listener listener, String path)
    {
        return packageReference(path(listener, path));
    }

    /**
     * Returns a package path relative to the package containing the given class
     */
    public static PackageReference parsePackageReference(Listener listener, Class<?> type, String relativePath)
    {
        var parent = parseStringPath(listener, type.getPackageName(), "\\.");
        var child = parsePackageReference(listener, relativePath);
        return new PackageReference(type, parent.withChild(child));
    }

    /** A class where the package is defined */
    private Class<?> packageType;

    private PackageReference(Class<?> packageType, Path<String> path)
    {
        super(null, path.elements());
        this.packageType = packageType;
    }

    /**
     * Copy constructor
     */
    private PackageReference(PackageReference that)
    {
        super(that);
        packageType = that.packageType;
    }

    /**
     * Returns this package reference, slash-separated
     */
    public String asSlashSeparated()
    {
        return join("/");
    }

    /**
     * Returns true if the given resource is in this package
     */
    public boolean contains(PackageReference that)
    {
        return that.equals(this);
    }

    /**
     * Returns true if the given resource is in this package or any sub-package
     */
    public boolean containsNested(PackageReference that)
    {
        return that.startsWith(this);
    }

    /**
     * Returns a list of sub packages under this package from the directories in classpath
     */
    public Set<PackageReference> filesystemSubPackages(Listener listener)
    {
        // Get the code source for the package type class,
        var packages = new HashSet<PackageReference>();
        var source = hasPackageType() ? packageType().getProtectionDomain().getCodeSource() : null;
        if (source != null)
        {
            try
            {
                // and if the location URL ends in "/",
                URL location = source.getLocation();
                if (location != null && location.toString().endsWith("/"))
                {
                    var filepath = join("/") + "/";

                    var directory = stringPath(location.toURI()).withChild(filepath).asJavaPath();

                    if (Files.exists(directory))
                    {
                        try (var list = Files.list(directory))
                        {
                            list.filter(Files::isDirectory)
                                    .forEach(path -> packages.add(withChild(path.getFileName().toString())));
                        }
                    }
                }
            }
            catch (Exception ignored)
            {
                listener.warning("Exception thrown while searching directory sub packages from $", this);
            }
        }
        return packages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackageReference first(int n)
    {
        return (PackageReference) super.first(n);
    }

    /**
     * Returns true if this reference is relative to some class in the referenced package (the "package type")
     */
    public boolean hasPackageType()
    {
        return packageType != null;
    }

    /**
     * Returns a list of sub packages under this package from the jars in classpath
     */
    public Set<PackageReference> jarSubPackages(Listener listener)
    {
        // Get the code source for the package type class,
        var packages = new HashSet<PackageReference>();
        var source = hasPackageType() ? packageType().getProtectionDomain().getCodeSource() : null;
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
                            var suffix = stripLeading(name, filepath);
                            suffix = stripEnding(suffix, "/");

                            // and if we have only a folder name left,
                            if (!suffix.contains("/") && !suffix.isEmpty())
                            {
                                // then the entry is in the package, so add it to the resources list
                                packages.add(withChild(suffix));
                            }
                        }
                    }
                }
            }
            catch (Exception ignored)
            {
                listener.warning("Exception thrown while searching jar sub packages from $", this);
            }
        }
        return packages;
    }

    @Override
    public StringPath last(int n)
    {
        return super.last(n);
    }

    /**
     * Returns a type within the package
     */
    public Class<?> packageType()
    {
        return packageType == null
                ? getClass()
                : packageType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackageReference parent()
    {
        return (PackageReference) super.parent();
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
     * {@inheritDoc}
     */
    @Override
    public PackageReference subpath(int start, int end)
    {
        return (PackageReference) super.subpath(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackageReference transformed(Function<String, String> consumer)
    {
        return (PackageReference) super.transformed(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackageReference withChild(Path<String> that)
    {
        return (PackageReference) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackageReference withChild(String path)
    {
        return (PackageReference) super.withChild(path(throwingListener(), path));
    }

    public PackageReference withPackageType(Class<?> type)
    {
        var copy = (PackageReference) copy();
        copy.packageType = type;
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackageReference withParent(String path)
    {
        return (PackageReference) super.withParent(parsePackageReference(throwingListener(), path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackageReference withParent(Path<String> that)
    {
        return (PackageReference) super.withParent(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackageReference withRoot(String root)
    {
        return (PackageReference) super.withRoot(root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringPath withSeparator(String separator)
    {
        return super.withSeparator(separator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackageReference withoutFirst()
    {
        return (PackageReference) super.withoutFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackageReference withoutLast()
    {
        return (PackageReference) super.withoutLast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackageReference withoutOptionalPrefix(Path<String> prefix)
    {
        return (PackageReference) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackageReference withoutOptionalSuffix(Path<String> suffix)
    {
        return (PackageReference) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackageReference withoutPrefix(Path<String> prefix)
    {
        return (PackageReference) super.withoutPrefix(prefix);
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
    public PackageReference withoutSuffix(Path<String> suffix)
    {
        return (PackageReference) super.withoutSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Path<String> onCopy(String root, List<String> elements)
    {
        return new PackageReference(packageType, stringPath(root, elements));
    }

    @NotNull
    private static StringPath path(Listener listener, String path)
    {
        if (path == null)
        {
            return emptyStringPath();
        }
        if (path.contains("/"))
        {
            return parseStringPath(listener, path, "/");
        }
        return parseStringPath(listener, path, "\\.");
    }
}
