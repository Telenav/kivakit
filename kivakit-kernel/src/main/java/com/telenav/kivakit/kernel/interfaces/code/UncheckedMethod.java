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

    static UncheckedMethod of(final UncheckedMethod code)
    {
        return code;
    }

    /**
     * @return The value returned by the checked code
     * @throws Exception The exception that might be thrown by the code
     */
    void run() throws Exception;
}
