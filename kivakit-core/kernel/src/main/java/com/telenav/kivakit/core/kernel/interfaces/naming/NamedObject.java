////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.naming;

import com.telenav.kivakit.core.kernel.language.values.name.Name;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceNaming;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An object with a <i>programmatic</i> name. An object that is a {@link NamedObject} differs from an object that is
 * {@link Named} in that its name is meant for use by programmers and not end users. If {@link #objectName()} is not
 * implemented, a synthetic name for the object is created with {@link Name#synthetic(Object)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceNaming.class)
public interface NamedObject
{
    /**
     * @return The name of this object for use in programming and debugging. If this method is not overridden, the name
     * will be the simple class name in hyphenated form followed by this object's identity hash code in hexadecimal.
     */
    default String objectName()
    {
        return Name.synthetic(this);
    }

    /**
     * Sets the name of this object
     */
    default void objectName(final String name)
    {
    }
}

