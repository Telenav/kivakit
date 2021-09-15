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

package com.telenav.kivakit.resource.resources.packaged;

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.locales.Locale;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.resource.resources.other.PropertyMap;
import com.telenav.kivakit.resource.spi.ResourceFolderResolver;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.URI;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * An abstraction for locating and copying {@link Resource}s in Java packages.
 *
 * <p>
 * A package object can be constructed with {@link #of(PackagePath)} or {@link #of(Class, String)}. It implements the
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
 * Resources in a package can be obtained with {@link ResourceFolder#resources()} and {@link
 * ResourceFolder#resources(Matcher)}. A specific resource can be located with {@link ResourceFolder#resource(String)}.
 * The resources in a package can be copied to a {@link Folder} with {@link ResourceFolder#safeCopyTo(Folder, CopyMode,
 * ProgressReporter)}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see PackagePath
 * @see PackageResource
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@LexakaiJavadoc(complete = true)
public class Package implements ResourceFolder
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * @param _package The path to this package
     */
    public static Package of(final PackagePath _package)
    {
        return new Package(_package);
    }

    public static Package of(final Class<?> _packageType, final String path)
    {
        return of(PackagePath.parsePackagePath(_packageType, path));
    }

    /**
     * Resolves package resource identifiers that are of the form "classpath:/a/b/c" into {@link ResourceFolder}s (in
     * the form of {@link Package}s).
     *
     * @author jonathanl (shibo)
     * @see Resource#resolve(String)
     * @see Resource#resolve(ResourceIdentifier)
     */
    @UmlClassDiagram(diagram = DiagramResourceService.class)
    @LexakaiJavadoc(complete = true)
    public static class Resolver implements ResourceFolderResolver
    {
        public static final String SCHEME = "classpath:";

        @Override
        public boolean accepts(final ResourceFolderIdentifier identifier)
        {
            return identifier.identifier().startsWith(SCHEME);
        }

        @Override
        public ResourceFolder resolve(final ResourceFolderIdentifier identifier)
        {
            final var filepath = FilePath.parseFilePath(Strip.leading(identifier.identifier(), SCHEME));
            return Package.of(PackagePath.packagePath(filepath));
        }
    }

    /** The path to this package */
    private final PackagePath package_;

    protected Package(final PackagePath _package)
    {
        this.package_ = _package;
    }

    /**
     * @return The named sub-package under this one
     */
    public Package child(final String name)
    {
        return new Package(package_.withChild(name));
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Package)
        {
            final Package that = (Package) object;
            return path().equals(that.path());
        }
        return false;
    }

    @Override
    public Package folder(final String path)
    {
        return child(path);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(path());
    }

    @Override
    public ResourceFolderIdentifier identifier()
    {
        return ResourceFolder.identifier(package_.packageType() + ":" + package_.join());
    }

    @Override
    public boolean isMaterialized()
    {
        return false;
    }

    /**
     * @return A localized property map for the given locale
     */
    public PropertyMap localizedProperties(Listener listener, final Locale locale)
    {
        return PropertyMap.localized(listener, path(), locale);
    }

    /**
     * @return The parent package of this package, or null if there is none
     */
    public Package parent()
    {
        final var parent = package_.withoutLast();
        return parent == null ? null : new Package(parent);
    }

    /**
     * @return The path to this package folder
     */
    public PackagePath path()
    {
        return package_;
    }

    /**
     * @return The resource in this package with the given name
     */
    @Override
    public PackageResource resource(final String name)
    {
        if (name.contains("/"))
        {
            final var path = FilePath.parseFilePath(name);
            return folder(path.withoutLast().toString()).resource(path.last());
        }
        for (final var resource : resources())
        {
            if (resource.fileName().name().equals(name))
            {
                return (PackageResource) resource;
            }
        }
        return fail("Unable to find package resource $ in package $", name, path());
    }

    /**
     * @return The resources in this package folder
     */
    @Override
    public List<PackageResource> resources(final Matcher<? super Resource> matcher)
    {
        final var resources = package_
                .resources()
                .stream()
                .map(PackageResource::of)
                .filter(matcher)
                .collect(Collectors.toList());

        final var existing = new HashSet<>(resources);
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

        return resources;
    }

    @Override
    public String toString()
    {
        return package_.toString();
    }

    @Override
    public URI uri()
    {
        return Classes.resourceUri(package_.packageType(), package_.join("/"));
    }

    /**
     * List of resources loaded from this package folder in any directory classpath that might contain this class.
     *
     * @return Any package resources that can be found in the directory classpath (if any) containing this class
     */
    private List<PackageResource> directoryResources(final Matcher<? super PackageResource> matcher)
    {
        // Get the code source for the package type class,
        final var resources = new ArrayList<PackageResource>();
        final CodeSource source = package_.packageType().getProtectionDomain().getCodeSource();
        if (source != null)
        {
            try
            {
                // and if the location URL ends in "/",
                final URL location = source.getLocation();
                if (location != null && location.toString().endsWith("/"))
                {
                    final var filepath = package_.join("/") + "/";
                    var directory = Folder.of(location.toURI()).folder(filepath);
                    if (directory.exists())
                    {
                        for (var file : directory.files())
                        {
                            var resource = PackageResource.of(package_, file.fileName().name());
                            if (matcher.matches(resource))
                            {
                                resources.add(resource);
                            }
                        }
                    }
                }
            }
            catch (final Exception ignored)
            {
                LOGGER.warning("Exception thrown while loading directory resources from $", package_);
            }
        }

        return resources;
    }

    /**
     * List of resources loaded from this package folder in any jar that might contain this class.
     *
     * @return Any package resources that can be found in the jar (if any) containing this class
     */
    private List<PackageResource> jarResources(final Matcher<? super PackageResource> matcher)
    {
        // Get the code source for the package type class,
        final var resources = new ArrayList<PackageResource>();
        final CodeSource source = package_.packageType().getProtectionDomain().getCodeSource();
        if (source != null)
        {
            try
            {
                // and if the location URL ends in ".jar",
                final URL location = source.getLocation();
                if (location != null && location.toString().endsWith(".jar"))
                {
                    // then open the jar as a zip input stream,
                    final var urlConnection = location.openConnection();
                    final ZipInputStream zip = new ZipInputStream(urlConnection.getInputStream());

                    // form a file path from the package path,
                    final var filepath = package_.join("/") + "/";

                    // and loop,
                    while (true)
                    {
                        // reading the next entry,
                        final ZipEntry e = zip.getNextEntry();

                        // until we are out.
                        if (e == null)
                        {
                            break;
                        }

                        // Get the entry's name
                        final String name = e.getName();
                        // and if it is not a folder and it starts with the file path for the package,
                        if (!name.endsWith("/") && name.startsWith(filepath))
                        {
                            // then strip off the leading filepath,
                            final var suffix = Strip.leading(name, filepath);
                            // and if we have only a filename left,
                            if (!suffix.contains("/"))
                            {
                                // then the entry is in the package, so add it to the resources list
                                final var resource = PackageResource.of(package_, name.substring(filepath.length()));
                                if (matcher.matches(resource))
                                {
                                    resources.add(resource);
                                }
                            }
                        }
                    }
                }
            }
            catch (final Exception ignored)
            {
                LOGGER.warning("Exception thrown while loading jar resources from $", package_);
            }
        }
        return resources;
    }
}
