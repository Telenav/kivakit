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
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.comparison.Matchable;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.packages.Package;
import com.telenav.kivakit.resource.writing.WritableResource;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.time.Time.now;
import static com.telenav.kivakit.filesystem.Folder.FolderType.NORMAL;
import static com.telenav.kivakit.filesystem.Folder.temporaryFolderForProcess;
import static com.telenav.kivakit.interfaces.comparison.Matcher.matchAll;
import static com.telenav.kivakit.resource.CopyMode.OVERWRITE;
import static com.telenav.kivakit.resource.Extension.TEMPORARY;
import static com.telenav.kivakit.resource.ResourcePath.parseResourcePath;
import static com.telenav.kivakit.resource.spi.ResourceFolderResolverService.resourceFolderResolverService;

/**
 * A resource container is an abstraction that provides access to hierarchical resources, independent of implementation.
 * {@link Folder} is a {@link ResourceFolder}, but {@link Package} is also a resource folder.
 *
 * <p><b>Contents</b></p>
 *
 * <ul>
 *     <li>{@link #resources()} - The resources in this folder</li>
 *     <li>{@link #resources(Matcher)} - The matching files in this folder</li>
 *     <li>{@link #folders()} - The folders in this folder</li>
 *     <li>{@link #resources(Matcher)} - The matching folders in this folder</li>
 *     <li>{@link #nestedResources(Matcher)} ()} - All nested files in this folder</li>
 *     <li>{@link #nestedResources(Matcher)} - All matching nested files in this folder</li>
 *     <li>{@link #nestedFolders(Matcher)} - All matching nested folders under this folder</li>
 *     <li>{@link #temporaryFile(FileName)} - A temporary file in this folder with the given name</li>
 *     <li>{@link #temporaryFile(FileName, Extension)} - A temporary file in this folder with the given name and extension</li>
 *     <li>{@link #temporaryFolder(FileName)} - A temporary sub-folder with the given name</li>
 * </ul>
 *
 * <p><b>Hierarchy</b></p>
 *
 * <ul>
 *     <li>{@link #absolute()} - This folder with an absolute path</li>
 *     <li>{@link #path()} - The path to this folder</li>
 *     <li>{@link #parent()} - The parent folder, or null if there is none</li>
 *     <li>{@link #relativeTo(ResourceFolder)} - This folder with a path relative to the given folder</li>
 *     <li>{@link #resource(FileName)} - The file with the given name in this folder</li>
 *     <li>{@link #folder(String)} - The folder with the given name in this folder</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #delete()} - Deletes this folder if it is empty</li>
 *     <li>{@link #mkdirs()} - Creates this folder and any required parent folders</li>
 *     <li>{@link #renameTo(ResourceFolder)} - Renames this folder to the given folder</li>
 * </ul>
 *
 * <p><b>Checks</b></p>
 *
 * <ul>
 *     <li>{@link #exists()} - True if this folder exists</li>
 *     <li>{@link #isEmpty()} - True if this folder is empty</li>
 *     <li>{@link #isMaterialized()} - True if this folder exists locally</li>
 *     <li>{@link #isWritable()} - True if this folder can be written to</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asJavaFile()} - This folder as a {@link java.io.File}</li>
 *     <li>{@link #absolute()} - This folder with an absolute path</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface ResourceFolder<T extends ResourceFolder<T>> extends
        Repeater,
        UriIdentified,
        ResourcePathed,
        Matchable<ResourcePathed>
{
    static ResourceFolder<?> resolveResourceFolder(@NotNull Listener listener,
                                                   @NotNull String identifier)
    {
        return resolveResourceFolder(listener, new ResourceFolderIdentifier(identifier));
    }

    static ResourceFolder<?> resolveResourceFolder(@NotNull Listener listener,
                                                   @NotNull ResourceFolderIdentifier identifier)
    {
        return listener.listenTo(resourceFolderResolverService()).resolveResourceFolder(identifier);
    }

    static ResourceFolderIdentifier resourceFolderIdentifier(@NotNull String identifier)
    {
        return new ResourceFolderIdentifier(identifier);
    }

    /**
     * Converts to and from {@link ResourceFolder}s by resolving strings via {@link ResourceFolderIdentifier}s.
     *
     * @author jonathanl (shibo)
     */
    @CodeQuality(stability = STABLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE)
    class Converter extends BaseStringConverter<ResourceFolder<?>>
    {
        public Converter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected ResourceFolder<?> onToValue(String value)
        {
            return new ResourceFolderIdentifier(value).resolve(this);
        }
    }

    /**
     * This folder as an absolute path with a trailing slash on it
     */
    default ResourceFolder<?> absolute()
    {
        return newFolder(path().asAbsolute()).withTrailingSlash();
    }

    default boolean contains(@NotNull ResourcePathed that)
    {
        return that.path().startsWith(path());
    }

    /**
     * Copies all nested resources matching the given matcher from this folder to the destination folder.
     */
    default void copyTo(@NotNull Folder destination,
                        @NotNull CopyMode mode,
                        @NotNull Matcher<ResourcePathed> matcher,
                        @NotNull ProgressReporter reporter)
    {
        var start = now();

        // Ensure the destination folder exists,
        information("Copying $ to $", this, destination);
        destination.ensureExists();

        // then for each nested file,
        for (var resource : nestedResources(matcher))
        {
            // make relative target resource,
            var target = destination.file(resource.relativeTo(this));

            // and if we can copy to it,
            if (mode.canCopy(resource, target))
            {
                // then copy the resource and update its last modified timestamp to the source timestamp
                information("Copying $ to $", resource, target);
                listenTo(resource).copyTo(target.ensureWritable(), mode, reporter);
                target.lastModified(resource.lastModified());
            }
        }
        information("Copy completed in $", start.elapsedSince());
    }

    /**
     * Copies all nested files from this folder to the destination folder
     */
    default void copyTo(Folder destination, CopyMode mode, ProgressReporter reporter)
    {
        copyTo(destination, mode, matchAll(), reporter);
    }

    boolean delete();

    boolean exists();

    /**
     * Returns the child resource container at the given relative path
     */
    T folder(String path);

    ObjectList<T> folders();

    /**
     * Returns true if the path for this resource folder has a trailing slash
     */
    default boolean hasTrailingSlash()
    {
        return path().hasTrailingSlash();
    }

    /**
     * Returns true if there's nothing in this folder
     */
    default boolean isEmpty()
    {
        return folders().isEmpty() && resources().isEmpty();
    }

    /**
     * Returns true if this folder is materialized
     */
    boolean isMaterialized();

    /**
     * Returns true if this folder can be written to
     */
    default boolean isWritable()
    {
        return unsupported();
    }

    /**
     * Returns a matcher that matches all resource paths in this folder
     */
    default Matcher<ResourcePathed> matchAllPathsIn()
    {
        return resource -> path().equals(resource.path().parent());
    }

    /**
     * Returns a matcher that matches all resource paths under this folder
     */
    default Matcher<ResourcePathed> matchAllPathsUnder()
    {
        return this::contains;
    }

    @Override
    default Matcher<ResourcePathed> matcher()
    {
        return matchAllPathsUnder();
    }

    /**
     * Creates a local copy of this folder for efficiency and random access
     */
    default Folder materialize()
    {
        return materializeTo(temporaryFolderForProcess(NORMAL));
    }

    /**
     * Materializes this folder to the given folder
     */
    default Folder materializeTo(@NotNull Folder folder)
    {
        if (!isMaterialized())
        {
            folder.mkdirs().clearAll();
            for (var resource : resources())
            {
                var destination = folder.file(resource.fileName());
                resource.safeCopyTo(destination, OVERWRITE);
            }
        }
        return folder;
    }

    /**
     * Creates all folders above and including this folder
     */
    @SuppressWarnings("SpellCheckingInspection")
    default ResourceFolder<?> mkdirs()
    {
        unsupported();
        return this;
    }

    /**
     * Returns the list of folders that match the given matcher
     *
     * @param matcher The matcher
     * @return The matching folders
     */
    default ObjectList<T> nestedFolders(@NotNull Matcher<T> matcher)
    {
        var folders = new ObjectList<T>();
        for (var at : folders())
        {
            folders.add(at);
            folders.addAll(at.nestedFolders(matcher));
        }
        return folders;
    }

    /**
     * Returns all nested resources
     */
    default ResourceList nestedResources()
    {
        return nestedResources(value -> true);
    }

    /**
     * Returns any matching files that are recursively contained in this folder
     */
    default ResourceList nestedResources(@NotNull Matcher<ResourcePathed> matcher)
    {
        var list = new ResourceList();
        list.addAll(resources());
        for (var at : folders())
        {
            if (!equals(at))
            {
                list.addAll(at.nestedResources(matcher));
            }
        }
        return list;
    }

    ResourceFolder<?> newFolder(@NotNull ResourcePath relativePath);

    /**
     * Returns the parent folder of this folder
     */
    ResourceFolder<?> parent();

    default ResourceFolder<?> relativeTo(@NotNull ResourceFolder<?> folder)
    {
        var relativePath = absolute().path().relativeTo(folder.absolute().path());
        return newFolder(relativePath);
    }

    /**
     * Renames this folder to the given folder
     */
    boolean renameTo(@NotNull ResourceFolder<?> folder);

    /**
     * Returns the resource of the given in this container
     */
    default Resource resource(@NotNull FileName name)
    {
        return resource(name.asPath());
    }

    /**
     * Returns the resource of the given in this container
     */
    default Resource resource(@NotNull String name)
    {
        return resource(parseResourcePath(throwingListener(), name));
    }

    /**
     * Returns the resource of the given in this container
     */
    Resource resource(@NotNull ResourcePathed name);

    /**
     * Returns the storage-independent identifier for this folder
     */
    ResourceFolderIdentifier resourceFolderIdentifier();

    /**
     * Returns the resources in this folder matching the given matcher
     */
    ResourceList resources(@NotNull Matcher<ResourcePathed> matcher);

    /**
     * Returns the resources in this folder
     */
    default ResourceList resources()
    {
        return resources(matchAll());
    }

    /**
     * Copies the resources in this package to the given folder
     */
    default void safeCopyTo(@NotNull ResourceFolder<?> folder,
                            @NotNull CopyMode mode,
                            @NotNull ProgressReporter reporter)
    {
        safeCopyTo(folder, mode, matchAll(), reporter);
    }

    /**
     * Copies the resources in this package to the given folder
     */
    default void safeCopyTo(@NotNull ResourceFolder<?> folder,
                            @NotNull CopyMode mode,
                            @NotNull Matcher<ResourcePathed> matcher,
                            @NotNull ProgressReporter reporter)
    {
        for (var at : resources(matcher))
        {
            var destination = folder.mkdirs().resource(at.fileName());
            if (mode.canCopy(at, destination))
            {
                at.safeCopyTo(destination.asWritable(), mode, reporter);
            }
        }
    }

    /**
     * Returns a temporary file with the given base filename
     *
     * @param baseName The base filename
     * @return The writable file
     */
    default WritableResource temporaryFile(@NotNull FileName baseName)
    {
        return temporaryFile(baseName, TEMPORARY);
    }

    /**
     * Returns a temporary file with the given base filename and extension
     *
     * @param baseName The base filename
     * @param extension The extension
     * @return The writable file
     */
    WritableResource temporaryFile(@NotNull FileName baseName,
                                   @NotNull Extension extension);

    /**
     * Returns a temporary folder with the given base name
     *
     * @param baseName The base name
     * @return The temporary folder
     */
    default ResourceFolder<?> temporaryFolder(@NotNull FileName baseName)
    {
        return unsupported();
    }

    /**
     * Returns this folder with a trailing slash
     */
    default ResourceFolder<?> withTrailingSlash()
    {
        if (hasTrailingSlash())
        {
            return this;
        }
        return newFolder(path().withChild(""));
    }
}
