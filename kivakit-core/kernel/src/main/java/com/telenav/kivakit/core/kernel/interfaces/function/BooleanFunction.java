package com.telenav.kivakit.core.kernel.interfaces.function;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceFunction;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A boolean functional interface that returns true or false for a given value.
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceFunction.class)
public interface BooleanFunction<Value>
{
    /**
     * @return True if the value evaluates to true
     */
    boolean isTrue(Value value);
}
