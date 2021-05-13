////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
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
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.interfaces.io.ByteSized;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.time.ModificationTimestamped;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.compression.archive.ZipEntry;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.resource.path.ResourcePathed;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramFileSystemFile;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.kivakit.resource.resources.other.NullResource;
import com.telenav.kivakit.resource.resources.packaged.PackageResource;
import com.telenav.kivakit.resource.resources.string.StringResource;
import com.telenav.kivakit.resource.spi.ResourceResolverServiceLoader;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

/**
 * A resource that can be read via {@link ReadableResource}. In addition, resources are {@link ModificationTimestamped},
 * {@link ByteSized} and are message {@link Repeater}s. A resource can be created by instantiating a concrete
 * implementation of {@link Resource} or one can be resolved from an abstract {@link ResourceIdentifier} with {@link
 * #resolve(ResourceIdentifier)}.
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
 *     <li>{@link BitArrayResource}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #bytes()}</li>
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
        ModificationTimestamped,
        ByteSized,
        ReadableResource,
        Repeater,
        Resourceful
{
    Logger LOGGER = LoggerFactory.newLogger();

    static ResourceIdentifier identifier(final String identifier)
    {
        return new ResourceIdentifier(identifier);
    }

    static Resource resolve(final ResourceIdentifier identifier)
    {
        return ResourceResolverServiceLoader.resolve(identifier);
    }

    static Resource resolve(final String identifier)
    {
        return resolve(new ResourceIdentifier(identifier));
    }

    static ArgumentParser.Builder<Resource> resource(final Listener listener, final String description)
    {
        return ArgumentParser.builder(Resource.class)
                .converter(new Resource.Converter(listener))
                .description(description);
    }

    static SwitchParser.Builder<Resource> resource(
            final Listener listener,
            final String name,
            final String description)
    {
        return SwitchParser.builder(Resource.class)
                .name(name)
                .converter(new Resource.Converter(listener))
                .description(description);
    }

    static ArgumentParser.Builder<ResourceList> resourceList(final String description, final Extension extension)
    {
        return ArgumentParser.builder(ResourceList.class)
                .converter(new ResourceList.Converter(LOGGER, extension))
                .description(description);
    }

    static SwitchParser.Builder<ResourceList> resourceList(
            final String name,
            final String description,
            final Extension extension)
    {
        return SwitchParser.builder(ResourceList.class)
                .name(name)
                .converter(new ResourceList.Converter(LOGGER, extension))
                .description(description);
    }

    static SwitchParser.Builder<ResourcePath> resourcePath(final String name, final String description)
    {
        return SwitchParser.builder(ResourcePath.class)
                .name(name)
                .converter(new ResourcePath.Converter(LOGGER))
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
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected Resource onConvertToObject(final String value)
        {
            return new ResourceIdentifier(value).resolve();
        }
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

    default boolean isEmpty()
    {
        return bytes().isZero();
    }

    default boolean isLocal()
    {
        return !isRemote();
    }

    default boolean isMaterializable()
    {
        return isPackaged() || isRemote();
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
    default boolean isSame(final Resource that)
    {
        assert that != null;
        assert lastModified() != null;
        assert bytes() != null;
        assert that.lastModified() != null;
        assert that.bytes() != null;

        return lastModified().equals(that.lastModified()) && bytes().equals(that.bytes());
    }

    /**
     * @return A local cached copy of this resource if it is remote.
     */
    Resource materialized(ProgressReporter reporter);

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
    default void safeCopyTo(final Folder destination, final CopyMode mode)
    {
        safeCopyTo(destination.file(fileName()), mode, ProgressReporter.NULL);
    }

    /**
     * Copies this readable resource to the given folder safely
     *
     * @param destination The file to copy to
     * @param mode Copying semantics
     */
    default void safeCopyTo(final Folder destination, final CopyMode mode, final ProgressReporter reporter)
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
    default void safeCopyTo(final File destination, final CopyMode mode)
    {
        safeCopyTo(destination, mode, ProgressReporter.NULL);
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
    default void safeCopyTo(final File destination, final CopyMode mode, final ProgressReporter reporter)
    {
        // If there is no destination file or we can overwrite,
        if (mode.canCopy(this, destination))
        {
            // then copy to a temporary file
            final var temporary = destination.parent().temporaryFile(destination.fileName());
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
