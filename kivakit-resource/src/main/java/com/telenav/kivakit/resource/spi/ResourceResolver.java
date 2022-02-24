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

import com.telenav.kivakit.kernel.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

/**
 * Service provider interface that resolves resource identifiers to resources. If the resolver returns true from {@link
 * #accepts(ResourceIdentifier)}, then the resource identifier can be resolved to a {@link Resource} by {@link
 * #resolve(ResourceIdentifier)}. Resource resolution permits a very broad abstraction to be used when denoting
 * resources with strings. In particular, it is possible to accept an arbitrary string, like a command line switch, and
 * convert this value into a {@link Resource} that can subsequently be read.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@UmlRelation(label = "parses", referent = ResourceFolderIdentifier.class)
@LexakaiJavadoc(complete = true)
public interface ResourceResolver extends RepeaterMixin
{
    /**
     * @return True if this resource factory understands the given resource identifier
     */
    boolean accepts(ResourceIdentifier identifier);

    /**
     * @return A new resource for the given resource identifier
     */
    @UmlRelation(label = "creates")
    Resource resolve(ResourceIdentifier identifier);
}
