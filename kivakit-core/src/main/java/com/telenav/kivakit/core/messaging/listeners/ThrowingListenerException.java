package com.telenav.kivakit.core.messaging.listeners;

import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.broadcasters.Multicaster;
import com.telenav.kivakit.core.messaging.messages.MessageException;
import com.telenav.kivakit.interfaces.messaging.Transmittable;

/**
 * An exception that the messaging system does not trap during the message broadcasting process (see the method
 * {@link Multicaster#transmit(Transmittable)} for details).
 *
 * @author jonathanl (shibo)
 * @see ThrowingListener
 */
public class ThrowingListenerException extends MessageException
{
    public ThrowingListenerException(Message message)
    {
        super(message);
    }
}
