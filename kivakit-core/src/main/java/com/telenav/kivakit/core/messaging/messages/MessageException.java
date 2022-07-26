package com.telenav.kivakit.core.messaging.messages;

import com.telenav.kivakit.core.messaging.Message;

/**
 * An exception thrown when exception chaining might not be desired.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class MessageException extends RuntimeException
{
    private final Message message;

    public MessageException(Message message)
    {
        this.message = message;
    }

    public Message messageObject()
    {
        return message;
    }

    @Override
    public String toString()
    {
        return message.toString();
    }
}
