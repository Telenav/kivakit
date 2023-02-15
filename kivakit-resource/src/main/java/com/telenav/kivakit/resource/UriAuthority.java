package com.telenav.kivakit.resource;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

/**
 * Holds authority information from a URI:
 *
 * <pre>[scheme ":"]* ["//" authority]? [path] ["?" query]? ["#" fragment]?</pre>
 *
 * @author Jonathan Locke
 */
public class UriAuthority
{
    public static UriAuthority uriAuthority()
    {
        return new UriAuthority();
    }

    @NotNull
    public static UriAuthority uriAuthority(URI uri)
    {
        var authority = new UriAuthority();
        if (uri != null)
        {
            authority.authority = uri.getAuthority();
            authority.port = uri.getPort();
            authority.host = uri.getHost();
            authority.user = uri.getUserInfo();
        }
        return authority;
    }

    private String authority;

    private int port;

    private String host;

    private String user;

    protected UriAuthority()
    {
    }

    public String authority()
    {
        return authority;
    }

    public String host()
    {
        return host;
    }

    public int port()
    {
        return port;
    }

    @Override
    public String toString()
    {
        return authority == null ? "" : "//" + authority;
    }

    public String user()
    {
        return user;
    }
}
