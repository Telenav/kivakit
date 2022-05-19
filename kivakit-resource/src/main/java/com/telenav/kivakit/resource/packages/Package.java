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

import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.language.module.PackageReference;
import com.telenav.kivakit.core.locale.Locale;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.filesystem.FilePath;
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
import com.telenav.kivakit.resource.lexakai.DiagramResourceService;
import com.telenav.kivakit.resource.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.spi.ResourceFolderResolver;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.language.module.PackageReference.packageReference;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.resource.ResourceList.resourceList;
import static com.telenav.kivakit.resource.packages.PackagePath.packagePath;
import static com.telenav.kivakit.resource.packages.PackagePath.parsePackagePath;
import static com.telenav.kivakit.resource.packages.PackageResource.packageResource;

/**
 * An abstraction for locating and copying {@link Resource}s in Java packages.
 *
 * <p>
 * A package object can be constructed with {@link #packageForPath(Listener, PackagePath)} or {@link
 * #parsePackage(Listener listener, Class, String)}. It implements the {@link ResourceFolder} interface because it
 * contains resources, just as {@link Folder} does. This means, of course, that methods that accept {@link
 * ResourceFolder} can accept either {@link Folder}s or {@link Package}s.
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
 * Resources in a package can be obtained with {@link ResourceFolder#resources()} and {@link
 * ResourceFolder#resources(Matcher)}. A specific resource can be located with {@link ResourceFolder#resource(String)}.
 * The resources in a package can be copied to a {@link Folder} with {@link ResourceFolder#safeCopyTo(ResourceFolder,
 * CopyMode, ProgressReporter)}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see PackageReference
 * @see PackageResource
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramResourceType.class)
@LexakaiJavadoc(complete = true)
public class Package extends BaseRepeater implements ResourceFolder<Package>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static Package packageContaining(Listener listener, Class<?> packageType)
    {
        return new Package(listener, packagePath(packageType));
    }

    /**
     * @param packagePath The path to this package
     */
    public static Package packageForPath(Listener listener, PackagePath packagePath)
    {
        return new Package(listener, packagePath);
    }

    public static Package parsePackage(Listener listener, Class<?> packageType, String path)
    {
        return packageForPath(listener, parsePackagePath(listener, packageType, path));
    }

    /**
     * Resolves package resource identifiers that are of the form "classpath:/a/b/c" into {@link ResourceFolder}s (in
     * the form of {@link Package}s).
     *
     * @author jonathanl (shibo)
     * @see Resource#resolve(Listener, String)
     * @see Resource#resolve(Listener, ResourceIdentifier)
     */
    @UmlClassDiagram(diagram = DiagramResourceService.class)
    @LexakaiJavadoc(complete = true)
    public static class Resolver implements ResourceFolderResolver
    {
        public static final String SCHEME = "classpath:";

        @Override
        public boolean accepts(ResourceFolderIdentifier identifier)
        {
            return identifier.identifier().startsWith(SCHEME);
        }

        @Override
        public Package resolve(ResourceFolderIdentifier identifier)
        {
            var filepath = FilePath.parseFilePath(this, Strip.leading(identifier.identifier(), SCHEME));
            return packageForPath(throwingListener(), packagePath(filepath));
        }
    }

    /** The path to this package */
    private final PackagePath packagePath;

    public Package(Listener listener, PackagePath packagePath)
    {
        listener.listenTo(this);

        this.packagePath = packagePath;
    }

    /**
     * @return The named sub-package under this one
     */
    public Package child(String name)
    {
        return new Package(this, packagePath.withChild(name));
    }

    @Override
    public boolean delete()
    {
        return unsupported();
    }

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

    @Override
    public boolean exists()
    {
        return unsupported();
    }

    @Override
    public Package folder(String path)
    {
        return child(path);
    }

    @Override
    public List<Package> folders()
    {
        var children = new ArrayList<Package>();

        for (var child : reference().subPackages(this))
        {
            children.add(packageForPath(this, packagePath(child)));
        }

        return children;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(path());
    }

    @Override
    public ResourceFolderIdentifier identifier()
    {
        return ResourceFolder.identifier(packagePath.packageType() + ":" + packagePath.join());
    }

    @Override
    public boolean isMaterialized()
    {
        return false;
    }

    /**
     * @return A localized property map for the given locale
     */
    public PropertyMap localizedProperties(Listener listener, Locale locale)
    {
        return PropertyMap.localized(listener, path(), locale);
    }

    @Override
    public ResourceFolder<?> newFolder(ResourcePath relativePath)
    {
        return packageForPath(this, packagePath(relativePath));
    }

    /**
     * @return The parent package of this package, or null if there is none
     */
    @Override
    public Package parent()
    {
        var parent = packagePath.withoutLast();
        return parent == null ? null : new Package(this, parent);
    }

    /**
     * @return The path to this package folder
     */
    @Override
    public PackagePath path()
    {
        return packagePath;
    }

    public PackageReference reference()
    {
        return packageReference(path().packageType(), path());
    }

    @Override
    public boolean renameTo(final ResourceFolder<?> folder)
    {
        return unsupported();
    }

    /**
     * @return The resource in this package with the given name
     */
    @Override
    public Resource resource(ResourcePathed pathed)
    {
        return packageResource(this, path(), pathed.path());
    }

    /**
     * @return The resources in this package folder
     */
    @Override
    public ResourceList resources(Matcher<? super Resource> matcher)
    {
        var resources = packagePath
                .asPackageReference()
                .moduleResources(this)
                .stream()
                .map(moduleResource -> packageResource(this, moduleResource))
                .filter(matcher.asPredicate())
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

    @Override
    public WritableResource temporary(final FileName baseName, final Extension extension)
    {
        return unsupported();
    }

    @Override
    public ResourceFolder<?> temporaryFolder(final FileName baseName)
    {
        return unsupported();
    }

    @Override
    public String toString()
    {
        return packagePath.toString();
    }

    @Override
    public URI uri()
    {
        try
        {
            return Classes.resourceUri(packagePath.packageType(), packagePath.join("/"));
        }
        catch (IllegalArgumentException ignored)
        {
            // If there is no file in the package, we can't get a URI for the package
            return null;
        }
    }

    /**
     * List of resources loaded from this package folder in any directory classpath that might contain this class.
     *
     * @return Any package resources that can be found in the directory classpath (if any) containing this class
     */
    private List<PackageResource> directoryResources(Matcher<? super PackageResource> matcher)
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
                LOGGER.warning("Exception thrown while loading directory resources from $", packagePath);
            }
        }

        return resources;
    }

    /**
     * List of resources loaded from this package folder in any jar that might contain this class.
     *
     * @return Any package resources that can be found in the jar (if any) containing this class
     */
    private List<PackageResource> jarResources(Matcher<? super PackageResource> matcher)
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
                            var suffix = Strip.leading(name, filepath);
                            // and if we have only a filename left,
                            if (!suffix.contains("/"))
                            {
                                // then the entry is in the package, so add it to the resources list
                                var resource = packageResource(LOGGER, packagePath, name.substring(filepath.length()));
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
                LOGGER.warning("Exception thrown while loading jar resources from $", packagePath);
            }
        }
        return resources;
    }
}
