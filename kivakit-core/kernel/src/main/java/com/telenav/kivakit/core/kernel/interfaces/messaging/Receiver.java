package com.telenav.kivakit.core.kernel.interfaces.messaging;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceMessaging;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageBroadcaster;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageListener;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.function.Consumer;

/**
 * A receiver of values with similar function to a {@link Consumer}, a sink, a callback or a target. {@link Receiver}s
 * and {@link Transmittable}s are used in the messaging API.
 *
 * @author jonathanl (shibo)
 * @see Transmittable
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceMessaging.class)
@UmlClassDiagram(diagram = DiagramMessageListener.class)
@UmlClassDiagram(diagram = DiagramMessageBroadcaster.class)
@UmlRelation(label = "receives", referent = Transmittable.class)
public interface Receiver<T>
{
    /**
     * Receives the given value
     */
    void receive(T value);
}
