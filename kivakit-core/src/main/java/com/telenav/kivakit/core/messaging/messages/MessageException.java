package com.telenav.kivakit.core.messaging.messages;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.Message;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * An exception thrown when exception chaining might not be desired.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
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
