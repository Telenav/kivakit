package com.telenav.kivakit.kernel.language.traits;

import com.telenav.kivakit.kernel.interfaces.code.Unchecked;
import com.telenav.kivakit.kernel.interfaces.code.UncheckedVoid;
import com.telenav.kivakit.kernel.messaging.Broadcaster;

/**
 * Provides methods that execute code, stripping off checked exceptions.
 *
 * <p><b>try/catch</b></p>
 *
 * <ul>
 *     <li>{@link #tryCatch(Unchecked, String, Object...)}</li>
 *     <li>{@link #tryCatch(UncheckedVoid, String, Object...)}</li>
 * </ul>
 *
 * <p><b>try/catch/throw</b></p>
 *
 * <ul>
 *     <li>{@link #tryCatchThrow(Unchecked, String, Object...)}</li>
 *     <li>{@link #tryCatchThrow(UncheckedVoid, String, Object...)}</li>
 * </ul>
 *
 * <p><b>try/catch/default</b></p>
 *
 * <ul>
 *     <li>{@link #tryCatchDefault(Unchecked, Object)}</li>
 * </ul>
 *
 * <p><b>try/finally</b></p>
 *
 * <ul>
 *     <li>{@link #tryFinally(UncheckedVoid, Runnable)}</li>
 *     <li>{@link #tryFinally(Unchecked, Runnable)}</li>
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
    default <T> T tryCatch(Unchecked<T> code, String message, Object... arguments)
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

    default boolean tryCatch(UncheckedVoid code, String message, Object... arguments)
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

    default <T> T tryCatchDefault(Unchecked<T> code, T defaultValue)
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

    default <T> T tryCatchThrow(Unchecked<T> code, String message, Object... arguments)
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

    default void tryCatchThrow(UncheckedVoid code, String message, Object... arguments)
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

    default void tryFinally(UncheckedVoid code, Runnable after)
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

    default <T> T tryFinally(Unchecked<T> code, Runnable after)
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
