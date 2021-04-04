////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.factory;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceFactory;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A factory that creates an object given a parameter (it "maps" from the parameter to the new object).
 * <p>
 * For factories that do not need a parameter, see {@link Factory}
 *
 * @param <Value> The type of object to create
 * @param <Parameter> A parametric value for the implementation to use when creating the object
 * @author jonathanl (shibo)
 * @see Factory
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceFactory.class)
public interface MapFactory<Parameter, Value>
{
    /**
     * Creates a value by constructing an object with the given parameter
     *
     * @param parameter The constructor parameter for creating a new object
     * @return The new object instance
     */
    Value newInstance(Parameter parameter);
}
