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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.language.packaging.PackageReference;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.ResourcePathed;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.reading.BaseReadableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.time.Time.now;
import static com.telenav.kivakit.resource.ResourcePath.parseResourcePath;
import static com.telenav.kivakit.resource.packages.Classpath.classpath;

/**
 * A readable {@link Resource} in a package, as specified by {@link PackageReference}. Package resources can be
 * constructed by several factory methods:
 *
 * <ul>
 *     <li>{@link #packageResource(Listener, StringPath, StringPath)}</li>
 *     <li>{@link #packageResource(Listener, StringPath)}</li>
 * </ul>
 * <p>
 * They can also be retrieved from a (KivaKit) {@link Package} with these methods:
 *
 * <ul>
 *     <li>{@link Package#resource(String)}</li>
 *     <li>{@link Package#resources()}</li>
 *     <li>{@link Package#resources(Matcher)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #createdAt()}</li>
 *     <li>{@link #isPackaged()}</li>
 *     <li>{@link #lastModified()}</li>
 *     <li>{@link #path()}</li>
 *     <li>{@link #sizeInBytes()}</li>
 * </ul>
 *
 * <p><b>NOTE</b></p>
 *
 * <p>
 * Note that {@link PackageResource}s cannot be retrieved from {@link PackageReference}s because package paths
 * are used in *kivakit-core*, which cannot depend on *kivakit-resource*. The easiest way to
 * get a resource from a package path is one of the factory methods above.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Package
 * @see BaseReadableResource
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramResourceType.class)
@TypeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class PackageResource extends BaseReadableResource
{
    /**
     * Returns a package resource for the resource at the given path relative to the given package. Note that
     * {@link StringPath} is the superclass of many other path objects, so most can be passed to this factory method.
     *
     * @param packagePath The path to a package
     * @param child A path of strings relative to the package path
     * @return The package resource
     */
    public static PackageResource packageResource(@NotNull Listener listener,
                                                  @NotNull StringPath packagePath,
                                                  @NotNull StringPath child)
    {
        return packageResource(listener, packagePath.withChild(child));
    }

    /**
     * Returns a package resource for the given string path. Note that {@link StringPath} is the superclass of many
     * other path objects, so most can be passed to this factory method.
     *
     * @param resourcePath The resource's path
     * @return The package resource
     */
    public static PackageResource packageResource(@NotNull Listener listener,
                                                  @NotNull StringPath resourcePath)
    {
        // Search the classpath for the given package and filename
        var found = classpath().allResources(listener).findFirst(resource ->
            resource.packageReference().equals(PackageReference.packageReference(resourcePath.withoutLast()))
                && resource.fileName().name().equals(resourcePath.last()));

        // and if the resource was found,
        if (found != null)
        {
            // return it
            return new PackageResource(listener, found);
        }

        listener.problem("Could not find: $", resourcePath);
        return null;
    }

    /**
     * Returns a package resource for the resource at the given path relative to the given package
     *
     * @param packagePath The package's path
     * @param child A path relative to the package path. Note that a number of classes implement {@link ResourcePathed},
     * including {@link FileName}, {@link ResourcePath}, {@link FilePath}, and {@link PackagePath}.
     * @return The package resource
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static PackageResource packageResource(@NotNull Listener listener,
                                                  @NotNull PackagePath packagePath,
                                                  @NotNull ResourcePathed child)
    {
        return packageResource(listener, packagePath, (StringPath) child.path());
    }

    /** The underlying classpath resource */
    private final ClasspathResource resource;

    protected PackageResource(@NotNull Listener listener, @NotNull ClasspathResource resource)
    {
        listener.listenTo(this);
        this.resource = resource;
    }

    /**
     * Returns the path to this resource from the classpath root (not including a parent JAR file or filesystem
     * folder).
     */
    public ResourcePath classpathPath()
    {
        return parseResourcePath(this, resource.packageReference().asSlashSeparated()
            + "/" + resource.fileName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FormatProperty
    public Time createdAt()
    {
        return resource.created();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof PackageResource that)
        {
            return resource.equals(that.resource);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return resource.hashCode();
    }

    @Override
    public ResourceIdentifier identifier()
    {
        return new ResourceIdentifier("classpath:" + packagePath() + "/" + fileName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPackaged()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FormatProperty
    public Time lastModified()
    {
        return resource == null ? now() : resource.lastModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream onOpenForReading()
    {
        try
        {
            return resource.asUri().toURL().openStream();
        }
        catch (Exception e)
        {
            return fatal(e, "Cannot open: $", resource);
        }
    }

    /**
     * Returns the package path for this resource
     */
    public PackagePath packagePath()
    {
        return PackagePath.packagePath(resource.packageReference());
    }

    /**
     * Returns the package reference to the package containing this resource
     */
    public PackageReference packageReference()
    {
        return resource.packageReference();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FormatProperty
    public ResourcePath path()
    {
        return resource.resourcePath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource relativeTo(@NotNull ResourceFolder<?> folder)
    {
        var relativePath = packagePath().relativeTo(folder.path());
        if (relativePath.isEmpty())
        {
            return packageResource(this, packagePath(), fileName());
        }
        return packageResource(this, packagePath().withChild(relativePath), fileName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @FormatProperty
    public Bytes sizeInBytes()
    {
        return resource == null ? Bytes._0 : resource.size();
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}
