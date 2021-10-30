package com.telenav.kivakit.kernel.language.traits;

import com.telenav.kivakit.kernel.interfaces.code.Unchecked;
import com.telenav.kivakit.kernel.interfaces.code.UncheckedMethod;
import com.telenav.kivakit.kernel.messaging.Broadcaster;

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

    default void tryCatch(UncheckedMethod code, String message, Object... arguments)
    {
        try
        {
            code.run();
        }
        catch (Exception e)
        {
            problem(e, message, arguments);
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

    default void tryFinally(UncheckedMethod code, Runnable after)
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

    default <T> T tryFinallyReturn(Unchecked<T> code, Runnable after)
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
