////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.code;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceCode;

/**
 * A simple callback interface. There are other similar interfaces, but sometimes the best name is callback.
 *
 * @param <Value> The type of object to be passed to the callback
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceCode.class)
public interface Callback<Value>
{
    default void callback(final Value value)
    {
        onCallback(value);
    }

    /**
     * The callback implementation
     *
     * @param value The value passed to the callback code
     */
    void onCallback(Value value);
}
