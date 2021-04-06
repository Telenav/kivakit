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

package com.telenav.kivakit.core.network.core;

import com.telenav.kivakit.core.network.core.project.lexakai.diagrams.DiagramNetworkLocation;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.resource.ResourcePath;
import com.telenav.kivakit.core.resource.reading.BaseReadableResource;

import java.net.URI;
import java.net.URL;

@UmlClassDiagram(diagram = DiagramNetworkLocation.class)
public abstract class BaseNetworkResource extends BaseReadableResource implements NetworkResource
{
    protected BaseNetworkResource(final BaseReadableResource that)
    {
        super(that);
    }

    protected BaseNetworkResource(final NetworkLocation location)
    {
        super(ResourcePath.parseResourcePath(location.toString()));
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
