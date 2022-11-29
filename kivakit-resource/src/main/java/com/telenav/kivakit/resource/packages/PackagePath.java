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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.language.packaging.PackageReference;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.Path;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResource;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourcePath;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.language.packaging.PackageReference.packageReference;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.resource.packages.Package.packageForPath;

/**
 * A {@link ResourcePath} to a {@link Package}.
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #isPackagePath(String)} - True if the given string can be parsed as a package path</li>
 *     <li>{@link #parsePackagePath(Listener listener, String)} - The specified package path, separated by either '/' or '.'</li>
 * </ul>
 *
 * <p><b>Factories</b></p>
 *
 * <ul>
 *     <li>{@link #packagePath(StringPath)} - The specified package path</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #parent()}</li>
 *     <li>{@link #separator()}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asPackage(Listener)}</li>A
 *     <li>{@link #asPackageReference()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #withChild(Path)}</li>
 *     <li>{@link #withChild(String)}</li>
 *     <li>{@link #withParent(Path)}</li>
 *     <li>{@link #withParent(String)}</li>
 *     <li>{@link #withSeparator(String)}</li>
 *     <li>{@link #withoutFirst()}</li>
 *     <li>{@link #withoutLast()}</li>
 *     <li>{@link #withoutOptionalPrefix(Path)}</li>
 *     <li>{@link #withoutOptionalSuffix(Path)}</li>
 *     <li>{@link #withoutPrefix(Path)}</li>
 *     <li>{@link #withoutSuffix(Path)}</li>
 *     <li>{@link #withoutRoot()}</li>
 * </ul>
 *
 * <p><b>Examples</b></p>
 *
 * <pre>
 * packagePath(MyClass.class)
 * parsePackagePath(MyClass.class, "resources/images")
 * parsePackagePath(MyClass.class, "resources.images")
 * parsePackagePath(getClass(), "resources/images")
 * parsePackagePath("com.telenav.kivakit/core")
 * parsePackagePath("com.telenav.kivakit.core")
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "DuplicatedCode", "SpellCheckingInspection" })
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlClassDiagram(diagram = DiagramResourcePath.class)
@CodeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public final class PackagePath extends ResourcePath
{
    /** The com.telenav package */
    public static final PackagePath TELENAV = parsePackagePath(throwingListener(), "com.telenav");

    /**
     * Returns true if the given path is a valid dot-separated package path
     *
     * @param path The path
     * @return True if the path is a package path
     */
    public static boolean isPackagePath(@NotNull String path)
    {
        return path.matches("(?x) [a-z][a-z0-9_]* ( \\. [a-z][a-z0-9_]* ) *");
    }

    /**
     * Returns package path for the given Java path object
     *
     * @param path A path of strings. Note that {@link PackageReference} is a {@link StringPath}, so it can be passed
     * for this parameter.
     * @return The path as a {@link PackagePath}
     */
    public static PackagePath packagePath(@NotNull StringPath path)
    {
        return new PackagePath(path);
    }

    /**
     * Returns the package path specified by the given path. The path may be separated by either '.' or '/'.
     */
    public static PackagePath parsePackagePath(@NotNull Listener listener, @NotNull String path)
    {
        return packagePath(path(listener, path));
    }

    private PackagePath(@NotNull Path<String> path)
    {
        super(stringList(), path.elements());
    }

    /**
     * Copy constructor
     */
    private PackagePath(@NotNull PackagePath that)
    {
        super(that);
    }

    /**
     * Returns the package for this path
     *
     * @param listener The listener to call with any errors
     */
    public Package asPackage(@NotNull Listener listener)
    {
        return packageForPath(listener, this);
    }

    /**
     * Returns this package path as a package reference
     */
    public PackageReference asPackageReference()
    {
        return packageReference(this);
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
     * {@inheritDoc}
     */
    @Override
    public PackagePath last(int n)
    {
        return (PackagePath) super.last(n);
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
     * {@inheritDoc}
     */
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
    public PackagePath transformed(@NotNull Function<String, String> consumer)
    {
        return (PackagePath) super.transformed(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withChild(@NotNull Path<String> that)
    {
        return (PackagePath) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withChild(@NotNull String path)
    {
        return (PackagePath) super.withChild(path(throwingListener(), path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withParent(@NotNull String path)
    {
        return (PackagePath) super.withParent(parsePackagePath(throwingListener(), path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withParent(@NotNull Path<String> that)
    {
        return (PackagePath) super.withParent(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withRoot(@NotNull String root)
    {
        return (PackagePath) super.withRoot(root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withSeparator(@NotNull String separator)
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
    public PackagePath withoutOptionalPrefix(@NotNull Path<String> prefix)
    {
        return (PackagePath) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withoutOptionalSuffix(@NotNull Path<String> suffix)
    {
        return (PackagePath) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PackagePath withoutPrefix(@NotNull Path<String> prefix)
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
    public PackagePath withoutSuffix(@NotNull Path<String> suffix)
    {
        return (PackagePath) super.withoutSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PackagePath onCopy(String root, @NotNull List<String> elements)
    {
        return new PackagePath(stringPath(root, elements));
    }

    @NotNull
    private static StringPath path(Listener listener, @NotNull String path)
    {
        if (path.contains("/"))
        {
            path = path.replaceAll("/", ".");
        }
        return parseStringPath(listener, path, "\\.");
    }
}
