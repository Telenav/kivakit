package com.telenav.kivakit.core.messaging.broadcasters;

import static com.telenav.kivakit.core.string.Formatter.format;

/**
 * An error thrown by {@link Multicaster} when there is no listener to call,
 * indicating a broken or unterminated listener chain.
 *
 * @author jonathanl (shibo)
 */
public class NoListenerError extends Error
{
    public NoListenerError(String message, Object... arguments)
    {
        super(format(message, arguments));
    }
}
