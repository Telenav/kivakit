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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.CodeType.CODE_INTERNAL;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Resolves {@link ResourceIdentifier}s into {@link Resource}s. This is the service provider interface for resource
 * resolution. For the user API, see {@link Resource#resolveResource(Listener, String)},
 * {@link Resource#resolveResource(Listener, ResourceIdentifier)}, and
 * {@link Resource#resolveResource(Listener, ResourcePath)}.
 * </p>
 *
 * <p>
 * The method {@link #resolveResource(ResourceIdentifier)} iterates through implementations of the
 * {@link ResourceResolver} interface located by Java's {@link ServiceLoader}, until a resolver is found that can accept
 * the identifier, as determined by {@link ResourceResolver#accepts(ResourceIdentifier)}. This resolver is then used to
 * resolve the resource with {@link ResourceResolver#resolve(ResourceIdentifier)}.
 * </p>
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #resourceResolverService()}</li>
 * </ul>
 *
 * <p><b>Resolution</b></p>
 *
 * <ul>
 *     <li>{@link #resolveResource(ResourceIdentifier)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramResourceService.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             documentation = DOCUMENTATION_COMPLETE,
             testing = TESTING_NONE,
             type = CODE_INTERNAL)
public class ResourceResolverService extends BaseRepeater
{
    /**
     * Returns an instance of this class
     */
    public static ResourceResolverService resourceResolverService()
    {
        return new ResourceResolverService();
    }

    /** The resource resolvers to try when resolving a resource */
    @UmlAggregation
    private ObjectList<ResourceResolver> resolvers;

    private ResourceResolverService()
    {
    }

    /**
     * Resolves the given {@link ResourceIdentifier} to a {@link Resource} by using {@link ServiceLoader} to locate
     * {@link ResourceResolver}s. The method {@link ResourceResolver#accepts(ResourceIdentifier)} is called on each
     * resolver until a resolver is found that can accept the identifier. Then this resolver is used to resolve the
     * identifier into a resource.
     *
     * @param identifier The resource identifier to resolve
     * @return The resolved resource
     */
    public Resource resolveResource(@NotNull ResourceIdentifier identifier)
    {
        for (var resolver : resourceResolvers())
        {
            if (resolver.accepts(identifier))
            {
                return resolver.resolve(identifier);
            }
        }

        problem("Invalid resource identifier '$'", identifier);
        return null;
    }

    /**
     * Locates all {@link ResourceResolver}s on the classpath using {@link ServiceLoader}.
     *
     * @return The list of all resource resolvers
     */
    private synchronized ObjectList<ResourceResolver> resourceResolvers()
    {
        if (resolvers == null)
        {
            resolvers = new ObjectList<>();
            for (var service : ServiceLoader.load(ResourceResolver.class))
            {
                trace("Loaded resource resolver: ${class}", service.getClass());
                resolvers.add(listenTo(service));
            }
        }

        return resolvers;
    }
}
