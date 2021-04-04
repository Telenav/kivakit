package com.telenav.kivakit.web.jersey;

import com.telenav.kivakit.web.jetty.resources.JettyServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * @author jonathanl (shibo)
 */
public class JettyJersey extends JettyServlet
{
    private final ResourceConfig application;

    public JettyJersey(final ResourceConfig application)
    {
        super("[Jersey application = " + application.getClass().getSimpleName() + "]");

        this.application = application;
    }

    @Override
    public ServletHolder holder()
    {
        // Get the fully qualified class name of the JAX-RS application,
        final var name = application.getClass().getName();

        // create a "ServletContainer" for the application (this object may look like it's part of
        // the Servlet API, but it would be better named "RestServlet"),
        final var restServlet = new ServletContainer(application);

        // create a Jetty-specific "ServletHolder" for the servlet
        final var jersey = new ServletHolder(name, restServlet);

        // and initialize it with the name of the JAX-RS application.
        jersey.setInitOrder(0);
        jersey.setInitParameter("javax.ws.rs.Application", name);

        return jersey;
    }
}
