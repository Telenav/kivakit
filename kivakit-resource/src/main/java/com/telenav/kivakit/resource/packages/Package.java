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
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.packaging.PackageReference;
import com.telenav.kivakit.core.locale.Locale;
import com.telenav.kivakit.core.locale.LocaleLanguage;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.properties.PropertyMap;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.resource.ResourceList;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.ResourcePathed;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.language.packaging.PackageReference.packageReference;
import static com.telenav.kivakit.properties.PropertyMap.loadLocalizedPropertyMap;
import static com.telenav.kivakit.resource.ResourceList.resourceList;
import static com.telenav.kivakit.resource.packages.Classpath.classpath;
import static com.telenav.kivakit.resource.packages.PackagePath.packagePath;
import static com.telenav.kivakit.resource.packages.PackagePath.parsePackagePath;
import static com.telenav.kivakit.resource.packages.PackageResource.packageResource;
import static java.util.Objects.hash;

/**
 * An abstraction for locating and copying {@link Resource}s in Java packages.
 *
 * <p>
 * A package object can be constructed with {@link #packageForPath(Listener, PackagePath)}. It implements the
 * {@link ResourceFolder} interface because it contains resources, just as {@link Folder} does. This means, of course,
 * that methods that accept {@link ResourceFolder} can accept either {@link Folder}s or {@link Package}s.
 * </p>
 *
 * <p><b>Hierarchy</b></p>
 *
 * <p>
 * The parent package can be retrieved with {@link #parent()} and the path with {@link #path()}. Sub-packages can be
 * accessed with {@link #child(String)} or as a resource folder with {@link #folder(String)}.
 * </p>
 *
 * <p><b>Resources</b></p>
 *
 * <p>
 * Resources in a package can be obtained with {@link ResourceFolder#resources()} and
 * {@link ResourceFolder#resources(Matcher)}. A specific resource can be located with
 * {@link ResourceFolder#resource(String)}. The resources in a package can be copied to a {@link Folder} with
 * {@link ResourceFolder#safeCopyTo(ResourceFolder, CopyMode, ProgressReporter)}.
 * </p>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #exists()}</li>
 *     <li>{@link #folders()}</li>
 *     <li>{@link #isMaterialized()}</li>
 *     <li>{@link #parent()}</li>
 *     <li>{@link #path()}</li>
 *     <li>{@link #reference()}</li>
 *     <li>{@link #uri()}</li>
 * </ul>
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #child(String)}</li>
 *     <li>{@link #folder(String)}</li>
 *     <li>{@link #localizedProperties(Listener, Locale, LocaleLanguage)}</li>
 *     <li>{@link #relativeTo(ResourceFolder)}</li>
 *     <li>{@link #resource(FileName)}</li>
 *     <li>{@link #resource(ResourcePathed)}</li>
 *     <li>{@link #resource(String)}</li>
 *     <li>{@link #resourceFolderIdentifier()}</li>
 *     <li>{@link #resources()}</li>
 *     <li>{@link #resources(Matcher)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see PackageReference
 * @see PackageResource
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramResourceType.class)
@CodeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Package extends BaseRepeater implements ResourceFolder<Package>
{
    /**
     * Returns the package for the given path
     *
     * @param listener The listener to call with any problems
     * @param packagePath The path to this package
     */
    public static Package packageForPath(@NotNull Listener listener,
                                         @NotNull PackagePath packagePath)
    {
        return new Package(listener, packagePath);
    }


    /**
     * Returns the package for the given type and relative path
     *
     * @param listener The listener to call with any problems
     * @param packageType The type
     * @param path The path relative to the package of the type
     * @return The package
     */
    public static Package parsePackage(@NotNull Listener listener,
                                       @NotNull Class<?> packageType,
                                       @NotNull String path)
    {
        return packageForPath(listener, parsePackagePath(listener, packageType.getPackageName() + "." + path));
    }

    /** The path to this package */
    private final PackagePath packagePath;

    private Package(@NotNull Listener listener,
                    @NotNull PackagePath packagePath)
    {
        listener.listenTo(this);
        this.packagePath = packagePath;
    }

    /**
     * Returns the named sub-package under this one
     */
    public Package child(@NotNull String name)
    {
        return new Package(this, packagePath.withChild(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete()
    {
        return unsupported();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Package that)
        {
            return path().equals(that.path());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists()
    {
        return unsupported();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Package folder(@NotNull String path)
    {
        return child(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectList<Package> folders()
    {
        var children = new ObjectList<Package>();
        for (var folder : classpath().resourceFolders(this))
        {
            children.add(packageForPath(this, packagePath(folder.path())));
        }
        return children;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return hash(path());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMaterialized()
    {
        return false;
    }

    /**
     * Returns a localized property map for the given locale
     */
    public PropertyMap localizedProperties(@NotNull Listener listener,
                                           @NotNull Locale locale,
                                           @NotNull LocaleLanguage languageName)
    {
        return loadLocalizedPropertyMap(listener, path(), locale, languageName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceFolder<?> newFolder(@NotNull ResourcePath relativePath)
    {
        return packageForPath(this, packagePath(relativePath));
    }

    /**
     * Returns the parent package of this package, or null if there is none
     */
    @Override
    public Package parent()
    {
        var parent = packagePath.withoutLast();
        return parent == null ? null : new Package(this, parent);
    }

    /**
     * Returns the path to this package folder
     */
    @Override
    public PackagePath path()
    {
        return packagePath;
    }

    /**
     * Returns a {@link PackageReference} to this package
     */
    public PackageReference reference()
    {
        return packageReference(path());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceFolder<?> relativeTo(@NotNull ResourceFolder<?> folder)
    {
        if (folder instanceof Package relativeTo)
        {
            return packageForPath(this, (PackagePath) packagePath.relativeTo(relativeTo.packagePath));
        }
        else
        {
            return fail("Package can only be relative to another package");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean renameTo(@NotNull ResourceFolder<?> folder)
    {
        return unsupported();
    }

    /**
     * Returns the resource in this package with the given path
     */
    @Override
    @SuppressWarnings("SpellCheckingInspection")
    public Resource resource(@NotNull ResourcePathed pathed)
    {
        return packageResource(this, path(), pathed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceFolderIdentifier resourceFolderIdentifier()
    {
        return ResourceFolder.resourceFolderIdentifier(packagePath.join());
    }

    /**
     * Returns the resources in this package folder
     */
    @Override
    public ResourceList resources(@NotNull Matcher<ResourcePathed> matcher)
    {
        var list = resourceList();
        for (var resource : classpath().resources(this, packagePath.asPackageReference()).matching(matcher::matches))
        {
            list.add(packageResource(this, packagePath(resource.packageReference()), resource.fileName()));
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WritableResource temporaryFile(@NotNull FileName baseName,
                                          @NotNull Extension extension)
    {
        return unsupported();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceFolder<?> temporaryFolder(@NotNull FileName baseName)
    {
        return unsupported();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return packagePath.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI uri()
    {
        return packagePath.uri();
    }
}
