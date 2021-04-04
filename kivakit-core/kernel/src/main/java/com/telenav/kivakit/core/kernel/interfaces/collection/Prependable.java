////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.collection;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramExampleBaseList;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceCollection;

/**
 * An object, often a collection or related type, to which objects can be prepended.
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceCollection.class)
@UmlClassDiagram(diagram = DiagramExampleBaseList.class)
public interface Prependable<T>
{
    /**
     * Adds the given value
     *
     * @return Self reference for chaining of append calls
     */
    Prependable<T> prepend(T value);
}
