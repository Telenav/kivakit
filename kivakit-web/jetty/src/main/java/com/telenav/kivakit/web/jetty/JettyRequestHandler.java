package com.telenav.kivakit.web.jetty;

import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.web.jetty.resources.*;

/**
 * Base class for all types of Jetty request handlers.
 *
 * @author jonathanl (shibo)
 * @see JettyFilter
 * @see JettyResource
 * @see JettyServlet
 */
public abstract class JettyRequestHandler implements Named
{
    private String path;

    private final String name;

    public JettyRequestHandler(final String name)
    {
        this.name = name;
    }

    @Override
    public String name()
    {
        return name;
    }

    protected void path(final String path)
    {
        this.path = path;
    }

    protected String path()
    {
        return path;
    }
}
