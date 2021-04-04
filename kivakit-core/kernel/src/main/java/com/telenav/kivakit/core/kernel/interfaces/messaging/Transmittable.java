package com.telenav.kivakit.core.kernel.interfaces.messaging;

import com.telenav.kivakit.core.kernel.messaging.Broadcaster;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceMessaging;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageBroadcaster;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessaging;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A message object that can be transmitted with a {@link Broadcaster} and received by a {@link Listener}. Although
 * {@link Message} is the primary implementor {@link Transmittable} any class can implement this interface and
 * participate in the broadcaster / listener framework.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceMessaging.class)
@UmlClassDiagram(diagram = DiagramMessaging.class)
@UmlClassDiagram(diagram = DiagramMessageBroadcaster.class)
public interface Transmittable
{
}
