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

import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.language.Objects;
import com.telenav.kivakit.core.language.module.ModuleResource;
import com.telenav.kivakit.core.language.module.PackageReference;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.ResourcePathed;
import com.telenav.kivakit.resource.lexakai.DiagramResourceService;
import com.telenav.kivakit.resource.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.reading.BaseReadableResource;
import com.telenav.kivakit.resource.spi.ResourceResolver;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.InputStream;
import java.lang.module.ModuleReference;
import java.net.URI;

import static com.telenav.kivakit.core.language.module.Modules.moduleResource;
import static com.telenav.kivakit.resource.FileName.parseFileName;
import static com.telenav.kivakit.resource.ResourcePath.resourcePath;
import static com.telenav.kivakit.resource.packages.PackagePath.packagePath;

/**
 * A readable {@link Resource} in a package, as specified by {@link PackageReference} or {@link ModuleResource} (which
 * has a {@link ModuleReference} and {@link URI}), and a name. Package resources can be constructed by several factory
 * methods:
 *
 * <ul>
 *     <li>{@link #packageResource(Listener, ModuleResource)}</li>
 *     <li>{@link #packageResource(Listener listener, Class, String)}</li>
 *     <li>{@link #packageResource(Listener listener, PackagePath, String)}</li>
 *     <li>{@link #packageResource(Listener, PackagePath, FileName)}</li>
 *     <li>{@link #packageResource(Listener, PackagePath, ResourcePathed)}</li>
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
 * Note that {@link PackageResource}s cannot be retrieved from {@link PackageReference}s because package paths
 * are used in *kivakit-core*, which cannot depend on *kivakit-resource*. The easiest way to
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
    public static PackageResource packageResource(Listener listener, ModuleResource resource)
    {
        var fileName = parseFileName(listener, resource.fileNameAsJavaPath().toString());
        return new PackageResource(listener, packagePath(resource.packageReference()), resource, fileName);
    }

    /**
     * @return A package resource for the resource at the given path relative to the given package
     */
    public static PackageResource packageResource(Listener listener, PackagePath packagePath, String path)
    {
        return packageResource(listener, packagePath, FilePath.parseFilePath(listener, path));
    }

    /**
     * @return A package resource for the resource at the given path relative to the given class
     */
    public static PackageResource packageResource(Listener listener, Class<?> type, String path)
    {
        return packageResource(listener, packagePath(type), path);
    }

    /**
     * @return A package resource for the resource at the given path relative to the given package
     */
    public static PackageResource packageResource(Listener listener, PackagePath packagePath, ResourcePathed relative)
    {
        var path = relative.path();
        var resource = moduleResource(listener, packagePath.withChild(path));
        if (path.size() == 1)
        {
            return new PackageResource(listener, packagePath, resource, path.fileName());
        }
        else
        {
            return new PackageResource(listener, packagePath.withChild(path.parent()), resource, path.fileName());
        }
    }

    /**
     * @return A package resource for the resource with the given filename in the given package
     */
    public static PackageResource packageResource(Listener listener, PackagePath packagePath, FileName name)
    {
        var resource = moduleResource(listener, packagePath.withChild(name.name()));
        return new PackageResource(listener, packagePath, resource, name);
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
        public boolean accepts(ResourceIdentifier identifier)
        {
            return identifier.identifier().startsWith(SCHEME);
        }

        @Override
        public Resource resolve(ResourceIdentifier identifier)
        {
            var filepath = FilePath.parseFilePath(this, Strip.leading(identifier.identifier(), SCHEME));
            var parent = filepath.parent();
            if (parent != null)
            {
                var packagePath = PackagePath.packagePath(parent);
                return packageResource(Listener.throwingListener(), packagePath, filepath.fileName());
            }
            return null;
        }
    }

    /** The name of this resource */
    private final FileName name;

    /** The package path to this resource */
    private final PackagePath packagePath;

    /** Information about the resource */
    private final ModuleResource resource;

    protected PackageResource(Listener listener, PackagePath packagePath, ModuleResource resource, FileName name)
    {
        listener.listenTo(this);
        this.packagePath = packagePath;
        this.name = name;
        this.resource = resource != null
                ? resource
                : packagePath.asPackageReference().moduleResource(this, this.name.name());
    }

    @Override
    public Time createdAt()
    {
        return resource.created();
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof PackageResource)
        {
            var that = (PackageResource) object;
            return Objects.equalPairs(packagePath, that.packagePath, name, that.name);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(packagePath, name);
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
    public Time modifiedAt()
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
                var path = packagePath.withRoot("/").join("/") + "/" + name;
                return packagePath.asPackageReference().moduleResourceStream(path);
            }
        }
        catch (IOException e)
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
        var path = resource != null
                ? resource.packageReference()
                : this.packagePath;

        return resourcePath(path).withChild(name.name());
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
        return resource != null ? resource.toString() : packagePath + "/" + name;
    }
}
