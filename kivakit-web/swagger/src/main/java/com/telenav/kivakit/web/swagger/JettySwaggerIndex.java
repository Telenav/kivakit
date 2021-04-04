package com.telenav.kivakit.web.swagger;

import com.telenav.kivakit.core.resource.resources.packaged.PackageResource;
import com.telenav.kivakit.web.jetty.resources.JettyResource;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Provides the Swagger JavaScript resources required to show Swagger documentation.
 *
 * @author jonathanl (shibo)
 */
public class JettySwaggerIndex extends JettyResource
{
    class IndexServlet extends HttpServlet
    {
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException
        {
            response.setContentType("text/html");
            try (final var out = response.getWriter())
            {
                out.println(index());
            }
        }
    }

    private final int port;

    public JettySwaggerIndex(final int port)
    {
        super("[SwaggerIndex]");
        this.port = port;
    }

    @Override
    public ServletHolder holder()
    {
        return new ServletHolder(name(), new IndexServlet());
    }

    String index()
    {
        return PackageResource.packageResource(JettySwaggerIndex.class, "webapp/index.html")
                .reader()
                .string()
                .replaceAll("PORT", Integer.toString(port));
    }
}
