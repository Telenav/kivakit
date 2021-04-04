////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.factory;

import com.telenav.kivakit.core.kernel.interfaces.value.Source;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceFactory;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * A factory that creates an object.
 * <p>
 * For factories that require a parameter to create the object, see {@link MapFactory}.
 *
 * @param <Value> The type of object to create
 * @author jonathanl (shibo)
 * @see MapFactory
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceFactory.class)
public interface Factory<Value> extends Source<Value>
{
    /**
     * @return New instance of the given type
     */
    @Override
    @UmlExcludeMember
    default Value get()
    {
        return newInstance();
    }

    /**
     * @return A new instance of the given type
     */
    Value newInstance();
}
