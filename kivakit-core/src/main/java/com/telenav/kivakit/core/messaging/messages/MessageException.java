package com.telenav.kivakit.core.messaging.messages;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.messaging.Message;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * An exception thrown when exception chaining might not be desired.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class MessageException extends RuntimeException
{
    private final Message message;

    public MessageException(Message message)
    {
        this.message = message;
    }

    public Message message()
    {
        return message;
    }

    @Override
    public String toString()
    {
        return message.toString();
    }
}
