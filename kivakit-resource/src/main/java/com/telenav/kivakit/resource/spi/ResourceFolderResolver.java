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
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.ServiceLoader;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.ApiType.SERVICE_PROVIDER_API;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * A service provider interface that resolves {@link ResourceFolder}s (including {@link Package}s and {@link Folder}s).
 * If the {@link #accepts(ResourceFolderIdentifier)} method returns true, then this resolver can resolve the given
 * {@link ResourceFolderIdentifier} with a call to {@link #resolve(ResourceFolderIdentifier)}.
 * {@link ResourceFolderResolverService} iterates through implementations of this interface provided by Java's
 * {@link ServiceLoader} class and resolves {@link ResourceFolderIdentifier}s by calling
 * {@link #accepts(ResourceFolderIdentifier)} to find a resolver that can resolve the identifier.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@UmlRelation(label = "parses", referent = ResourceIdentifier.class)
@ApiQuality(stability = STABLE,
            documentation = FULLY_DOCUMENTED,
            testing = TESTING_NOT_NEEDED,
            type = SERVICE_PROVIDER_API)
public interface ResourceFolderResolver extends RepeaterMixin
{
    /**
     * @return True if this resource factory understands the given resource identifier
     */
    boolean accepts(ResourceFolderIdentifier identifier);

    /**
     * @return A new resource for the given resource identifier
     */
    @UmlRelation(label = "creates")
    ResourceFolder<?> resolve(ResourceFolderIdentifier identifier);
}
