////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.web.swagger;

import com.telenav.kivakit.web.jetty.resources.BaseJettyServlet;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.OpenApiServlet;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * Produces the swagger.json OpenAPI interface description for the given application.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class JettySwaggerOpenApi extends BaseJettyServlet
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
