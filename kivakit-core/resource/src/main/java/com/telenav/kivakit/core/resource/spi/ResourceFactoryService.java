////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.spi;

import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.ResourceIdentifier;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceService;

/**
 * Service provider interface for resources
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@UmlRelation(label = "parses", referent = ResourceIdentifier.class)
public interface ResourceFactoryService
{
    /**
     * @return True if this resource factory understands the given resource identifier
     */
    boolean accepts(ResourceIdentifier identifier);

    /**
     * @return A new resource for the given resource identifier
     */
    @UmlRelation(label = "creates")
    Resource newResource(ResourceIdentifier identifier);
}
