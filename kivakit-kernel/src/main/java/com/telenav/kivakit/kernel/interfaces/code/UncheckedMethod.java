package com.telenav.kivakit.kernel.interfaces.code;

import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;

/**
 * {@link Unchecked} for void methods
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
public interface UncheckedMethod
{
    Logger LOGGER = LoggerFactory.newLogger();

    static UncheckedMethod of(UncheckedMethod code)
    {
        return code;
    }

    void run();
}
