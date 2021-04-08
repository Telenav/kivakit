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

package com.telenav.kivakit.web.jetty;

import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.web.jetty.resources.BaseJettyFilter;
import com.telenav.kivakit.web.jetty.resources.BaseJettyResource;
import com.telenav.kivakit.web.jetty.resources.BaseJettyServlet;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.log.StdErrLog;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

/**
 * A convenient way to use Jetty for simple web applications.
 * <p>
 * A Jetty web server can quickly and easily be started on a particular port with a given set of servlets and filters.
 * </p>
 * <p>
 * Support is available for:
 * </p>
 * <ul>
 *     <li>Wicket</li>
 *     <li>Jersey</li>
 *     <li>Swagger</li>
 * </ul>
 *
 * <p><b>Example:</b></p>
 *
 * <pre>
 * // Create the Jersey REST application with Swagger OpenAPI and documentation,
 * var application = new ServiceRegistryRestApplication();
 * var jersey = new JettyJersey(application);
 * var swaggerOpenApi = new JettySwaggerOpenApi(swaggerConfiguration(application));
 * var swaggerWebJar = new JettySwaggerWebJar(application);
 *
 * // create the Wicket WebApplication,
 * var wicket = new JettyWicket(ServiceRegistryWebApplication.class);
 *
 * // and start up Jetty.
 * listenTo(new JettyServer())
 *     .port(port)
 *     .add("/*", wicket)
 *     .add("/api/*", jersey)
 *     .add("/open-api/*", swaggerOpenApi)
 *     .add("/docs/*", new JettySwaggerDocs(port))
 *     .add("/webjar/*", swaggerWebJar)
 *     .start();
 *
 * JettySwaggerConfiguration swagger(ServiceRegistryRestApplication application)
 * {
 *     var api = new OpenAPI().info(new Info()
 *         .title("KivaKit Service Registry")
 *         .description("Registry of KivaKit services.")
 *         .contact(new Contact().email("jonathanl@telenav.com"))
 *         .license(new License().name("Copyright 2011-2021 Telenav, Inc.")));
 *
 *     var resources = Set.of(application.getClass());
 *     return new JettySwaggerConfiguration(api, application, resources);
 * }
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class JettyServer extends BaseRepeater
{
    public static void configureLogging()
    {
        System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
        org.eclipse.jetty.util.log.Log.setLog(new StdErrLog());
    }

    /** The port to run Jetty on */
    private int port;

    /** The filters to install when Jetty starts */
    private final List<BaseJettyFilter> filters = new ArrayList<>();

    /** The servlets to install when Jetty starts */
    private final List<BaseJettyServlet> servlets = new ArrayList<>();

    /** The static resources to install when Jetty starts */
    private final List<BaseJettyResource> resources = new ArrayList<>();

    public JettyServer()
    {
        configureLogging();
    }

    public JettyServer add(final String path, final BaseJettyFilter filter)
    {
        filter.path(path);
        filters.add(filter);
        return this;
    }

    public JettyServer add(final String path, final BaseJettyServlet servlet)
    {
        servlet.path(path);
        servlets.add(servlet);
        return this;
    }

    public JettyServer add(final String path, final BaseJettyResource resource)
    {
        resource.path(path);
        resources.add(resource);
        return this;
    }

    public JettyServer port(final int port)
    {
        ensure(port > 0);
        this.port = port;
        return this;
    }

    public void start()
    {
        try
        {
            // Create and start Jetty
            server().start();
            narrate("Jetty started on port $", port);
        }
        catch (final Exception e)
        {
            throw new Problem(e, "Couldn't start embedded Jetty web server").asException();
        }
    }

    private ServerConnector httpConnector(final Server server)
    {
        // Return an HTTP Jetty server connector for the port that was specified
        final ServerConnector http = new ServerConnector(server);
        http.setPort(port);
        http.setIdleTimeout(Duration.hours(1).asMilliseconds());
        return http;
    }

    private Server server()
    {
        // Create Jetty server and add HTTP connector to it,
        final var server = new Server();
        server.addConnector(httpConnector(server));

        // create a "ServletContextHandler", which is a really confusing name that really means
        // something like "the place where you can register all kinds of stuff that the server
        // will use when handling requests, including but not limited to servlets"
        final var servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContext.setContextPath("/");
        servletContext.setServer(server);
        servletContext.setSessionHandler(new SessionHandler());
        servletContext.setResourceBase(".");

        // then for each JettyResource,
        resources.forEach(resource ->
        {
            // add it to the servlet context at the given path,
            servletContext.addServlet(resource.holder(), resource.path());
            narrate("Added resource $ => $", resource.path(), resource.name());
        });

        // and for each JettyFilter,
        filters.forEach(filter ->
        {
            // add it to the servlet context at the given path,
            servletContext.addFilter(filter.holder(), filter.path(), filter.dispatchers());
            narrate("Added filter $ => $", filter.path(), filter.name());
        });

        // and for each JettyServlet,
        servlets.forEach(servlet ->
        {
            // add it to the servlet context at the given path,
            servletContext.addServlet(servlet.holder(), servlet.path());
            narrate("Added servlet $ => $", servlet.path(), servlet.name());
        });

        // and finally, add the servlet context as the handler for server requests.
        server.setHandler(servletContext);

        return server;
    }
}
