package com.telenav.kivakit.core.kernel.interfaces.value;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Interface to an object that has a boolean value
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceValue.class)
public interface BooleanValued
{
    /**
     * @return True if this expression is true
     */
    boolean isTrue();
}
