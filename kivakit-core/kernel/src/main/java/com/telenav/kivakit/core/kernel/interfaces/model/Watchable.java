package com.telenav.kivakit.core.kernel.interfaces.model;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceModel;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A value that can be watched. Calling {@link #observe()} on a model object that implements {@link Watchable} will
 * return the current value.
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceModel.class)
public interface Watchable<Value>
{
    /**
     * @return The observed value
     */
    Value observe();
}
