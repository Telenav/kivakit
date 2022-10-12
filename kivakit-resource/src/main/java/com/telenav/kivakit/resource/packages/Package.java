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
import com.telenav.kivakit.core.language.module.PackageReference;
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
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.ResourceList;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.ResourcePathed;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceService;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.spi.ResourceFolderResolver;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.language.Classes.resourceUri;
import static com.telenav.kivakit.core.language.module.PackageReference.packageReference;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.string.Strip.stripLeading;
import static com.telenav.kivakit.filesystem.FilePath.parseFilePath;
import static com.telenav.kivakit.properties.PropertyMap.loadLocalizedPropertyMap;
import static com.telenav.kivakit.resource.ResourceList.resourceList;
import static com.telenav.kivakit.resource.packages.PackagePath.packagePath;
import static com.telenav.kivakit.resource.packages.PackagePath.parsePackagePath;
import static com.telenav.kivakit.resource.packages.PackageResource.packageResource;
import static java.util.Objects.hash;

/**
 * An abstraction for locating and copying {@link Resource}s in Java packages.
 *
 * <p>
 * A package object can be constructed with {@link #packageForPath(Listener, PackagePath)} or
 * {@link #parsePackage(Listener listener, Class, String)}. It implements the {@link ResourceFolder} interface because
 * it contains resources, just as {@link Folder} does. This means, of course, that methods that accept
 * {@link ResourceFolder} can accept either {@link Folder}s or {@link Package}s.
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
@CodeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTATION_COMPLETE,
             testing = UNTESTED)
public class Package extends BaseRepeater implements ResourceFolder<Package>
{
    /**
     * Returns the package containing the given type
     *
     * @param listener The listener to call with any problems
     */
    public static Package packageFor(@NotNull Listener listener,
                                     @NotNull Class<?> packageType)
    {
        return new Package(listener, packagePath(packageType));
    }

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
        return packageForPath(listener, parsePackagePath(listener, packageType, path));
    }

    /**
     * Resolves package resource identifiers that are of the form "classpath:/a/b/c" into {@link ResourceFolder}s (in
     * the form of {@link Package}s).
     *
     * @author jonathanl (shibo)
     * @see Resource#resolveResource(Listener, String)
     * @see Resource#resolveResource(Listener, ResourceIdentifier)
     */
    @UmlClassDiagram(diagram = DiagramResourceService.class)
    @CodeQuality(stability = STABLE_EXTENSIBLE,
                 documentation = DOCUMENTATION_COMPLETE,
                 testing = UNTESTED)
    public static class Resolver implements ResourceFolderResolver
    {
        public static final String SCHEME = "classpath:";

        @Override
        public boolean accepts(@NotNull ResourceFolderIdentifier identifier)
        {
            return identifier.identifier().startsWith(SCHEME);
        }

        @Override
        public Package resolve(@NotNull ResourceFolderIdentifier identifier)
        {
            var filepath = parseFilePath(this, stripLeading(identifier.identifier(), SCHEME));
            return packageForPath(throwingListener(), packagePath(filepath));
        }
    }

    /** The path to this package */
    private final PackagePath packagePath;

    public Package(@NotNull Listener listener,
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
        if (object instanceof Package)
        {
            Package that = (Package) object;
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

        for (var child : reference().subPackages(this))
        {
            children.add(packageForPath(this, packagePath(child)));
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
        return packageReference(path().packageType(), path());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceFolder<?> relativeTo(@NotNull ResourceFolder<?> folder)
    {
        if (folder instanceof Package)
        {
            var relativeTo = (Package) folder;
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
        return packageResource(this, path(), pathed.path());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceFolderIdentifier resourceFolderIdentifier()
    {
        return ResourceFolder.resourceFolderIdentifier(packagePath.packageType() + ":" + packagePath.join());
    }

    /**
     * Returns the resources in this package folder
     */
    @Override
    public ResourceList resources(@NotNull Matcher<ResourcePathed> matcher)
    {
        var resources = packagePath
                .asPackageReference()
                .moduleResources(this)
                .stream()
                .map(moduleResource -> packageResource(this, moduleResource))
                .filter(matcher)
                .collect(Collectors.toList());

        var existing = new HashSet<>(resources);
        Consumer<PackageResource> addDeduplicated = resource ->
        {
            if (!existing.contains(resource))
            {
                resources.add(resource);
                existing.add(resource);
            }
        };

        jarResources(matcher).forEach(addDeduplicated);
        directoryResources(matcher).forEach(addDeduplicated);

        return resourceList(resources);
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
        try
        {
            return resourceUri(packagePath.packageType(), packagePath.join("/"));
        }
        catch (IllegalArgumentException ignored)
        {
            // If there is no file in the package, we can't get a URI for the package
            return null;
        }
    }

    /**
     * Returns a list of package resources matching the given matcher, and found in any directory on the classpath with
     * a path that matches this package's path.
     *
     * @param matcher The matcher that must match resources
     */
    private List<PackageResource> directoryResources(@NotNull Matcher<? super PackageResource> matcher)
    {
        // Get the code source for the package type class,
        var resources = new ArrayList<PackageResource>();
        var source = packagePath.hasPackageType() ? packagePath.packageType().getProtectionDomain().getCodeSource() : null;
        if (source != null)
        {
            try
            {
                // and if the location URL ends in "/",
                URL location = source.getLocation();
                if (location != null && location.toString().endsWith("/"))
                {
                    var filepath = packagePath.join("/") + "/";
                    var directory = Folder.folder(location.toURI()).folder(filepath);
                    if (directory.exists())
                    {
                        for (var file : directory.files())
                        {
                            var resource = packageResource(this, packagePath, file.fileName().name());
                            if (matcher.matches(resource))
                            {
                                resources.add(resource);
                            }
                        }
                    }
                }
            }
            catch (Exception ignored)
            {
                warning("Exception thrown while loading directory resources from $", packagePath);
            }
        }

        return resources;
    }

    /**
     * Returns a list of package resources matching the given matcher, and found in any JAR on the classpath with a path
     * that matches this package's path.
     *
     * @param matcher The matcher that must match resources
     */
    private List<PackageResource> jarResources(@NotNull Matcher<? super PackageResource> matcher)
    {
        // Get the code source for the package type class,
        var resources = new ArrayList<PackageResource>();
        var source = packagePath.hasPackageType() ? packagePath.packageType().getProtectionDomain().getCodeSource() : null;
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
                    var filepath = packagePath.join("/") + "/";

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
                        // and if it is not a folder, and it starts with the file path for the package,
                        if (!name.endsWith("/") && name.startsWith(filepath))
                        {
                            // then strip off the leading filepath,
                            var suffix = stripLeading(name, filepath);
                            // and if we have only a filename left,
                            if (!suffix.contains("/"))
                            {
                                // then the entry is in the package, so add it to the resources list
                                var resource = packageResource(this, packagePath, name.substring(filepath.length()));
                                if (matcher.matches(resource))
                                {
                                    resources.add(resource);
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ignored)
            {
                warning("Exception thrown while loading jar resources from $", packagePath);
            }
        }
        return resources;
    }
}
