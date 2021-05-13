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

package com.telenav.kivakit.resource.spi;

import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Debug;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * {@link #resolve(ResourceIdentifier)} iterates through implementations of the {@link ResourceResolver} interface
 * provided by Java's {@link ServiceLoader} and resolves {@link ResourceIdentifier}s by calling {@link
 * ResourceResolver#accepts(ResourceIdentifier)} until it reaches a resolver that recognizes the identifier. It then
 * returns the resolved resource with {@link ResourceResolver#resolve(ResourceIdentifier)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
public class ResourceResolverServiceLoader
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    @UmlAggregation
    private static List<ResourceResolver> resolvers;

    public static Resource resolve(final ResourceIdentifier identifier)
    {
        for (final var resolver : resolvers())
        {
            if (resolver.accepts(identifier))
            {
                return resolver.resolve(identifier);
            }
        }

        return fail("Invalid resource identifier '$'", identifier);
    }

    private static synchronized List<ResourceResolver> resolvers()
    {
        if (resolvers == null)
        {
            resolvers = new ArrayList<>();
            for (final var service : ServiceLoader.load(ResourceResolver.class))
            {
                DEBUG.trace("Loaded resource factory '${class}'", service.getClass());
                resolvers.add(service);
            }
        }
        return resolvers;
    }
}
