package com.telenav.kivakit.core.kernel.messaging;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessaging;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessaging.class)
public interface MessageFilter extends Filter<Transmittable>
{
    @Override
    default boolean accepts(final Transmittable message)
    {
        if (message instanceof Message)
        {
            return accepts((Message) message);
        }
        return false;
    }

    default boolean accepts(final Message message)
    {
        return false;
    }
}
