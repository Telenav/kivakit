package com.telenav.kivakit.resource;

import com.telenav.kivakit.core.messaging.Listener;

import java.net.URI;

/**
 * Utility methods for working with {@link URI}s
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class Uris
{
    /**
     * Returns a {@link URI} for the given path
     *
     * @param path The path
     * @return The {@link URI}
     */
    public static URI parseUri(Listener listener, String path)
    {
        return new java.io.File(path).toURI();
    }
}
