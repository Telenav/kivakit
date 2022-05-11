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

import com.telenav.kivakit.core.collections.iteration.Iterables;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.lexakai.DiagramResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

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
public class ResourceList extends BaseResourceList<Resource>
{
    public static ResourceList resourceList(Iterable<? extends Resource> resources)
    {
        return new ResourceList(resources);
    }

    public static ResourceList resourceList(Resource... resources)
    {
        return resourceList(Iterables.iterable(resources));
    }

    public ResourceList()
    {
    }

    public ResourceList(Iterable<? extends Resource> resources)
    {
        for (var resource : resources)
        {
            add(resource);
        }
    }

    @Override
    protected Resource newResource(final ResourcePath path)
    {
        return Resource.resolve(Listener.throwingListener(), path);
    }

    @Override
    protected ResourceList newResourceList()
    {
        return new ResourceList();
    }
}
