package com.telenav.kivakit.core.code;

import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.value.Source;

/**
 * Removes exception handling from code that can throw a checked (or unchecked) {@link Exception}.
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
 * return UncheckedCode.of(this::doIt).or(false, "Unable to do it");
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
public interface UncheckedCode<Value>
{
    static <T> UncheckedCode<T> of(UncheckedCode<T> code)
    {
        return code;
    }

    /**
     * @return The value returned by the code, or a default value if an exception is thrown
     */
    default Value orDefault(Source<Value> defaultValue)
    {
        try
        {
            return run();
        }
        catch (Exception ignored)
        {
            return defaultValue.get();
        }
    }

    /**
     * @param defaultValue A default value to return if the code throws an exception
     * @param message A warning message to give to the listener if an exception is thrown
     * @param arguments Arguments to interpolate into the message
     * @return The value returned by the code, or the given default value if an exception is thrown
     */
    default Value orDefault(Value defaultValue, String message, Object... arguments)
    {
        return orDefault(Logger.logger(), defaultValue, message, arguments);
    }

    /**
     * @param defaultValue A default value to return if the code throws an exception
     * @param listener A listener to broadcast a warning message to if an exception is thrown
     * @param message A warning message to give to the listener if an exception is thrown
     * @param arguments Arguments to interpolate into the message
     * @return The value returned by the code, or the given default value if an exception is thrown
     */
    default Value orDefault(Listener listener, Value defaultValue, String message, Object... arguments)
    {
        try
        {
            return run();
        }
        catch (Exception e)
        {
            listener.warning(e, message, arguments);
            return defaultValue;
        }
    }

    /**
     * @return The value returned by the code, or a default value if an exception is thrown
     */
    default Value orDefault(Value defaultValue)
    {
        try
        {
            return run();
        }
        catch (Exception ignored)
        {
            return defaultValue;
        }
    }

    default Value orNull()
    {
        return orDefault((Value) null);
    }

    /**
     * @return The value returned by the checked code
     * @throws Exception The exception that might be thrown by the code
     */
    Value run() throws Exception;
}
