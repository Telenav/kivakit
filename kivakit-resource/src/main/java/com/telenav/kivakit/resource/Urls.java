package com.telenav.kivakit.resource;

import com.telenav.kivakit.core.messaging.Listener;

import java.net.MalformedURLException;
import java.net.URL;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

@SuppressWarnings("unused")
public class Urls
{
    /**
     * Returns a {@link URL} for the given text
     *
     * @param url The url to parse
     * @return The {@link URL}
     * @throws IllegalArgumentException Thrown if the URL is invalid
     */
    public static URL url(String url)
    {
        return parseUrl(throwingListener(), url);
    }
    /**
     * Returns a {@link URL} for the given text
     *
     * @param listener The listener to call with any problems
     * @param url The url to parse
     * @return The {@link URL}
     * @throws IllegalArgumentException Thrown if the URL is invalid
     */
    public static URL parseUrl(Listener listener, String url)
    {
        try
        {
            return new URL(url);
        }
        catch (MalformedURLException e)
        {
            listener.problem("Invalid URL: " + url);
            return null;
        }
    }
}
