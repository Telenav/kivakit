////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
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
