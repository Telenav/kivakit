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

package com.telenav.kivakit.core.resource.spi;

import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.resource.ResourceFolder;
import com.telenav.kivakit.core.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * {@link #resolve(ResourceFolderIdentifier)} iterates through implementations of the {@link ResourceFolderResolver}
 * interface provided by Java's {@link ServiceLoader} and resolves {@link ResourceFolderIdentifier}s by calling {@link
 * ResourceFolderResolver#accepts(ResourceFolderIdentifier)} until it reaches a resolver that recognizes the identifier.
 * It then returns the resolved resource with {@link ResourceFolderResolver#resolve(ResourceFolderIdentifier)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@LexakaiJavadoc(complete = true)
public class ResourceFolderResolverServiceLoader
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    @UmlAggregation
    private static List<ResourceFolderResolver> resolvers;

    public static ResourceFolder resolve(final ResourceFolderIdentifier identifier)
    {
        for (final var factory : resolvers())
        {
            if (factory.accepts(identifier))
            {
                return factory.resolve(identifier);
            }
        }

        return fail("Invalid resource identifier '$'", identifier);
    }

    private static synchronized List<ResourceFolderResolver> resolvers()
    {
        if (resolvers == null)
        {
            resolvers = new ArrayList<>();
            for (final var resolver : ServiceLoader.load(ResourceFolderResolver.class))
            {
                DEBUG.trace("Loaded resource factory '${class}'", resolver.getClass());
                resolvers.add(resolver);
            }
        }
        return resolvers;
    }
}
