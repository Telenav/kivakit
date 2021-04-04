////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.listeners;

import com.telenav.kivakit.core.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidation;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageListenerType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A listener, such as {@link MessageList} that is able to report how many of different kinds of messages it has seen.
 * The {@link #count(Message.Status)} message returns the number of messages of a given {@link Message.Status} type it
 * has seen. For example, messages that represent failure (of which there is more than one) would have the step type
 * {@link Message.Status#FAILED}. To get a count of a specific message type like {@link Warning}, the method {@link
 * #count(Message.Status)} can be used. To get the total of all messages that are higher than a given status severity
 * (as determined by {@link Message.Status#isWorseThan(Message.Status)}, the method {@link
 * #countWorseThanOrEqualTo(Message.Status)} can be used. For example, to find out how many messages represented
 * something other than full success (warnings and worse), this code might be used:
 * <pre>
 * var issues = messages.countWorseThan(Status.SUCCESS)
 * </pre>
 * To find out how many messages represented a failure to complete:
 * <pre>
 * var issues = message.countWorseThan(Status.COMPLETED)
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see Message
 * @see Message.Status
 * @see MessageList
 */
@UmlClassDiagram(diagram = DiagramMessageListenerType.class)
@UmlClassDiagram(diagram = DiagramDataValidation.class)
public interface MessageCounter extends Listener
{
    /**
     * @return The number of messages with the given status
     */
    Count count(Message.Status type);

    /**
     * @return The number of messages of the given type
     */
    Count count(Class<? extends Message> type);

    /**
     * @return The number of messages that are worse than or equal to the given status
     */
    Count countWorseThanOrEqualTo(final Message.Status status);
}
