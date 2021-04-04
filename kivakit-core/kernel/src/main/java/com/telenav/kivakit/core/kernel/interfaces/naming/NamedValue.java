////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.naming;

import com.telenav.kivakit.core.kernel.interfaces.value.Valued;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceNaming;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A {@link Named} object that has a value, retrievable through {@link Valued#value()}.
 *
 * @param <T> The type of value
 * @author jonathanl (shibo)
 * @see Named
 * @see Valued
 */
@UmlClassDiagram(diagram = DiagramInterfaceNaming.class)
public interface NamedValue<T> extends Named, Valued<T>
{
}
