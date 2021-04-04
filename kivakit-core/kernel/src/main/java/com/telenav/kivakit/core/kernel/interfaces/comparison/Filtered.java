////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.comparison;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceComparison;

import java.util.List;

/**
 * A filtered object has a list of filters to apply.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceComparison.class)
public interface Filtered<T>
{
    /**
     * @return The filters for this object
     */
    @UmlRelation(label = "has", referentCardinality = "*")
    List<Filter<T>> filters();
}
