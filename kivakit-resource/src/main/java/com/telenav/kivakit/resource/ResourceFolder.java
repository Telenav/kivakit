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
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.comparison.Matchable;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.packages.Package;
import com.telenav.kivakit.resource.spi.ResourceFolderResolverServiceLoader;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.filesystem.Folder.Type.NORMAL;
import static com.telenav.kivakit.resource.CopyMode.OVERWRITE;

/**
 * A resource container is an abstraction that provides access to hierarchical resources, independent of implementation.
 * {@link Folder} is a {@link ResourceFolder}, but {@link Package is also a resource container.
 *
 * @author jonathanl (shibo)
 */
public interface ResourceFolder<T extends ResourceFolder<T>> extends
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
    default ResourceList nestedResources(Matcher<T> matcher)
    {
        var list = new ResourceList();
        list.addAll(resources());
        for (var at : folders())
        {
            at.nestedResources(matcher);
        }
        return list;
    }


    ResourceFolder<?> parent();

    void renameTo(ResourceFolder<?> folder);

    ResourceFolder<?> newFolder(ResourcePath relativePath);

    default ResourcePath relativePath(ResourceFolder<?> folder)
    {
        return absolute().relativePath(folder.absolute());
    }

    default ResourceFolder<?> relativeTo(ResourceFolder<?> folder)
    {
        return newFolder(relativePath(folder));
    }

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
    Resource resource(ResourcePathed path);

    /**
     * @return The resource of the given in this container
     */
    default Resource resource(String name)
    {
        return resource(FileName.parseFileName(Listener.throwing(), name));
    }

    /**
     * @return The resources in this folder matching the given matcher
     */
    ResourceList resources(Matcher<? super Resource> matcher);

    /**
     * @return The resources in this folder
     */
    default ResourceList resources()
    {
        return resources(Matcher.matchAll());
    }

    /**
     * Copy the resources in this package to the given folder
     */
    default void safeCopyTo(ResourceFolder<?> folder, CopyMode mode, ProgressReporter reporter)
    {
        for (var at : resources())
        {
            var destination = folder.mkdirs().resource(at.fileName()).asWritable();
            if (mode.canCopy(at, destination))
            {
                at.safeCopyTo(destination, mode, reporter);
            }
        }
    }

    default Resource temporary(FileName baseName)
    {
        return temporary(baseName, Extension.TMP);
    }

    File temporary(FileName baseName, Extension extension);

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
