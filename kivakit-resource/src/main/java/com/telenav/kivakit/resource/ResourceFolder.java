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

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.comparison.Matchable;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.packages.Package;
import com.telenav.kivakit.resource.spi.ResourceFolderResolverServiceLoader;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.filesystem.Folder.Type.NORMAL;
import static com.telenav.kivakit.interfaces.comparison.Matcher.matchAll;
import static com.telenav.kivakit.resource.CopyMode.OVERWRITE;
import static com.telenav.kivakit.resource.ResourcePath.parseResourcePath;

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
public interface ResourceFolder<T extends ResourceFolder<T>> extends
        Repeater,
        UriIdentified,
        ResourcePathed,
        Matchable<ResourcePathed>
{
    static ResourceFolderIdentifier identifier(String identifier)
    {
        return new ResourceFolderIdentifier(identifier);
    }

    static ResourceFolder<?> resolve(Listener listener, String identifier)
    {
        return resolve(listener, new ResourceFolderIdentifier(identifier));
    }

    static ResourceFolder<?> resolve(Listener listener, ResourceFolderIdentifier identifier)
    {
        return ResourceFolderResolverServiceLoader.resolve(listener, identifier);
    }

    /**
     * Converts to and from {@link ResourceFolder}s by resolving strings via {@link ResourceFolderIdentifier}s.
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
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
        return newFolder(path().absolute()).withTrailingSlash();
    }

    default boolean contains(ResourcePathed that)
    {
        return that.path().startsWith(path());
    }

    /**
     * Copies all nested resources matching the given matcher from this folder to the destination folder.
     */
    default void copyTo(Folder destination,
                        CopyMode mode,
                        Matcher<Resource> matcher,
                        ProgressReporter reporter)
    {
        var start = Time.now();

        // Ensure the destination folder exists,
        information("Copying $ to $", this, destination);
        destination.ensureExists();

        // then for each nested file,
        for (var file : nestedResources(matcher))
        {
            // make relative target file,
            var target = destination.file(file.relativeTo(this));

            // and if we can copy to it,
            if (mode.canCopy(file, target))
            {
                // then copy the file and update its last modified timestamp to the source timestamp
                file.copyTo(target.ensureWritable(), mode, reporter);
                target.lastModified(file.modifiedAt());
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
     * @return The child resource container at the given relative path
     */
    T folder(String path);

    List<T> folders();

    default boolean hasTrailingSlash()
    {
        return path().hasTrailingSlash();
    }

    ResourceFolderIdentifier identifier();

    default boolean isEmpty()
    {
        return folders().isEmpty() && resources().isEmpty();
    }

    boolean isMaterialized();

    default boolean isWritable()
    {
        return unsupported();
    }

    default Matcher<ResourcePathed> matchAllIn()
    {
        return resource -> path().equals(resource.path().parent());
    }

    default Matcher<ResourcePathed> matchAllUnder()
    {
        return this::contains;
    }

    @Override
    default Matcher<ResourcePathed> matcher()
    {
        return matchAllUnder();
    }

    default Folder materialize()
    {
        return materializeTo(Folder.temporaryForProcess(NORMAL));
    }

    default Folder materializeTo(Folder folder)
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

    @SuppressWarnings("SpellCheckingInspection")
    default ResourceFolder<?> mkdirs()
    {
        unsupported();
        return this;
    }

    default List<T> nestedFolders(Matcher<T> matcher)
    {
        var folders = new ArrayList<T>();
        for (var at : folders())
        {
            folders.add(at);
            folders.addAll(at.nestedFolders(matcher));
        }
        return folders;
    }

    default ResourceList nestedResources()
    {
        return nestedResources(value -> true);
    }

    /**
     * @return Any matching files that are recursively contained in this folder
     */
    default ResourceList nestedResources(Matcher<Resource> matcher)
    {
        var list = new ResourceList();
        list.addAll(resources());
        for (var at : folders())
        {
            at.nestedResources(matcher);
        }
        return list;
    }

    ResourceFolder<?> newFolder(ResourcePath relativePath);

    ResourceFolder<?> parent();

    default ResourceFolder<?> relativeTo(ResourceFolder<?> folder)
    {
        var relativePath = absolute().path().relativeTo(folder.absolute().path());
        return newFolder(relativePath);
    }

    boolean renameTo(ResourceFolder<?> folder);

    /**
     * @return The resource of the given in this container
     */
    default Resource resource(FileName name)
    {
        return resource(name.asPath());
    }

    /**
     * @return The resource of the given in this container
     */
    default Resource resource(String name)
    {
        return resource(parseResourcePath(Listener.throwingListener(), name));
    }

    /**
     * @return The resource of the given in this container
     */
    Resource resource(ResourcePathed name);

    /**
     * @return The resources in this folder matching the given matcher
     */
    ResourceList resources(Matcher<Resource> matcher);

    /**
     * @return The resources in this folder
     */
    default ResourceList resources()
    {
        return resources(matchAll());
    }

    /**
     * Copy the resources in this package to the given folder
     */
    default void safeCopyTo(ResourceFolder<?> folder, CopyMode mode, ProgressReporter reporter)
    {
        safeCopyTo(folder, mode, matchAll(), reporter);
    }

    /**
     * Copy the resources in this package to the given folder
     */
    default void safeCopyTo(ResourceFolder<?> folder,
                            CopyMode mode,
                            Matcher<Resource> matcher,
                            ProgressReporter reporter)
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

    default WritableResource temporaryFile(FileName baseName)
    {
        return temporaryFile(baseName, Extension.TMP);
    }

    WritableResource temporaryFile(FileName baseName, Extension extension);

    default ResourceFolder<?> temporaryFolder(FileName baseName)
    {
        return unsupported();
    }

    default ResourceFolder<?> withTrailingSlash()
    {
        if (hasTrailingSlash())
        {
            return this;
        }
        return newFolder(path().withChild(""));
    }
}
