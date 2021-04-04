package com.telenav.kivakit.core.kernel.interfaces.collection;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceCollection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An interface for any object that can contain values, typically a collection.
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceCollection.class)
public interface Contains<T>
{
    /**
     * @return True if the given value is contained in this object
     */
    boolean contains(T value);
}
