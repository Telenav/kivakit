////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.resources.packaged;

import com.telenav.kivakit.core.kernel.language.modules.ModuleResource;
import com.telenav.kivakit.core.kernel.language.modules.Modules;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.kernel.language.strings.Strip;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.ResourceIdentifier;
import com.telenav.kivakit.core.resource.ResourcePath;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.core.resource.reading.BaseReadableResource;
import com.telenav.kivakit.core.resource.spi.ResourceResolver;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.InputStream;

/**
 * A resource in a package, as specified by {@link PackagePath}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
public class PackageResource extends BaseReadableResource
{
    /**
     * @return A package resource for the given module resource
     */
    public static PackageResource packageResource(final ModuleResource resource)
    {
        final var fileName = new FileName(resource.fileNameAsJavaPath().toString());
        return new PackageResource(resource.packagePath(), resource, fileName);
    }

    /**
     * @return A package resource for the resource at the given path relative to the given package
     */
    public static PackageResource packageResource(final PackagePath _package, final String path)
    {
        return packageResource(_package, FilePath.parseFilePath(path));
    }

    /**
     * @return A package resource for the resource at the given path relative to the given class
     */
    public static PackageResource packageResource(final Class<?> type, final String path)
    {
        return packageResource(PackagePath.packagePath(type), path);
    }

    /**
     * @return A package resource for the resource at the given path relative to the given package
     */
    public static PackageResource packageResource(final PackagePath _package, final FilePath path)
    {
        final var resource = Modules.resource(_package.withChild(path));
        if (path.size() == 1)
        {
            return new PackageResource(_package, resource, path.fileName());
        }
        else
        {
            return new PackageResource(_package.withChild(path.parent()), resource, path.fileName());
        }
    }

    /**
     * @return A package resource for the resource with the given filename in the given package
     */
    public static PackageResource packageResource(final PackagePath _package, final FileName name)
    {
        final var resource = Modules.resource(_package.withChild(name.name()));
        return new PackageResource(_package, resource, name);
    }

    @UmlClassDiagram(diagram = DiagramResourceService.class)
    public static class Resolver implements ResourceResolver
    {
        public static final String SCHEME = "classpath:";

        @Override
        public boolean accepts(final ResourceIdentifier identifier)
        {
            return identifier.identifier().startsWith(SCHEME);
        }

        @Override
        public Resource resolve(final ResourceIdentifier identifier)
        {
            final var filepath = FilePath.parseFilePath(Strip.leading(identifier.identifier(), SCHEME));
            final var parent = filepath.parent();
            if (parent != null)
            {
                final var _package = PackagePath.packagePath(parent);
                final var name = filepath.fileName();
                return packageResource(_package, name);
            }
            return null;
        }
    }

    /** Information about the resource */
    private final ModuleResource resource;

    /** The package path to this resource */
    private final PackagePath _package;

    /** The name of this resource */
    private final FileName name;

    protected PackageResource(final PackagePath _package, final ModuleResource resource, final FileName name)
    {
        this._package = _package;
        this.name = name;
        this.resource = resource != null ? resource : _package.resource(this.name.name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bytes bytes()
    {
        return resource == null ? Bytes._0 : resource.size();
    }

    @Override
    public boolean isPackaged()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time lastModified()
    {
        return resource == null ? Time.now() : resource.lastModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream onOpenForReading()
    {
        try
        {
            if (resource != null)
            {
                return resource.uri().toURL().openStream();
            }
            else
            {
                final var path = _package.withRoot("/").join("/") + "/" + name;
                return _package.resourceStream(path);
            }
        }
        catch (final IOException e)
        {
            problem(e, "Unable to open package resource $", this);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath path()
    {
        final var _package = resource != null ? resource.packagePath() : this._package;
        return ResourcePath.resourcePath(_package).withChild(name.name());
    }

    @Override
    public String toString()
    {
        return resource.toString();
    }
}
