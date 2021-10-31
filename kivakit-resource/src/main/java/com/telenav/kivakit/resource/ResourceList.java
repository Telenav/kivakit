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

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A list of {@link Resource}s. Adds the methods:
 *
 * <ul>
 *     <li>{@link #largest()} - The largest resource in this list</li>
 *     <li>{@link #matching(Extension)} - All resources with the given extension</li>
 *     <li>{@link #matching(Matcher)} - All matching resources</li>
 *     <li>{@link #smallest()} - The smallest resource in this list</li>
 *     <li>{@link #totalSize()} - The total size of the resources in this list</li>
 * </ul>
 */
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlRelation(label = "contains", referent = Resource.class)
@LexakaiJavadoc(complete = true)
public class ResourceList extends ArrayList<Resource>
{
    /**
     * Converts lists of resource identifiers to and from {@link ResourceList}s
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<ResourceList>
    {
        private final Extension extension;

        public Converter(Listener listener, Extension extension)
        {
            super(listener);
            this.extension = extension;
        }

        @Override
        protected ResourceList onToValue(String value)
        {
            var resources = new ResourceList(extension);
            for (var path : value.split(","))
            {
                var resource = Resource.resolve(this, path);
                if (resource instanceof File)
                {
                    var file = (File) resource;
                    if (file.isFolder())
                    {
                        resources.addAll(file.asFolder().nestedFiles(extension.fileMatcher()));
                    }
                    else
                    {
                        resources.add(file);
                    }
                }
                else
                {
                    resources.add(resource);
                }
            }
            return resources;
        }
    }

    private final Extension extension;

    public ResourceList()
    {
        extension = null;
    }

    public ResourceList(Extension extension)
    {
        this.extension = extension;
    }

    public ResourceList(Iterable<Resource> resources)
    {
        extension = null;
        for (var resource : resources)
        {
            add(resource);
        }
    }

    @Override
    public void add(int index, Resource resource)
    {
        if (accepts(resource.fileName()))
        {
            super.add(index, resource);
        }
    }

    @Override
    public boolean add(Resource resource)
    {
        if (accepts(resource.fileName()))
        {
            return super.add(resource);
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Resource> collection)
    {
        for (Resource file : collection)
        {
            add(file);
        }
        return true;
    }

    public Count count()
    {
        return Count.count(size());
    }

    public Resource largest()
    {
        Resource largest = null;
        for (var resource : this)
        {
            if (largest == null || resource.isLargerThan(largest))
            {
                largest = resource;
            }
        }
        return largest;
    }

    public ResourceList matching(Extension extension)
    {
        return matching(resource ->
        {
            var fileExtension = resource.compoundExtension();
            return fileExtension != null && fileExtension.endsWith(extension);
        });
    }

    public ResourceList matching(Matcher<Resource> matcher)
    {
        var matches = new ResourceList();
        for (var resource : this)
        {
            if (matcher.matches(resource))
            {
                matches.add(resource);
            }
        }
        return matches;
    }

    public ResourceList relativeTo(Folder folder)
    {
        var resources = new ResourceList();
        for (var resource : this)
        {
            resources.add(resource.path().asFile().relativeTo(folder));
        }
        return resources;
    }

    @Override
    public Resource set(int index, Resource resource)
    {
        if (accepts(resource.fileName()))
        {
            return super.set(index, resource);
        }
        return null;
    }

    public Resource smallest()
    {
        Resource smallest = null;
        for (var resource : this)
        {
            if (smallest == null || resource.isSmallerThan(smallest))
            {
                smallest = resource;
            }
        }
        return smallest;
    }

    public void sortOldestToNewest()
    {
        sort((a, b) ->
        {
            if (a.wasChangedBefore(b))
            {
                return -1;
            }
            if (b.wasChangedBefore(a))
            {
                return 1;
            }
            return 0;
        });
    }

    public Bytes totalSize()
    {
        var bytes = Bytes._0;
        for (var resource : this)
        {
            bytes = bytes.add(resource.sizeInBytes());
        }
        return bytes;
    }

    private boolean accepts(FileName name)
    {
        return extension == null || name.endsWith(extension);
    }
}
