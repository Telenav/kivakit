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

package com.telenav.kivakit.resource;

import com.telenav.kivakit.commandline.ArgumentParser;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.time.LastModifiedAt;
import com.telenav.kivakit.core.time.CreatedAt;
import com.telenav.kivakit.core.time.Modifiable;
import com.telenav.kivakit.core.value.count.ByteSized;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.string.StringSource;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.compression.archive.ZipEntry;
import com.telenav.kivakit.resource.lexakai.DiagramFileSystemFile;
import com.telenav.kivakit.resource.lexakai.DiagramResource;
import com.telenav.kivakit.resource.packages.PackageResource;
import com.telenav.kivakit.resource.reading.ReadableResource;
import com.telenav.kivakit.resource.resources.NullResource;
import com.telenav.kivakit.resource.resources.StringResource;
import com.telenav.kivakit.resource.spi.ResourceResolver;
import com.telenav.kivakit.resource.spi.ResourceResolverServiceLoader;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.ServiceLoader;

/**
 * A resource that can be read via {@link ReadableResource}. In addition, resources are {@link LastModifiedAt}, {@link
 * ByteSized} and are message {@link Repeater}s. A resource can be created by instantiating a concrete implementation of
 * {@link Resource} or one can be resolved from an abstract {@link ResourceIdentifier} with {@link #resolve(Listener,
 * ResourceIdentifier)}.
 *
 * <p><b>Examples</b></p>
 * <p>
 * Some examples of resources include:
 *
 * <ul>
 *     <li>{@link File}</li>
 *     <li>{@link PackageResource}</li>
 *     <li>{@link StringResource}</li>
 *     <li>{@link ZipEntry}</li>
 *     <li>HttpGetResource</li>
 *     <li>HttpPostResource</li>
 *     <li>FtpResource</li>
 *     <li>{@link NullResource}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #sizeInBytes()}</li>
 *     <li>{@link #codec()}</li>
 *     <li>{@link #path()}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #materialized(ProgressReporter)}</li>
 *     <li>{@link #dematerialize()}</li>
 *     <li>{@link #safeCopyTo(File, CopyMode, ProgressReporter)}</li>
 * </ul>
 *
 * <p><b>Checks</b></p>
 *
 * <ul>
 *     <li>{@link #exists()}</li>
 *     <li>{@link #ensureExists()}</li>
 *     <li>{@link #isRemote()}</li>
 *     <li>{@link #isLocal()}</li>
 *     <li>{@link #isEmpty()}</li>
 *     <li>{@link #isMaterializable()}</li>
 *     <li>{@link #isSame(Resource)}</li>
 * </ul>
 */
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
@LexakaiJavadoc(complete = true)
public interface Resource extends
        ResourcePathed,
        Modifiable,
        LastModifiedAt,
        CreatedAt,
        ByteSized,
        StringSource,
        ReadableResource,
        Repeater,
        Resourceful,
        UriIdentified
{
    static ArgumentParser.Builder<Resource> argumentParser(Listener listener, String description)
    {
        return ArgumentParser.builder(Resource.class)
                .converter(new Resource.Converter(listener))
                .description(description);
    }

    /**
     * Returns a {@link ResourceIdentifier} for the given string
     *
     * @param identifier The identifier
     * @return The {@link ResourceIdentifier}
     */
    static ResourceIdentifier identifier(String identifier)
    {
        return new ResourceIdentifier(identifier);
    }

    /**
     * Resolves the given {@link ResourceIdentifier} to a {@link Resource}. This is done by using {@link
     * ResourceResolverServiceLoader} to find an implementation of {@link ResourceResolver} using Java's {@link
     * ServiceLoader} to find the implementation.
     *
     * @param listener The listener to call with any resolution problems
     * @param identifier The resource identifier
     * @return The resource
     */
    static Resource resolve(Listener listener, ResourceIdentifier identifier)
    {
        return listener.listenTo(listener.listenTo(ResourceResolverServiceLoader.get()).resolve(identifier));
    }

    static Resource resolve(Listener listener, ResourcePath path)
    {
        return resolve(listener, path.asString());
    }

    static Resource resolve(Listener listener, String identifier)
    {
        return resolve(listener, new ResourceIdentifier(identifier));
    }

    static SwitchParser.Builder<Resource> resourceSwitchParser(
            Listener listener,
            String name,
            String description)
    {
        return SwitchParser.builder(Resource.class)
                .name(name)
                .converter(new Resource.Converter(listener))
                .description(description);
    }

    /**
     * Converts to and from {@link Resource}s by resolving {@link ResourceIdentifier}s.
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    class Converter extends BaseStringConverter<Resource>
    {
        public Converter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected Resource onToValue(String value)
        {
            return new ResourceIdentifier(value).resolve(this);
        }
    }

    @Override
    default String asString()
    {
        return reader().asString();
    }

    /**
     * @return Any codec for compression / decompression
     */
    @UmlRelation(label = "uses")
    Codec codec();

    /**
     * Remove any materialized local copy if this is a remote resource that's been cached
     */
    default void dematerialize()
    {
    }

    default boolean endsWith(String end)
    {
        return path().endsWith(end);
    }

    /**
     * Ensures that this file exists or throws a runtime exception
     */
    default void ensureExists()
    {
        if (!exists())
        {
            throw new IllegalStateException(this + " does not exist");
        }
    }

    /**
     * @return True if the resource exists
     */
    boolean exists();

    default boolean hasParent()
    {
        return parent() != null;
    }

    default boolean isEmpty()
    {
        return sizeInBytes().isZero();
    }

    default boolean isLocal()
    {
        return !isRemote();
    }

    default boolean isMaterializable()
    {
        return isPackaged() || isRemote();
    }

    default boolean isOlderThan(Resource that)
    {
        return lastModified().isOlderThan(that.lastModified());
    }

    /**
     * @return True if this resource is in a JAR file
     */
    default boolean isPackaged()
    {
        return false;
    }

    /**
     * @return True if this resource is on a remote filesystem
     */
    default boolean isRemote()
    {
        return false;
    }

    /**
     * @return True if the given resource has the same last modified time and the same size
     */
    default boolean isSame(Resource that)
    {
        assert that != null;
        assert lastModified() != null;
        assert sizeInBytes() != null;
        assert that.lastModified() != null;
        assert that.sizeInBytes() != null;

        return lastModified().equals(that.lastModified()) && sizeInBytes().equals(that.sizeInBytes());
    }

    /**
     * @return A local cached copy of this resource if it is remote.
     */
    Resource materialized(ProgressReporter reporter);

    /**
     * Returns the parent folder of this resource
     *
     * @return The parent
     */
    default ResourceFolder parent()
    {
        return null;
    }

    /**
     * StringPath to this resource
     */
    @Override
    ResourcePath path();

    @Override
    default Resource resource()
    {
        return this;
    }

    /**
     * Copies this readable resource to the given folder safely
     *
     * @param destination The file to copy to
     * @param mode Copying semantics
     */
    default void safeCopyTo(Folder destination, CopyMode mode)
    {
        safeCopyTo(destination.file(fileName()), mode, ProgressReporter.none());
    }

    /**
     * Copies this readable resource to the given folder safely
     *
     * @param destination The file to copy to
     * @param mode Copying semantics
     */
    default void safeCopyTo(Folder destination, CopyMode mode, ProgressReporter reporter)
    {
        safeCopyTo(destination.file(fileName()), mode, reporter);
    }

    /**
     * Copies this resource to the given file safely (ensuring that a corrupted copy of the file never exists). This is
     * done by first copying to a temporary file in the same folder. If the copy operation is successful, the
     * destination file is then removed and the temporary file is renamed to the destination file's name.
     *
     * @param destination The file to copy to
     * @param mode Copying semantics
     */
    default void safeCopyTo(File destination, CopyMode mode)
    {
        safeCopyTo(destination, mode, ProgressReporter.none());
    }

    /**
     * Copies this resource to the given file safely (ensuring that a corrupted copy of the file never exists). This is
     * done by first copying to a temporary file in the same folder. If the copy operation is successful, the
     * destination file is then removed and the temporary file is renamed to the destination file's name.
     *
     * @param destination The file to copy to
     * @param mode Copying semantics
     * @param reporter Progress reporter to call as copy proceeds
     */
    default void safeCopyTo(File destination, CopyMode mode, ProgressReporter reporter)
    {
        // If there is no destination file or we can overwrite,
        if (mode.canCopy(this, destination))
        {
            // then copy to a temporary file
            var temporary = destination.parent().temporaryFile(destination.fileName());
            copyTo(temporary, mode, reporter);

            // remove the destination file
            if (destination.exists())
            {
                destination.delete();
            }

            // and rename the temporary file to the destination file.
            temporary.renameTo(destination);
        }
    }
}
