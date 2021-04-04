////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.core;

import com.telenav.kivakit.core.network.core.project.lexakai.diagrams.DiagramNetworkLocation;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.kivakit.core.resource.Resource;

/**
 * Interface to a network resource. The resource has a {@link NetworkLocation}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramNetworkLocation.class)
public interface NetworkResource extends Resource
{
    /**
     * @return The location of this network resource
     */
    @UmlRelation(label = "located at")
    NetworkLocation location();
}
