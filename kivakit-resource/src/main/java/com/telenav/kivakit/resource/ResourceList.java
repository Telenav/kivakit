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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.iteration.Iterables;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_DEFAULT_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

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
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlRelation(label = "contains", referent = Resource.class)
@ApiQuality(stability = API_STABLE_DEFAULT_EXTENSIBLE,
            documentation = DOCUMENTATION_COMPLETE,
            testing = TESTING_NONE)
public class ResourceList extends BaseResourceList<Resource>
{
    /**
     * Returns a resource list fo the given resources
     */
    public static ResourceList resourceList(@NotNull Iterable<? extends Resource> resources)
    {
        return new ResourceList(resources);
    }

    /**
     * Returns a resource list fo the given resources
     */
    public static ResourceList resourceList(@NotNull Resource... resources)
    {
        return resourceList(Iterables.iterable(resources));
    }

    public ResourceList()
    {
    }

    public ResourceList(@NotNull Iterable<? extends Resource> resources)
    {
        for (var resource : resources)
        {
            add(resource);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Resource newResource(@NotNull ResourcePath path)
    {
        return Resource.resolveResource(Listener.throwingListener(), path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ResourceList newResourceList()
    {
        return new ResourceList();
    }
}
