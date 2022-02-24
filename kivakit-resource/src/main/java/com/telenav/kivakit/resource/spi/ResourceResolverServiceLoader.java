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

package com.telenav.kivakit.resource.spi;

import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Resolves {@link ResourceIdentifier}s into {@link Resource}s.
 *
 * <p>
 * The method {@link #resolve(ResourceIdentifier)} iterates through implementations of the {@link ResourceResolver}
 * interface located by Java's {@link ServiceLoader}, until a resolver is found that can accept the identifier, as
 * determined by {@link ResourceResolver#accepts(ResourceIdentifier)}. This resolver is then used to resolve the
 * resource with {@link ResourceResolver#resolve(ResourceIdentifier)}.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
public class ResourceResolverServiceLoader extends BaseRepeater
{
    private static final ResourceResolverServiceLoader instance = new ResourceResolverServiceLoader();

    public static ResourceResolverServiceLoader get()
    {
        return instance;
    }

    @UmlAggregation
    private List<ResourceResolver> resolvers;

    private ResourceResolverServiceLoader()
    {
    }

    /**
     * Resolves the given {@link ResourceIdentifier} to a {@link Resource} by using {@link ServiceLoader} to locate
     * {@link ResourceResolver}s. The method {@link ResourceResolver#accepts(ResourceIdentifier)} is called on each
     * resolver until a resolver is found that can accept the identifier. Then this resolver is used to resolve the
     * identifier into a resource.
     *
     * @param identifier The resource identifier
     * @return The resolved resource
     */
    public Resource resolve(ResourceIdentifier identifier)
    {
        for (var resolver : resolvers())
        {
            if (resolver.accepts(identifier))
            {
                return resolver.resolve(identifier);
            }
        }

        throw problem("Invalid resource identifier '$'", identifier).asException();
    }

    /**
     * Locates all {@link ResourceResolver}s on the classpath using {@link ServiceLoader}.
     *
     * @return The list of all resolvers
     */
    private synchronized List<ResourceResolver> resolvers()
    {
        if (resolvers == null)
        {
            resolvers = new ArrayList<>();
            for (var service : ServiceLoader.load(ResourceResolver.class))
            {
                trace("Loaded resource factory '${class}'", service.getClass());
                resolvers.add(listenTo(service));
            }
        }

        return resolvers;
    }
}
