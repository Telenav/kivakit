package com.telenav.kivakit.core.os;

import static com.telenav.kivakit.core.os.Console.console;

/**
 * A trait to make console access cleaner
 *
 * @author jonathanl (shibo)
 * @see Console
 */
public interface ConsoleTrait
{
    /**
     * Prints the given formatted text to the console (stdout)
     *
     * @param text The text
     * @param arguments The arguments
     */
    default void print(String text, Object... arguments)
    {
        console().print(text, arguments);
    }

    /**
     * Prints the given formatted text to the console (stdout)
     *
     * @param text The text
     * @param arguments The arguments
     */
    default void println(String text, Object... arguments)
    {
        console().println(text, arguments);
    }
}
