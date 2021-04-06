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

package com.telenav.kivakit.core.resource;

import com.telenav.kivakit.core.commandline.ArgumentParser;
import com.telenav.kivakit.core.commandline.SwitchParser;
import com.telenav.kivakit.core.filesystem.File;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.interfaces.io.ByteSized;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.time.ModificationTimestamped;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Repeater;
import com.telenav.kivakit.core.resource.compression.Codec;
import com.telenav.kivakit.core.resource.path.Extension;
import com.telenav.kivakit.core.resource.path.ResourcePathed;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemFile;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.kivakit.core.resource.spi.ResourceResolverServiceLoader;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
public interface Resource extends ResourcePathed, ModificationTimestamped, ByteSized, ReadableResource, Repeater, Resourced
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
     * Copies this readable resource to the given file safely (ensuring that a corrupted copy of the file never exists).
     * This is done by first copying to a temporary file in the same folder. If the copy operation is successful, the
     * destination file is then removed and the temporary file is renamed to the destination file's name.
     *
     * @param destination The file to copy to
     */
    default void safeCopyTo(final File destination, final CopyMode mode, final ProgressReporter reporter)
    {
        // If there is no destination file or we can overwrite,
        if (mode.canCopy(this, destination))
        {
            // then copy to a temporary file
            final var folder = destination.parent();
            final var temporary = folder.temporaryFile(destination.fileName());
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
