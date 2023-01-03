package com.telenav.kivakit.resource;

import com.telenav.kivakit.core.messaging.Listener;

import java.net.URI;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * Utility methods for working with {@link URI}s
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class Uris
{
    /**
     * Returns a URI for the given text
     * @param text The text
     * @return The URI
     * @throws RuntimeException Thrown if the URI is invalid
     */
    public static URI uri(String text)
    {
        return parseUri(throwingListener(), text);
    }

    /**
     * Returns a {@link URI} for the given text
     *
     * @param listener The listener to call with any problems
     * @param text The path
     * @return The {@link URI}
     */
    public static URI parseUri(Listener listener, String text)
    {
        return new java.io.File(text).toURI();
    }
}
