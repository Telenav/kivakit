package com.telenav.kivakit.web.swagger;

import com.telenav.kivakit.core.kernel.language.types.Classes;
import com.telenav.kivakit.web.jetty.resources.JettyResource;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.ws.rs.core.Application;

/**
 * Provides the Swagger JavaScript resources required to show Swagger documentation.
 *
 * @author jonathanl (shibo)
 */
public class JettySwaggerWebJar extends JettyResource
{
    private final Application application;

    public JettySwaggerWebJar(final Application application)
    {
        super("[SwaggerDocumentation service = " + application.getClass().getSimpleName() + "]");

        this.application = application;
    }

    @Override
    public ServletHolder holder()
    {
        final var holder = new ServletHolder(name(), new DefaultServlet());
        holder.setInitParameter("resourceBase", resourceBase());
        holder.setInitParameter("dirAllowed", "false");
        holder.setInitParameter("pathInfoOnly", "true");
        return holder;
    }

    String resourceBase()
    {
        final var path = "META-INF/resources/webjars/swagger-ui/3.34.0";
        return Classes.resourceUri(application.getClass(), path).toString();
    }
}
