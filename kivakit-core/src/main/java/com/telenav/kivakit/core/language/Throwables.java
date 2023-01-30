package com.telenav.kivakit.core.language;

import com.telenav.kivakit.core.messaging.messages.MessageException;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.telenav.kivakit.core.messaging.Message.escapeMessageText;

public class Throwables
{
    public static String causeToString(Throwable cause)
    {
        if (cause == null)
        {
            return "";
        }

        if (cause instanceof MessageException messageException)
        {
            return messageException.message().formatted();
        }

        var message = stackTrace(cause);

        var nested = cause.getCause();
        if (nested != null)
        {
            message += "\n\ncaused by:\n\n" + causeToString(nested);
        }

        return escapeMessageText(message);
    }

    public static String stackTrace(Throwable throwable)
    {
        var buffer = new StringWriter();
        var out = new PrintWriter(buffer, true);
        throwable.printStackTrace(out);
        return buffer.toString().replaceAll("\\$", ".");
    }
}
