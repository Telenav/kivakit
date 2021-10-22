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

import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An arbitrary string that identifies a resource. This might be a path or package name or some other identifier that
 * can be resolved into a resource. The {@link #resolve()} method searches resource service providers to find one that
 * accepts this identifier. It then returns the resource, as resolved by the service provider.
 *
 * @author jonathanl (shibo)
 * @see Resource#resolve(String)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@LexakaiJavadoc(complete = true)
public class ResourceIdentifier
{
    private final String identifier;

    public ResourceIdentifier(final String identifier)
    {
        this.identifier = identifier;
    }

    public String identifier()
    {
        return identifier;
    }

    public Resource resolve(Listener listener)
    {
        return Resource.resolve(listener, this);
    }

    @Override
    public String toString()
    {
        return identifier;
    }
}
