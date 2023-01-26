package com.telenav.kivakit.core.language;

import com.telenav.kivakit.core.messaging.messages.MessageException;

import static com.telenav.kivakit.core.messaging.Message.escapeMessageText;

public class Throwables
{
    public static String causeToString(Throwable cause)
    {
        if (cause == null)
        {
            return "Unknown cause";
        }
        if (cause instanceof MessageException messageException)
        {
            return messageException.message().formatted();
        }
        var message = cause.getMessage();
        var nested = cause.getCause();
        if (nested != null)
        {
            message += "\ncaused by: " + causeToString(nested);
        }
        return escapeMessageText(message);
    }
}
