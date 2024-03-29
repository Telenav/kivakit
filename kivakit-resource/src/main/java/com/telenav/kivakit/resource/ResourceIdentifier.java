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
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceService;
import com.telenav.kivakit.resource.spi.ResourceResolver;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.resource.Resource.resolveResource;

/**
 * An arbitrary string that identifies a resource.
 *
 * <p>
 * A resource identifier might be a path or package name or some other identifier that can be resolved into a resource.
 * The {@link #resolve(Listener)} method searches available {@link ResourceResolver}s to find one that accepts this
 * identifier. It then returns the resource, as resolved by the resolver.
 * </p>
 *
 * <p><b>Resource Resolvers</b></p>
 * <ul>
 *    <li>PackageResource.PackageResourceResolver - Resolves resources for the scheme <i>classpath:</i></li>
 *    <li>File.FileResourceResolver - Resolves filesystem resource with various schemes. Resources on the local filesystem
 *                        are identifier with <i>file:</i> or by not specifying any scheme</li>
 *    <li>HttpGetResourceResolver - Resolves <i>https:</i> and <i>http:</i> resources</li>
 * </ul>
 *
 * @param identifier The storage-agnostic identifier
 * @author jonathanl (shibo)
 * @see Resource#resolveResource(Listener, String)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public record ResourceIdentifier(String identifier)
{
    /**
     * Returns the identifier
     */
    @Override
    public String identifier()
    {
        return identifier;
    }

    /**
     * Resolves this resource identifier to a {@link Resource}
     *
     * @param listener The listener to call with any problems during resolution
     * @return The {@link Resource} that this identifier identifies
     */
    public Resource resolve(@NotNull Listener listener)
    {
        return resolveResource(listener, this);
    }

    @Override
    public String toString()
    {
        return identifier;
    }
}
