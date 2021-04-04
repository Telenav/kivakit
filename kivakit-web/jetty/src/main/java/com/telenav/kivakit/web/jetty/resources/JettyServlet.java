package com.telenav.kivakit.web.jetty.resources;

import com.telenav.kivakit.web.jetty.JettyRequestHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Base class for handling servlet requests.
 *
 * @author jonathanl (shibo)
 */
public abstract class JettyServlet extends JettyRequestHandler
{
    public JettyServlet(final String name)
    {
        super(name);
    }

    /**
     * @return Jetty-specific adaptor for servlets
     */
    public abstract ServletHolder holder();
}
