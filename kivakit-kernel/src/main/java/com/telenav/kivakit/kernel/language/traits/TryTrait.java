package com.telenav.kivakit.kernel.language.traits;

import com.telenav.kivakit.kernel.interfaces.code.Unchecked;
import com.telenav.kivakit.kernel.interfaces.code.UncheckedVoid;
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
