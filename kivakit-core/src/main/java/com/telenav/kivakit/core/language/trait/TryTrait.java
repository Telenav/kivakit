package com.telenav.kivakit.core.language.trait;

import com.telenav.kivakit.core.code.UncheckedCode;
import com.telenav.kivakit.core.code.UncheckedVoidCode;
import com.telenav.kivakit.core.messaging.Broadcaster;

/**
 * Provides methods that execute code, stripping off checked exceptions.
 *
 * <p><b>try/catch</b></p>
 *
 * <ul>
 *     <li>{@link #tryCatch(UncheckedVoidCode)}</li>
 *     <li>{@link #tryCatch(UncheckedCode)}</li>
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
public interface TryTrait extends Broadcaster, SilentTryTrait
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
                problem(e, "Code threw exception");
            }
        }
        
        return null;
    }
}
