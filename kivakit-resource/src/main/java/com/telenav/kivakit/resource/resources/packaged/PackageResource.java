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

import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.modules.ModuleResource;
import com.telenav.kivakit.kernel.language.modules.Modules;
import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.language.objects.Objects;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.resource.reading.BaseReadableResource;
import com.telenav.kivakit.resource.spi.ResourceResolver;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.InputStream;
import java.lang.module.ModuleReference;
import java.net.URI;

/**
 * A readable {@link Resource} in a package, as specified by {@link PackagePath} or {@link ModuleResource} (which has a
 * {@link ModuleReference} and {@link URI}), and a name. Package resources can be constructed by several factory
 * methods:
 *
 * <ul>
 *     <li>{@link #of(ModuleResource)}</li>
 *     <li>{@link #of(Class, String)}</li>
 *     <li>{@link #of(PackagePath, String)}</li>
 *     <li>{@link #of(PackagePath, FileName)}</li>
 *     <li>{@link #of(PackagePath, FilePath)}</li>
 * </ul>
 * <p>
 * They can also be retrieved from a (KivaKit) {@link Package} with these methods:
 *
 * <ul>
 *     <li>{@link Package#resource(String)}</li>
 *     <li>{@link Package#resources()}</li>
 *     <li>{@link Package#resources(Matcher)}</li>
 * </ul>
 * <p>
 * Note that {@link PackageResource}s cannot be retrieved from {@link PackagePath}s because package paths
 * are used in *kivakit-core-kernel*, which cannot depend on *kivakit-core-resource*. The easiest way to
 * get a resource from a package path is one of the factory methods above.
 *
 * @author jonathanl (shibo)
 * @see Package
 * @see BaseReadableResource
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@LexakaiJavadoc(complete = true)
public class PackageResource extends BaseReadableResource
{
    /**
     * @return A package resource for the given module resource
     */
    public static PackageResource of(final ModuleResource resource)
    {
        final var fileName = FileName.parse(resource.fileNameAsJavaPath().toString());
        return new PackageResource(resource.packagePath(), resource, fileName);
    }

    /**
     * @return A package resource for the resource at the given path relative to the given package
     */
    public static PackageResource of(final PackagePath _package, final String path)
    {
        return of(_package, FilePath.parseFilePath(path));
    }

    /**
     * @return A package resource for the resource at the given path relative to the given class
     */
    public static PackageResource of(final Class<?> type, final String path)
    {
        return of(PackagePath.packagePath(type), path);
    }

    /**
     * @return A package resource for the resource at the given path relative to the given package
     */
    public static PackageResource of(final PackagePath _package, final FilePath path)
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
    public static PackageResource of(final PackagePath _package, final FileName name)
    {
        final var resource = Modules.resource(_package.withChild(name.name()));
        return new PackageResource(_package, resource, name);
    }

    /**
     * Resolves {@link ResourceIdentifier}s of the form "classpath:/a/b/resource.txt" into {@link Resource}s by creating
     * a {@link PackageResource} for the identifier.
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
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
                return of(_package, name);
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

    @Override
    public boolean equals(Object  object)
    {
        if (object instanceof PackageResource)
        {
            final var that = (PackageResource) object;
            return Objects.equalPairs(_package, that._package, name, that.name);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(_package, name);
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
            return fatal(e, "Unable to open package resource $", this);
        }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Bytes sizeInBytes()
    {
        return resource == null ? Bytes._0 : resource.size();
    }

    @Override
    public String toString()
    {
        return resource.toString();
    }
}
