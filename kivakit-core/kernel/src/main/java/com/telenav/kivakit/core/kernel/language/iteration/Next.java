////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.iteration;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageIteration;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Interface for iteration where hasNext() is replaced by the semantics that null represents the end of iteration
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramLanguageIteration.class)
public interface Next<T>
{
    /**
     * @return The next value or null if there is none
     */
    T onNext();
}
