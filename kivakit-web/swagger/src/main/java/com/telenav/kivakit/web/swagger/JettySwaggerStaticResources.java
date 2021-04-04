package com.telenav.kivakit.web.swagger;

import com.telenav.kivakit.core.kernel.language.types.Classes;
import com.telenav.kivakit.web.jetty.resources.JettyResource;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Provides the Swagger JavaScript resources required to show Swagger documentation.
 *
 * @author jonathanl (shibo)
 */
public class JettySwaggerStaticResources extends JettyResource
{
    public JettySwaggerStaticResources()
    {
        super("[SwaggerStaticResources]");
    }

    @Override
    public ServletHolder holder()
    {
        final var defaultServlet = new DefaultServlet();

        final var holder = new ServletHolder(defaultServlet);
        holder.setName("static-resources");
        holder.setInitParameter("resourceBase", Classes.resourceUri(getClass(), "webapp").toString());
        holder.setInitParameter("dirAllowed", "false");
        holder.setInitParameter("pathInfoOnly", "true");

        return holder;
    }
}
