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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.ApiType.PRIVATE_API;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * <b>Not public API</b>
 * <p>
 * {@link #resolveResourceFolder(ResourceFolderIdentifier)} iterates through implementations of the
 * {@link ResourceFolderResolver} interface provided by Java's {@link ServiceLoader} and resolves
 * {@link ResourceFolderIdentifier}s by calling {@link ResourceFolderResolver#accepts(ResourceFolderIdentifier)} until
 * it reaches a resolver that recognizes the identifier. It then returns the resolved resource with
 * {@link ResourceFolderResolver#resolve(ResourceFolderIdentifier)}.
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #resourceFolderResolverService()}</li>
 * </ul>
 *
 * <p><b>Resolution</b></p>
 *
 * <ul>
 *     <li>{@link #resolveResourceFolder(ResourceFolderIdentifier)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramResourceService.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            documentation = FULLY_DOCUMENTED,
            testing = UNTESTED,
            type = PRIVATE_API)
public class ResourceFolderResolverService extends BaseRepeater
{
    @UmlAggregation
    private static ObjectList<ResourceFolderResolver> resolvers;

    /**
     * Returns an instance of this class
     */
    public static ResourceFolderResolverService resourceFolderResolverService()
    {
        return new ResourceFolderResolverService();
    }

    /**
     * Resolves the given resource identifier to a resource folder
     *
     * @param identifier The resource folder identifier to resolve
     * @return The resource folder
     */
    public ResourceFolder<?> resolveResourceFolder(@NotNull ResourceFolderIdentifier identifier)
    {
        for (var factory : resourceFolderResolvers())
        {
            if (factory.accepts(identifier))
            {
                return factory.resolve(identifier);
            }
        }

        problem("Invalid resource identifier '$'", identifier);
        return null;
    }

    /**
     * Locates all {@link ResourceFolderResolver}s on the classpath using {@link ServiceLoader}.
     *
     * @return The list of all resource folder resolvers
     */
    private synchronized ObjectList<ResourceFolderResolver> resourceFolderResolvers()
    {
        if (resolvers == null)
        {
            resolvers = new ObjectList<>();
            for (var resolver : ServiceLoader.load(ResourceFolderResolver.class))
            {
                trace("Loaded resource folder resolver: ${class}", resolver.getClass());
                resolvers.add(resolver);
            }
        }
        return resolvers;
    }
}
