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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.commandline.ArgumentParser;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.time.CreatedAt;
import com.telenav.kivakit.core.time.Modifiable;
import com.telenav.kivakit.core.time.ModifiedAt;
import com.telenav.kivakit.core.value.count.ByteSized;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.interfaces.string.AsString;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.compression.archive.ZipEntry;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFile;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResource;
import com.telenav.kivakit.resource.packages.PackageResource;
import com.telenav.kivakit.resource.reading.ReadableResource;
import com.telenav.kivakit.resource.resources.NullResource;
import com.telenav.kivakit.resource.resources.StringResource;
import com.telenav.kivakit.resource.spi.ResourceResolver;
import com.telenav.kivakit.resource.spi.ResourceResolverService;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.commandline.ArgumentParser.argumentParser;
import static com.telenav.kivakit.commandline.SwitchParser.switchParser;
import static com.telenav.kivakit.core.collections.set.ObjectSet.emptySet;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;
import static com.telenav.kivakit.resource.Resource.Action.DELETE;
import static com.telenav.kivakit.resource.Resource.Action.RENAME;
import static com.telenav.kivakit.resource.spi.ResourceResolverService.resourceResolverService;

/**
 * A resource that can be read via {@link ReadableResource}. In addition, resources are {@link ModifiedAt},
 * {@link ByteSized} and are message {@link Repeater}s. A resource can be created by instantiating a concrete
 * implementation of {@link Resource} or one can be resolved from an abstract {@link ResourceIdentifier} with
 * {@link #resolveResource(Listener, ResourceIdentifier)}.
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
 * <p><b>Resource Resolution</b></p>
 *
 * <ul>
 *     <li>{@link #resolveResource(Listener, ResourceIdentifier)}</li>
 *     <li>{@link #resolveResource(Listener, ResourcePath)}</li>
 *     <li>{@link #resolveResource(Listener, String)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #codec()}</li>
 *     <li>{@link #exists()}</li>
 *     <li>{@link #extension()}</li>
 *     <li>{@link #fileName()}</li>
 *     <li>{@link #parent()}</li>
 *     <li>{@link #path()}</li>
 *     <li>{@link #sizeInBytes()}</li>
 * </ul>
 *
 * <p><b>Materialization</b></p>
 *
 * <ul>
 *     <li>{@link #isMaterializable()}</li>
 *     <li>{@link #materialized(ProgressReporter)}</li>
 *     <li>{@link #dematerialize()}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #can()}</li>
 *     <li>{@link #can(Action)}</li>
 *     <li>{@link #delete()}</li>
 *     <li>{@link #renameTo(Resource)}</li>
 *     <li>{@link #safeCopyTo(WritableResource, WriteMode, ProgressReporter)}</li>
 *     <li>{@link #safeCopyTo(WritableResource, WriteMode)}</li>
 *     <li>{@link #safeCopyTo(ResourceFolder, WriteMode, ProgressReporter)}</li>
 *     <li>{@link #safeCopyTo(ResourceFolder, WriteMode)}</li>
 * </ul>
 *
 * <p><b>Tests</b></p>
 *
 * <ul>
 *     <li>{@link #ensureExists()}</li>
 *     <li>{@link #exists()}</li>
 *     <li>{@link #hasParent()}</li>
 *     <li>{@link #isEmpty()}</li>
 *     <li>{@link #isLocal()}</li>
 *     <li>{@link #isPackaged()}</li>
 *     <li>{@link #isMaterializable()}</li>
 *     <li>{@link #isOlderThan(Resource)}</li>
 *     <li>{@link #isRemote()}</li>
 *     <li>{@link #isSame(Resource)}</li>
 * </ul>
 */
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface Resource extends
        ResourcePathed,
        Modifiable,
        ModifiedAt,
        CreatedAt,
        Deletable,
        ByteSized,
        AsString,
        ReadableResource,
        Repeater,
        Resourceful,
        UriIdentified
{
    /**
     * Resolves the given {@link ResourceIdentifier} to a {@link Resource}. This is done by using
     * {@link ResourceResolverService} to find an implementation of {@link ResourceResolver} using Java's
     * {@link ServiceLoader} to find the implementation.
     *
     * @param listener The listener to call with any resolution problems
     * @param identifier The resource identifier
     * @return The resource
     */
    static Resource resolveResource(@NotNull Listener listener,
                                    @NotNull ResourceIdentifier identifier)
    {
        return listener.listenTo(resourceResolverService()).resolveResource(identifier);
    }

    /**
     * Resolves the given {@link ResourceIdentifier} to a {@link Resource}. This is done by using
     * {@link ResourceResolverService} to find an implementation of {@link ResourceResolver} using Java's
     * {@link ServiceLoader} to find the implementation.
     *
     * @param listener The listener to call with any resolution problems
     * @param resourcePath The resource identifier
     * @return The resource
     */
    static Resource resolveResource(@NotNull Listener listener,
                                    @NotNull ResourcePath resourcePath)
    {
        return resolveResource(listener, resourcePath.asString());
    }

    /**
     * Resolves the given {@link ResourceIdentifier} to a {@link Resource}. This is done by using
     * {@link ResourceResolverService} to find an implementation of {@link ResourceResolver} using Java's
     * {@link ServiceLoader} to find the implementation.
     *
     * @param listener The listener to call with any resolution problems
     * @param identifier The resource identifier
     * @return The resource
     */
    static Resource resolveResource(@NotNull Listener listener,
                                    @NotNull String identifier)
    {
        return resolveResource(listener, new ResourceIdentifier(identifier));
    }

    /**
     * Returns a resource argument parser builder
     *
     * @param listener The listener to call with any problems
     * @param description A description for the resource argument
     * @return The builder
     */
    static ArgumentParser.Builder<Resource> resourceArgumentParser(@NotNull Listener listener,
                                                                   @NotNull String description)
    {
        return argumentParser(Resource.class)
                .converter(new Resource.Converter(listener))
                .description(description);
    }

    /**
     * Returns a {@link ResourceIdentifier} for the given string
     *
     * @param identifier The identifier
     * @return The {@link ResourceIdentifier}
     */
    static ResourceIdentifier resourceIdentifier(@NotNull String identifier)
    {
        return new ResourceIdentifier(identifier);
    }

    /**
     * Returns a resource switch parser builder
     *
     * @param listener The listener to call with any problems
     * @param name The name of the switch
     * @param description A description of the switch
     * @return The builder
     */
    static SwitchParser.Builder<Resource> resourceSwitchParser(
            @NotNull Listener listener,
            @NotNull String name,
            @NotNull String description)
    {
        return switchParser(Resource.class)
                .name(name)
                .converter(new Resource.Converter(listener))
                .description(description);
    }

    /**
     * Represents the ability to do something with this resource (or not)
     */
    @CodeQuality(stability = STABLE_EXTENSIBLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE)
    enum Action
    {
        /** The resource can be renamed */
        RENAME,

        /** The resource can be deleted */
        DELETE
    }

    /**
     * Converts to and from {@link Resource}s by resolving {@link ResourceIdentifier}s.
     *
     * @author jonathanl (shibo)
     */
    @CodeQuality(stability = STABLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE)
    class Converter extends BaseStringConverter<Resource>
    {
        public Converter(@NotNull Listener listener)
        {
            super(listener, Resource.class);
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
     * Returns this resource as a {@link WritableResource}
     */
    default WritableResource asWritable()
    {
        return (WritableResource) this;
    }

    /**
     * Returns the set of optional operations that this resource can perform
     */
    default ObjectSet<Action> can()
    {
        return emptySet();
    }

    /**
     * Returns true if this resource can perform the given action
     *
     * @param action The action
     * @return True if this resource supports the action
     */
    default boolean can(Action action)
    {
        return can().contains(action);
    }

    /**
     * Returns any codec for compression / decompression
     */
    @UmlRelation(label = "uses")
    Codec codec();

    /**
     * Remove any materialized local copy if this is a remote resource that's been cached
     */
    default void dematerialize()
    {
    }

    default boolean endsWith(@NotNull String end)
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
     * Returns true if the resource exists
     */
    boolean exists();

    /**
     * Returns true if this resource has a parent
     */
    default boolean hasParent()
    {
        return parent() != null;
    }

    /**
     * Returns true if this file has no data in it
     */
    default boolean isEmpty()
    {
        return sizeInBytes().isZero();
    }

    /**
     * Returns true if this resource is on the local machine
     */
    default boolean isLocal()
    {
        return !isRemote();
    }

    /**
     * Returns true if this resource can be materialized (copied to a temporary file on the local filesystem for
     * access)
     */
    default boolean isMaterializable()
    {
        return isPackaged() || isRemote();
    }

    default boolean isOlderThan(@NotNull Resource that)
    {
        return lastModified().isOlderThan(that.lastModified());
    }

    /**
     * Returns true if this resource is in a JAR file
     */
    default boolean isPackaged()
    {
        return false;
    }

    /**
     * Returns true if this resource is on a remote filesystem
     */
    default boolean isRemote()
    {
        return false;
    }

    /**
     * Returns true if the given resource has the same last modified time and the same size
     */
    default boolean isSame(@NotNull Resource that)
    {
        assert lastModified() != null;
        assert sizeInBytes() != null;
        assert that.lastModified() != null;
        assert that.sizeInBytes() != null;

        return lastModified().equals(that.lastModified()) && sizeInBytes().equals(that.sizeInBytes());
    }

    /**
     * Returns a local cached copy of this resource if it is remote.
     */
    Resource materialized(@NotNull ProgressReporter reporter);

    /**
     * Returns the parent folder of this resource
     *
     * @return The parent
     */
    default ResourceFolder<?> parent()
    {
        return unsupported();
    }

    /**
     * StringPath to this resource
     */
    @Override
    ResourcePath path();

    /**
     * Returns this file with a path relative to the given folder
     */
    default Resource relativeTo(@NotNull ResourceFolder<?> folder)
    {
        return unsupported();
    }

    /**
     * Renames this resource to the given resource
     *
     * @param that The resource to rename to
     * @return True if renaming succeeded
     */
    default boolean renameTo(@NotNull Resource that)
    {
        return unsupported();
    }

    /**
     * {@inheritDoc}
     */
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
     * @throws IllegalStateException Thrown if the copy operation fails
     */
    default void safeCopyTo(@NotNull ResourceFolder<?> destination,
                            @NotNull WriteMode mode)
    {
        safeCopyTo(destination.resource(fileName()).asWritable(), mode, nullProgressReporter());
    }

    /**
     * Copies this readable resource to the given folder safely
     *
     * @param destination The file to copy to
     * @param mode Copying semantics
     * @throws IllegalStateException Thrown if the copy operation fails
     */
    default void safeCopyTo(@NotNull ResourceFolder<?> destination,
                            @NotNull WriteMode mode,
                            @NotNull ProgressReporter reporter)
    {
        safeCopyTo(destination.resource(fileName()).asWritable(), mode, reporter);
    }

    /**
     * Copies this resource to the given file safely (ensuring that a corrupted copy of the file never exists). This is
     * done by first copying to a temporary file in the same folder. If the copy operation is successful, the
     * destination file is then removed and the temporary file is renamed to the destination file's name.
     *
     * @param destination The file to copy to
     * @param mode Copying semantics
     * @throws IllegalStateException Thrown if the copy operation fails
     */
    default void safeCopyTo(@NotNull WritableResource destination,
                            @NotNull WriteMode mode)
    {
        safeCopyTo(destination, mode, nullProgressReporter());
    }

    /**
     * Copies this resource to the given file safely (ensuring that a corrupted copy of the file never exists). This is
     * done by first copying to a temporary file in the same folder. If the copy operation is successful, the
     * destination file is then removed and the temporary file is renamed to the destination file's name.
     *
     * @param target The file to copy to
     * @param mode Copying semantics
     * @param reporter Progress reporter to call as copy proceeds
     * @throws IllegalStateException Thrown if the copy operation fails
     */
    default void safeCopyTo(@NotNull WritableResource target,
                            @NotNull WriteMode mode,
                            @NotNull ProgressReporter reporter)
    {
        // If that we can do the copy operation,
        mode.ensureAllowed(this, target);

        // copy this resource to a temporary file,
        var temporary = listenTo(target.parent().temporaryFile(target.fileName()));
        ensure(target.can(DELETE));
        ensure(temporary.can(RENAME));
        copyTo(temporary, mode, reporter);

        // remove the destination file if it exists,
        if (target.exists())
        {
            ensure(target.delete(), "Could not delete $", target);
        }

        // and rename the temporary file to the destination file.
        ensure(temporary.renameTo(target), "Could not rename $ => $", temporary, target);
    }
}
