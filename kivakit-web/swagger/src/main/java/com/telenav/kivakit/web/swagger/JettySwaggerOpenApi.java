package com.telenav.kivakit.web.swagger;

import com.telenav.kivakit.web.jetty.resources.JettyServlet;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.OpenApiServlet;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * Produces the swagger.json OpenAPI interface description.
 *
 * @author jonathanl (shibo)
 */
public class JettySwaggerOpenApi extends JettyServlet
{
    private final Application application;

    public JettySwaggerOpenApi(final Application application)
    {
        super("[SwaggerOpenApi service = " + application.getClass().getSimpleName() + "]");

        this.application = application;
    }

    @Override
    public ServletHolder holder()
    {
        final var configuration = new SwaggerConfiguration()
                .prettyPrint(true)
                .resourcePackages(Set.of(application.getClass().getPackageName()));

        try
        {
            final OpenApiServlet servlet = new OpenApiServlet();

            new JaxrsOpenApiContextBuilder<>()
                    .servletConfig(servlet)
                    .application(application)
                    .openApiConfiguration(configuration)
                    .buildContext(true);

            return new ServletHolder(servlet);
        }
        catch (final OpenApiConfigurationException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
