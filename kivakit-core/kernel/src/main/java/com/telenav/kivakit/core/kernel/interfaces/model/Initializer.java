////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.model;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceModel;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An algorithm (strategy) that initializes some object.
 *
 * @param <Model> The type of object to initialize
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceModel.class)
public interface Initializer<Model>
{
    /**
     * An initializer that does nothing
     */
    static <T> Initializer<T> nullInitializer()
    {
        return value ->
        {
        };
    }

    /**
     * Initializes the value and returns it for call chaining
     */
    void initialize(Model value);
}
