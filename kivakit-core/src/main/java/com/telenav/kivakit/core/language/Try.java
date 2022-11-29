package com.telenav.kivakit.core.language;

import com.telenav.kivakit.core.code.UncheckedCode;
import com.telenav.kivakit.core.messaging.Listener;

public class Try
{
    /**
     * Runs the given code catching all exceptions, including checked exceptions. If an exception is thrown, a problem
     * with the given message will be broadcast.
     *
     * @param code The code to run
     * @param message The message
     * @param arguments Arguments for formatting the message
     * @return The result of the code, or null if an exception was thrown
     */
    public static <T> T tryCatch(Listener listener, UncheckedCode<T> code, String message, Object... arguments)
    {
        try
        {
            return code.run();
        }
        catch (Exception e)
        {
            listener.problem(e, message, arguments);
            return null;
        }
    }
}
