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

package com.telenav.kivakit.network.core;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.network.core.lexakai.DiagramNetworkLocation;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.reading.BaseReadableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.URI;
import java.net.URL;

/**
 * Base class for network resources. All network resources have a URI accessible with {@link #asUri()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramNetworkLocation.class)
@LexakaiJavadoc(complete = true)
public abstract class BaseNetworkResource extends BaseReadableResource implements NetworkResource
{
    protected BaseNetworkResource(BaseReadableResource that)
    {
        super(that);
    }

    protected BaseNetworkResource(NetworkLocation location)
    {
        super(ResourcePath.parseResourcePath(Listener.consoleListener(), location.toString()));
    }

    public URI asUri()
    {
        return location().asUri();
    }

    public URL asUrl()
    {
        return location().asUrl();
    }
}
