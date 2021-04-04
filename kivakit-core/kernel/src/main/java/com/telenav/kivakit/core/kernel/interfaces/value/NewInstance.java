package com.telenav.kivakit.core.kernel.interfaces.value;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramExampleBaseList;

/**
 * Interface implemented by subclassed objects which need to operate on instances created by the subclass. Unlike {@link
 * Factory}, this interface does not subclass {@link Source}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramExampleBaseList.class)
public interface NewInstance<T>
{
    /**
     * @return Creates a new object instance
     */
    default T newInstance()
    {
        return onNewInstance();
    }

    /**
     * @return A new instance of type type
     */
    T onNewInstance();
}
