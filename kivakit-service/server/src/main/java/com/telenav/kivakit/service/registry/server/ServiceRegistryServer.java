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

package com.telenav.kivakit.service.registry.server;

import com.telenav.kivakit.core.application.Server;
import com.telenav.kivakit.core.commandline.SwitchParser;
import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.service.registry.Scope;
import com.telenav.kivakit.service.registry.ServiceRegistry;
import com.telenav.kivakit.service.registry.project.ServiceRegistryProject;
import com.telenav.kivakit.service.registry.registries.LocalServiceRegistry;
import com.telenav.kivakit.service.registry.registries.NetworkServiceRegistry;
import com.telenav.kivakit.service.registry.server.project.lexakai.diagrams.DiagramServer;
import com.telenav.kivakit.service.registry.server.rest.ServiceRegistryRestApplication;
import com.telenav.kivakit.service.registry.server.webapp.ServiceRegistryWebApplication;
import com.telenav.kivakit.service.registry.store.ServiceRegistryStore;
import com.telenav.kivakit.web.jersey.JettyJersey;
import com.telenav.kivakit.web.jetty.JettyServer;
import com.telenav.kivakit.web.swagger.JettySwaggerIndex;
import com.telenav.kivakit.web.swagger.JettySwaggerOpenApi;
import com.telenav.kivakit.web.swagger.JettySwaggerStaticResources;
import com.telenav.kivakit.web.swagger.JettySwaggerWebJar;
import com.telenav.kivakit.web.wicket.JettyWicket;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlNote;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.Set;

/**
 * Service registry server, including Wicket, REST and Swagger resources. Accepts these switches from the command line:
 *
 * <ul>
 *     <li>-scope=[network|localhost] - The scope of this registry, defaults to localhost</li>
 *     <li>-port=[number] - The port to run this server on</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramServer.class)
@UmlRelation(label = "persists to", referent = ServiceRegistryStore.class)
@UmlRelation(label = "creates", referent = ServiceRegistry.class)
@UmlRelation(label = "updates", referent = ServiceRegistry.class)
@UmlRelation(label = "searches", referent = ServiceRegistry.class)
@UmlNote(text = "For REST API details, refer to Swagger documentation provided by this server")
@LexakaiJavadoc(complete = true)
public class ServiceRegistryServer extends Server
{
    private static final Lazy<ServiceRegistryServer> project = Lazy.of(ServiceRegistryServer::new);

    public static ServiceRegistryServer get()
    {
        return project.get();
    }

    public static void main(final String[] arguments)
    {
        get().run(arguments);
    }

    private final SwitchParser<Scope.Type> SCOPE = SwitchParser
            .enumSwitch("scope", "The scope of operation for this server", Scope.Type.class)
            .defaultValue(Scope.localhost().type())
            .optional()
            .build();

    private final SwitchParser<Integer> PORT = SwitchParser
            .integerSwitch("first-port", "The first port in the range of ports to be allocated")
            .defaultValue(50_000)
            .optional()
            .build();

    private transient final Lazy<ServiceRegistry> registry = Lazy.of(() ->
    {
        final var registry = listenTo(get(SCOPE) == Scope.Type.NETWORK
                ? new NetworkServiceRegistry()
                : new LocalServiceRegistry(get(PORT)));

        registry.load();
        registry.start();

        return registry;
    });

    protected ServiceRegistryServer()
    {
        super(ServiceRegistryProject.get());
    }

    public boolean isLocal()
    {
        return !isNetwork();
    }

    public boolean isNetwork()
    {
        return scope() == Scope.Type.NETWORK;
    }

    public int port()
    {
        return commandLine().get(PORT);
    }

    public ServiceRegistry registry()
    {
        return registry.get();
    }

    public Scope.Type scope()
    {
        return commandLine().get(SCOPE);
    }

    @Override
    protected void onRun()
    {
        announce();

        // Determine what port to use for the server,
        final var settings = ServiceRegistry.settings();
        final var port = isNetwork()
                ? settings.networkServiceRegistryPort().number()
                : settings.localServiceRegistryPort();

        // create the Jersey REST application,
        final var application = new ServiceRegistryRestApplication();

        // and start up Jetty with Swagger, Jersey and Wicket.
        listenTo(new JettyServer())
                .port(port)
                .add("/*", new JettyWicket(ServiceRegistryWebApplication.class))
                .add("/open-api/*", new JettySwaggerOpenApi(application))
                .add("/docs/*", new JettySwaggerIndex(port))
                .add("/webapp/*", new JettySwaggerStaticResources())
                .add("/webjar/*", new JettySwaggerWebJar(application))
                .add("/*", new JettyJersey(application))
                .start();
    }

    @Override
    protected Set<SwitchParser<?>> switchParsers()
    {
        return Set.of(PORT, SCOPE);
    }
}
