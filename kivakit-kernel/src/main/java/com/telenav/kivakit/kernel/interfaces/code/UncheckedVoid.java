package com.telenav.kivakit.kernel.interfaces.code;

import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;

/**
 * {@link Unchecked} for void methods
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
public interface UncheckedVoid
{
    Logger LOGGER = LoggerFactory.newLogger();

    static UncheckedVoid of(UncheckedVoid code)
    {
        return code;
    }

    void run() throws Exception;
}
