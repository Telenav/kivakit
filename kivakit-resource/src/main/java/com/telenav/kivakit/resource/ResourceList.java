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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.iteration.Iterables.iterable;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.resource.Resource.resolveResource;

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
@TypeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTED,
             testing = UNTESTED)
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
        return resourceList(iterable(resources));
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

    @Override
    public ResourceList with(Collection<Resource> value)
    {
        return (ResourceList) super.with(value);
    }

    @Override
    public ResourceList with(Resource[] value)
    {
        return (ResourceList) super.with(value);
    }

    @Override
    public ResourceList with(Resource resource)
    {
        return (ResourceList) super.with(resource);
    }

    @Override
    public ResourceList without(Matcher<Resource> matcher)
    {
        return (ResourceList) super.without(matcher);
    }

    @Override
    public BaseResourceList<Resource> without(Filter<ResourcePathed> filter)
    {
        return super.without(filter);
    }

    @Override
    public ResourceList without(Resource[] that)
    {
        return (ResourceList) super.without(that);
    }

    @Override
    public ResourceList without(Collection<Resource> that)
    {
        return (ResourceList) super.without(that);
    }

    @Override
    public ResourceList without(Resource resource)
    {
        return (ResourceList) super.without(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Resource newResource(@NotNull ResourcePath path)
    {
        return resolveResource(throwingListener(), path);
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
