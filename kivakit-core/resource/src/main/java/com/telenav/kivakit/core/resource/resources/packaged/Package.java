////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.resources.packaged;

import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.language.locales.Locale;
import com.telenav.kivakit.core.kernel.language.matching.matchers.All;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.strings.Strip;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.resource.CopyMode;
import com.telenav.kivakit.core.resource.ResourceFolder;
import com.telenav.kivakit.core.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.core.resource.resources.other.PropertyMap;
import com.telenav.kivakit.core.resource.spi.ResourceFolderResolver;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * An abstraction missing from the JDK for dealing with the content of packages. A package folder is constructed with a
 * {@link PackagePath}, which specifies the location of the package. Resources in the package folder can be obtained
 * with {@link #resources()}.
 *
 * @author jonathanl (shibo)
 * @see PackagePath
 * @see PackageResource
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
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

    @UmlClassDiagram(diagram = DiagramResourceService.class)
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
    private final PackagePath _package;

    protected Package(final PackagePath _package)
    {
        this._package = _package;
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
    public ResourceFolder folder(final String path)
    {
        return subPackage(path);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(path());
    }

    @Override
    public boolean isMaterialized()
    {
        return false;
    }

    /**
     * @return A localized property map for the given locale
     */
    public PropertyMap localizedProperties(final Locale locale)
    {
        return PropertyMap.localized(path(), locale);
    }

    /**
     * @return The parent package of this package, or null if there is none
     */
    public Package parent()
    {
        final var parent = _package.withoutLast();
        return parent == null ? null : new Package(parent);
    }

    /**
     * @return The path to this package folder
     */
    public PackagePath path()
    {
        return _package;
    }

    /**
     * @return The resource in this package with the given name
     */
    @Override
    public PackageResource resource(final String name)
    {
        for (final var resource : resources())
        {
            if (resource.fileName().name().equals(name))
            {
                return resource;
            }
        }
        return fail("Unable to find package resource $ in package $", name, path());
    }

    /**
     * @return The resources in this package folder
     */
    @Override
    public List<PackageResource> resources()
    {
        return resources(new All<>());
    }

    /**
     * @return The resources in this package folder
     */
    public List<PackageResource> resources(final Matcher<PackageResource> matcher)
    {
        final var resources = _package
                .resources()
                .stream()
                .map(PackageResource::packageResource)
                .filter(matcher)
                .collect(Collectors.toList());

        resources.addAll(jarResources(matcher));

        return resources;
    }

    /**
     * Copy the resources in this package to the given folder
     */
    public void safeCopyTo(final Folder folder, final CopyMode mode, final ProgressReporter reporter)
    {
        for (final var at : resources())
        {
            final var destination = folder.mkdirs().file(at.fileName());
            if (mode.canCopy(at, destination))
            {
                at.safeCopyTo(destination, mode, reporter);
            }
        }
    }

    /**
     * @return The named sub-package under this one
     */
    public Package subPackage(final String name)
    {
        return new Package(_package.withChild(name));
    }

    @Override
    public String toString()
    {
        return _package.toString();
    }

    /**
     * List of resources loaded from this package folder in any jar that might contain this class.
     *
     * @return Any package resources that can be found in the jar (if any) containing this class
     */
    private List<PackageResource> jarResources(final Matcher<PackageResource> matcher)
    {
        // Get the code source for the package type class,
        final var resources = new ArrayList<PackageResource>();
        final CodeSource source = _package.packageType().getProtectionDomain().getCodeSource();
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
                    final var filepath = _package.join("/") + "/";

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
                                final var resource = PackageResource.packageResource(_package, name.substring(filepath.length()));
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
                LOGGER.warning("Exception thrown while loading jar resources from $", _package);
            }
        }
        return resources;
    }
}
