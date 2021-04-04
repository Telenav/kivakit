package com.telenav.kivakit.web.jetty.resources;

import com.telenav.kivakit.web.jetty.JettyRequestHandler;
import org.eclipse.jetty.servlet.FilterHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Base class for request handlers that filter requests and then pass them on to other request handlers and finally
 * servlets. The kind of requests that the filter intercepts is determined by {@link #dispatchers()}.
 *
 * @author jonathanl (shibo)
 */
public abstract class JettyFilter extends JettyRequestHandler
{
    public JettyFilter(final String name)
    {
        super(name);
    }

    /**
     * @return The set of request types that this filter handles
     */
    public abstract EnumSet<DispatcherType> dispatchers();

    /**
     * @return Jetty-specific adaptor for filters
     */
    public abstract FilterHolder holder();
}
