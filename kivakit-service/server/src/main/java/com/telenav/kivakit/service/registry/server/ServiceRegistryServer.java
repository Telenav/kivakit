package com.telenav.kivakit.service.registry.server;

import com.telenav.kivakit.core.application.Server;
import com.telenav.kivakit.core.commandline.SwitchParser;
import com.telenav.kivakit.core.kernel.language.objects.Lazy;
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
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlNote;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.Set;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramServer.class)
@UmlRelation(label = "persists to", referent = ServiceRegistryStore.class)
@UmlRelation(label = "creates", referent = ServiceRegistry.class)
@UmlRelation(label = "updates", referent = ServiceRegistry.class)
@UmlRelation(label = "searches", referent = ServiceRegistry.class)
@UmlNote(text = "For REST API details, refer to Swagger documentation provided by this server")
public class ServiceRegistryServer extends Server
{
    private static final Lazy<ServiceRegistryServer> singleton = Lazy.of(ServiceRegistryServer::new);

    public static ServiceRegistryServer get()
    {
        return singleton.get();
    }

    public static void main(final String[] arguments)
    {
        get().run(arguments);
    }

    private final SwitchParser<Boolean> NETWORK = SwitchParser
            .booleanSwitch("network", "True if this registry server is a server for the whole network")
            .defaultValue(false)
            .optional()
            .build();

    private final SwitchParser<Integer> PORT = SwitchParser
            .integerSwitch("first-port", "The first port in the range of ports to be allocated")
            .defaultValue(50_000)
            .optional()
            .build();

    private transient final Lazy<ServiceRegistry> registry = Lazy.of(() ->
    {
        final var registry = listenTo(get(NETWORK)
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
        return commandLine().get(NETWORK);
    }

    public int port()
    {
        return commandLine().get(PORT);
    }

    public ServiceRegistry registry()
    {
        return registry.get();
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

        // create the Jersey REST application with Swagger OpenAPI and documentation,
        final var application = new ServiceRegistryRestApplication();
        final var jersey = new JettyJersey(application);
        final var swaggerOpenApi = new JettySwaggerOpenApi(application);
        final var swaggerWebJar = new JettySwaggerWebJar(application);

        // create the Wicket WebApplication,
        final var wicket = new JettyWicket(ServiceRegistryWebApplication.class);

        // and start up Jetty.
        listenTo(new JettyServer())
                .port(port)
                .add("/*", wicket)
                .add("/open-api/*", swaggerOpenApi)
                .add("/docs/*", new JettySwaggerIndex(port))
                .add("/webapp/*", new JettySwaggerStaticResources())
                .add("/webjar/*", swaggerWebJar)
                .add("/*", jersey)
                .start();
    }

    @Override
    protected Set<SwitchParser<?>> switchParsers()
    {
        return Set.of(PORT, NETWORK);
    }
}
