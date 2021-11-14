package com.telenav.kivakit.kernel.messaging.messages;

import com.telenav.kivakit.kernel.messaging.Message;

/**
 * An exception thrown when exception chaining might not be desired.
 *
 * @author jonathanl (shibo)
 */
public class MessageException extends RuntimeException
{
    private Message message;

    public MessageException(Message message)
    {
        this.message = message;
    }

    public Message messageObject()
    {
        return message;
    }
}

