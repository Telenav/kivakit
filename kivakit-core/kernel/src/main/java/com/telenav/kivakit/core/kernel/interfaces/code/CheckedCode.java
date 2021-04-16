package com.telenav.kivakit.core.kernel.interfaces.code;

import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Listener;

/**
 * Code that can throw a checked {@link Exception}.
 * <p>
 * Methods are provided that provide exception handling. For example the code below executes the doIt() method and logs
 * a warning and returns the default value <i>false</i> if the code throws an exception. This design allows the code to
 * handle the checked exception in a succinct and readable way.
 * <pre>
 * boolean doIt() throws NumberFormatException
 * {
 *     [...]
 * }
 *
 *    [...]
 *
 * return Code.of(() -&gt; doIt()).or(false, "Unable to do it");
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
public interface CheckedCode<Value>
{
    Logger LOGGER = LoggerFactory.newLogger();

    static <T> CheckedCode<T> of(final CheckedCode<T> code)
    {
        return code;
    }

    /**
     * @param defaultValue A default value to return if the code throws an exception
     * @param message A warning message to give to the listener if an exception is thrown
     * @param arguments Arguments to interpolate into the message
     * @return The value returned by the code, or the given default value if an exception is thrown
     */
    default Value or(final Value defaultValue, final String message, final Object... arguments)
    {
        return or(LOGGER, defaultValue, message, arguments);
    }

    /**
     * @param defaultValue A default value to return if the code throws an exception
     * @param listener A listener to broadcast a warning message to if an exception is thrown
     * @param message A warning message to give to the listener if an exception is thrown
     * @param arguments Arguments to interpolate into the message
     * @return The value returned by the code, or the given default value if an exception is thrown
     */
    default Value or(final Listener listener, final Value defaultValue, final String message, final Object... arguments)
    {
        try
        {
            return run();
        }
        catch (final Exception e)
        {
            listener.warning(e, message, arguments);
            return defaultValue;
        }
    }

    /**
     * @return The value returned by the code, or a default value if an exception is thrown
     */
    default Value or(final Value defaultValue)
    {
        try
        {
            return run();
        }
        catch (final Exception ignored)
        {
            return defaultValue;
        }
    }

    default Value orNull()
    {
        return or(null);
    }

    /**
     * @return The value returned by the checked code
     * @throws Exception The exception that might be thrown by the code
     */
    Value run() throws Exception;
}