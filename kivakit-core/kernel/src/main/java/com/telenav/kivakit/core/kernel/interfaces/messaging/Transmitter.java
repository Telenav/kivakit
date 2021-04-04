package com.telenav.kivakit.core.kernel.interfaces.messaging;

import com.telenav.kivakit.core.kernel.interfaces.value.Source;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceMessaging;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageBroadcaster;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessaging;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.function.Supplier;

/**
 * A transmitter of values with similar function to a {@link Source}, or a {@link Supplier}. {@link Transmitter}s are
 * used in the messaging framework to transmit {@link Transmittable} values.
 *
 * @author jonathanl (shibo)
 * @see Transmittable
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceMessaging.class)
@UmlClassDiagram(diagram = DiagramMessaging.class)
@UmlClassDiagram(diagram = DiagramMessageBroadcaster.class)
@UmlRelation(label = "transmits", referent = Transmittable.class)
public interface Transmitter<T>
{
    /**
     * Receives the given value
     */
    void transmit(T value);
}
