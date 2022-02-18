package com.telenav.kivakit.kernel.interfaces.code;

import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;

/**
 * {@link UncheckedCode} for void methods
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
public interface UncheckedVoidCode
{
    Logger LOGGER = LoggerFactory.newLogger();

    static UncheckedVoidCode of(UncheckedVoidCode code)
    {
        return code;
    }

    void run() throws Exception;
}
