package com.telenav.kivakit.kernel.language.code;

/**
 * {@link UncheckedCode} for void methods
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
public interface UncheckedVoidCode
{
    static UncheckedVoidCode of(UncheckedVoidCode code)
    {
        return code;
    }

    void run() throws Exception;
}
