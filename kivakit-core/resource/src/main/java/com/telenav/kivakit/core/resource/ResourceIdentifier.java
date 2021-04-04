////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource;

import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramResourceService.class)
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

    public Resource resolve()
    {
        return Resource.resource(this);
    }

    @Override
    public String toString()
    {
        return identifier;
    }
}
