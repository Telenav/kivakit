package com.telenav.kivakit.core.language.trait;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.code.UncheckedCode;
import com.telenav.kivakit.core.code.UncheckedVoidCode;
import com.telenav.kivakit.core.messaging.messages.MessageException;
import com.telenav.kivakit.core.messaging.messages.status.Problem;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Provides methods that execute code, stripping off checked exceptions. {@link TryTrait} extends this interface to
 * provide try-related methods that broadcast problems.
 *
 * <p><b>try/catch</b></p>
 *
 * <ul>
 *     <li>{@link #tryCatch(UncheckedCode)}<br/> - Executes the given code, catching any exceptions, including checked exceptions</li>
 *     <li>{@link #tryCatch(UncheckedVoidCode)}<br/> - Executes the given code, catching any exceptions, including checked exceptions</li>
 * </ul>
 *
 * <p><b>try/catch/default</b></p>
 *
 * <ul>
 *     <li>{@link #tryCatchDefault(UncheckedCode, Object)}<br/> - Executes the given code, returning the given default if a checked or unchecked exception occurs</li>
 * </ul>
 *
 * <p><b>Examples:</b></p>
 *
 * <pre>tryCatch(this::start);</pre>
 *
 * <pre>tryCatchDefault(this::computeBytes, Bytes._0);</pre>
 *
 * @author jonathanl (shibo)
 * @see TryTrait
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTED,
             testing = UNTESTED)
public interface TryCatchTrait
{
    /**
     * Runs the given code catching all exceptions, including checked exceptions.
     *
     * @param code The code to run
     * @return The result of the code, or null if an exception was thrown
     */
    default <T> T tryCatch(UncheckedCode<T> code)
    {
        try
        {
            return code.run();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Runs the given code catching all exceptions, including checked exceptions.
     *
     * @param code The code to run
     */
    default void tryCatch(UncheckedVoidCode code)
    {
        try
        {
            code.run();
        }
        catch (Exception ignored)
        {
        }
    }

    /**
     * Runs the given code, catching all exceptions, including checked exceptions. If an exception is thrown, the given
     * default value will be returned.
     *
     * @param code The code to run
     * @param defaultValue The default value to return if an exception is thrown
     * @return The result of the code, or the default value if an exception was thrown
     */
    default <T> T tryCatchDefault(UncheckedCode<T> code, T defaultValue)
    {
        try
        {
            return code.run();
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     * Runs the given code, catching all exceptions, including checked exceptions. If an exception is thrown, it will be
     * caught and a {@link MessageException} will be rethrown, adding the given formatted message.
     *
     * @param code The code to run
     * @param message The message to format
     * @param arguments Arguments to message formatting
     * @return The result of the code
     */
    default <T> T tryCatchRethrow(UncheckedCode<T> code, String message, Object... arguments)
    {
        try
        {
            return code.run();
        }
        catch (Exception e)
        {
            throw new MessageException(new Problem(e, message, arguments));
        }
    }

    /**
     * Runs the given code, catching all exceptions, including checked exceptions. If an exception is thrown, it will be
     * caught and a {@link MessageException} will be rethrown, adding the given formatted message.
     *
     * @param code The code to run
     * @param message The message to format
     * @param arguments Arguments to message formatting
     */
    default void tryCatchRethrow(UncheckedVoidCode code, String message, Object... arguments)
    {
        try
        {
            code.run();
        }
        catch (Exception e)
        {
            throw new MessageException(new Problem(e, message, arguments));
        }
    }
}
