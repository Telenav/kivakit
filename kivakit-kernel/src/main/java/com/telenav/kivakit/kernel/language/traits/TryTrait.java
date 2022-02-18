package com.telenav.kivakit.kernel.language.traits;

import com.telenav.kivakit.kernel.interfaces.code.UncheckedCode;
import com.telenav.kivakit.kernel.interfaces.code.UncheckedVoidCode;
import com.telenav.kivakit.kernel.messaging.Broadcaster;

/**
 * Provides methods that execute code, stripping off checked exceptions.
 *
 * <p><b>try/catch</b></p>
 *
 * <ul>
 *     <li>{@link #tryCatch(UncheckedCode, String, Object...)}</li>
 *     <li>{@link #tryCatch(UncheckedVoidCode, String, Object...)}</li>
 * </ul>
 *
 * <p><b>try/catch/throw</b></p>
 *
 * <ul>
 *     <li>{@link #tryCatchThrow(UncheckedCode, String, Object...)}</li>
 *     <li>{@link #tryCatchThrow(UncheckedVoidCode, String, Object...)}</li>
 * </ul>
 *
 * <p><b>try/catch/default</b></p>
 *
 * <ul>
 *     <li>{@link #tryCatchDefault(UncheckedCode, Object)}</li>
 * </ul>
 *
 * <p><b>try/finally</b></p>
 *
 * <ul>
 *     <li>{@link #tryFinally(UncheckedVoidCode, Runnable)}</li>
 *     <li>{@link #tryFinally(UncheckedCode, Runnable)}</li>
 * </ul>
 *
 * <p><b>Examples:</b></p>
 *
 * <pre>tryCatch(this::start, "Microservice startup failed");</pre>
 *
 * <pre>tryCatchDefault(this::computeBytes, Bytes._0);</pre>
 *
 * <pre>tryFinally(this::compute, this::report);</pre>
 *
 * @author jonathanl (shibo)
 */
public interface TryTrait extends Broadcaster
{
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

    default <T> T tryCatchThrow(UncheckedCode<T> code, String message, Object... arguments)
    {
        try
        {
            return code.run();
        }
        catch (Exception e)
        {
            problem(message, arguments).throwAsIllegalStateException();
            return null;
        }
    }

    default void tryCatchThrow(UncheckedVoidCode code, String message, Object... arguments)
    {
        try
        {
            code.run();
        }
        catch (Exception e)
        {
            problem(message, arguments).throwAsIllegalStateException();
        }
    }

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
            after.run();
        }
    }

    default <T> T tryFinally(UncheckedCode<T> code, Runnable after)
    {
        try
        {
            return code.run();
        }
        catch (Exception e)
        {
            problem(e, "Code threw exception");
            return null;
        }
        finally
        {
            after.run();
        }
    }
}
