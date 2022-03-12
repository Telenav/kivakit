package com.telenav.kivakit.core.language.trait;

import com.telenav.kivakit.core.code.UncheckedCode;
import com.telenav.kivakit.core.code.UncheckedVoidCode;

/**
 * Provides methods that execute code, stripping off checked exceptions.
 *
 * <p><b>try/catch</b></p>
 *
 * <ul>
 *     <li>{@link #tryCatch(UncheckedVoidCode)}</li>
 *     <li>{@link #tryCatch(UncheckedCode)}</li>
 * </ul>
 *
 * <p><b>try/catch/default</b></p>
 *
 * <ul>
 *     <li>{@link #tryCatchDefault(UncheckedCode, Object)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
public interface SilentTryTrait
{
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
}
