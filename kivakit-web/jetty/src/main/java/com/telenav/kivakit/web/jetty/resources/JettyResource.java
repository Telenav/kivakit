package com.telenav.kivakit.web.jetty.resources;

import com.telenav.kivakit.web.jetty.JettyRequestHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Base class for request handlers that serve up file resources.
 *
 * @author jonathanl (shibo)
 */
public abstract class JettyResource extends JettyRequestHandler
{
    public JettyResource(final String name)
    {
        super(name);
    }

    /**
     * @return The Jetty-specific holder of a static resource servlet, normally {@link DefaultServlet}.
     */
    public abstract ServletHolder holder();
}
