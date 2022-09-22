package com.telenav.kivakit.core.language.trait;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.code.UncheckedCode;
import com.telenav.kivakit.core.code.UncheckedVoidCode;
import com.telenav.kivakit.core.messaging.Broadcaster;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.SUFFICIENT;
import static com.telenav.kivakit.annotations.code.TestingQuality.NONE;

/**
 * Provides methods that execute code, stripping off checked exceptions. This interface extends {@link Broadcaster} as
 * well as {@link TryCatchTrait}, allowing methods in this interface broadcast problems.
 *
 * <p><b>try/catch</b></p>
 *
 * <ul>
 *     <li>{@link #tryCatch(UncheckedCode, String, Object...)}<br/>  - Executes the given code, broadcasting a problem if it throws a checked or unchecked exception</li>
 *     <li>{@link #tryCatch(UncheckedVoidCode, String, Object...)}<br/>  - Executes the given code, broadcasting a problem if it throws a checked or unchecked exception</li>
 * </ul>
 *
 * <p><b>try/catch/throw</b></p>
 *
 * <ul>
 *     <li>{@link #tryCatchThrow(UncheckedCode, String, Object...)}<br/> - Executes the given code, rethrowing any checked or unchecked exceptions as an {@link IllegalStateException} with the given formatted message</li>
 *     <li>{@link #tryCatchThrow(UncheckedVoidCode, String, Object...)}<br/> - Executes the given code, rethrowing any checked or unchecked exceptions as an {@link IllegalStateException} with the given formatted message</li>
 * </ul>
 *
 * <p><b>try/finally</b></p>
 *
 * <ul>
 *     <li>{@link #tryFinally(UncheckedVoidCode, Runnable)}<br/> - Execute the given code, always running the given {@link Runnable} afterwards</li>
 *     <li>{@link #tryFinally(UncheckedCode, UncheckedVoidCode)}<br/> - Executes the given code, always running the given {@link UncheckedVoidCode} after execution</li>
 * </ul>
 *
 * <p><b>Examples:</b></p>
 *
 * <pre>tryCatch(this::start, "Microservice startup failed");</pre>
 *
 * <pre>tryFinally(this::compute, this::report);</pre>
 *
 * @author jonathanl (shibo)
 * @see TryCatchTrait
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_DEFAULT_EXPANDABLE,
             documentation = SUFFICIENT,
             testing = NONE)
public interface TryTrait extends
        Broadcaster,
        TryCatchTrait
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
    default <T> T tryCatch(UncheckedCode<T> code, String message, Object... arguments)
    {
        try
        {
            return code.run();
        }
        catch (Exception e)
        {
            problem(e, message, arguments);
            return null;
        }
    }

    /**
     * Runs the given code catching all exceptions, including checked exceptions. If an exception is thrown, a problem
     * with the given message will be broadcast.
     *
     * @param code The code to run
     * @param message The message
     * @param arguments Arguments for formatting the message
     * @return True if the code ran successfully, and false if an exception was thrown
     */
    default boolean tryCatch(UncheckedVoidCode code, String message, Object... arguments)
    {
        try
        {
            code.run();
            return true;
        }
        catch (Exception e)
        {
            problem(e, message, arguments);
            return false;
        }
    }

    /**
     * Runs the given code catching all exceptions, including checked exceptions. If an exception is thrown, a problem
     * with the given message will be thrown as an {@link IllegalStateException}.
     *
     * @param code The code to run
     * @param message The message
     * @param arguments Arguments for formatting the message
     * @return The result of running the code, or an {@link IllegalStateException}
     */
    default <T> T tryCatchThrow(UncheckedCode<T> code, String message, Object... arguments)
    {
        try
        {
            return code.run();
        }
        catch (Exception e)
        {
            problem(e, message, arguments).throwAsIllegalStateException();
            return null;
        }
    }

    /**
     * Runs the given code catching all exceptions, including checked exceptions. If an exception is thrown, a problem
     * with the given message will be thrown as an {@link IllegalStateException}.
     *
     * @param code The code to run
     * @param message The message
     * @param arguments Arguments for formatting the message
     */
    default void tryCatchThrow(UncheckedVoidCode code, String message, Object... arguments)
    {
        try
        {
            code.run();
        }
        catch (Exception e)
        {
            problem(e, message, arguments).throwAsIllegalStateException();
        }
    }

    /**
     * Runs the given code catching all exceptions, including checked exceptions. If an exception is thrown, a problem
     * with the given message will be broadcast. The {@link Runnable} code <i>after</i> will always be run.
     *
     * @param code The code to run
     * @param after The code to always run (in a "finally" block) after the code has run
     */
    default void tryFinally(UncheckedVoidCode code, Runnable after)
    {
        try
        {
            code.run();
        }
        catch (Exception e)
        {
            problem(e, "Code threw exception");
        }
        finally
        {
            try
            {
                after.run();
            }
            catch (Exception e)
            {
                problem(e, "Finally code threw exception");
            }
        }
    }

    /**
     * Runs the given code catching all exceptions, including checked exceptions. If an exception is thrown, a problem
     * with the given message will be broadcast. The {@link Runnable} code <i>after</i> will always be run.
     *
     * @param code The code to run
     * @param after The code to always run (in a "finally" block) after the code has run
     * @return The result of running the code
     */
    default <T> T tryFinally(UncheckedCode<T> code, UncheckedVoidCode after)
    {
        try
        {
            return code.run();
        }
        catch (Exception e)
        {
            problem(e, "Code threw exception");
        }
        finally
        {
            try
            {
                after.run();
            }
            catch (Exception e)
            {
                problem(e, "Finally code threw exception");
            }
        }

        return null;
    }
}
