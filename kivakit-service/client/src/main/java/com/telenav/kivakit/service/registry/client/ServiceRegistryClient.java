package com.telenav.kivakit.service.registry.client;

import com.google.gson.Gson;
import com.telenav.kivakit.core.application.Application;
import com.telenav.kivakit.core.application.ApplicationIdentifier;
import com.telenav.kivakit.core.application.Server;
import com.telenav.kivakit.core.configuration.settings.Settings;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.kernel.language.reflection.Type;
import com.telenav.kivakit.core.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.values.version.Version;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.core.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.core.kernel.messaging.messages.Result;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.network.core.Port;
import com.telenav.kivakit.core.resource.resources.jar.launcher.JarLauncher;
import com.telenav.kivakit.service.registry.Scope;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.ServiceMetadata;
import com.telenav.kivakit.service.registry.ServiceRegistry;
import com.telenav.kivakit.service.registry.ServiceRegistrySettings;
import com.telenav.kivakit.service.registry.ServiceType;
import com.telenav.kivakit.service.registry.client.project.lexakai.diagrams.DiagramClient;
import com.telenav.kivakit.service.registry.protocol.BaseRequest;
import com.telenav.kivakit.service.registry.protocol.BaseResponse;
import com.telenav.kivakit.service.registry.protocol.discover.DiscoverApplicationsRequest;
import com.telenav.kivakit.service.registry.protocol.discover.DiscoverApplicationsResponse;
import com.telenav.kivakit.service.registry.protocol.discover.DiscoverPortServiceRequest;
import com.telenav.kivakit.service.registry.protocol.discover.DiscoverPortServiceResponse;
import com.telenav.kivakit.service.registry.protocol.discover.DiscoverServicesRequest;
import com.telenav.kivakit.service.registry.protocol.discover.DiscoverServicesResponse;
import com.telenav.kivakit.service.registry.protocol.register.RegisterServiceRequest;
import com.telenav.kivakit.service.registry.protocol.register.RegisterServiceResponse;
import com.telenav.kivakit.service.registry.protocol.renew.RenewServiceRequest;
import com.telenav.kivakit.service.registry.protocol.renew.RenewServiceResponse;
import com.telenav.kivakit.service.registry.protocol.update.NetworkRegistryUpdateRequest;
import com.telenav.kivakit.service.registry.protocol.update.NetworkRegistryUpdateResponse;
import com.telenav.kivakit.service.registry.registries.LocalServiceRegistry;
import com.telenav.kivakit.service.registry.registries.NetworkServiceRegistry;
import com.telenav.kivakit.service.registry.serialization.ServiceRegistryGsonFactory;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import java.net.ConnectException;
import java.security.Provider;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.resource.resources.jar.launcher.JarLauncher.ProcessType.DETACHED;
import static com.telenav.kivakit.service.registry.protocol.discover.DiscoverServicesRequest.SearchType.ALL_SERVICES;
import static com.telenav.kivakit.service.registry.protocol.discover.DiscoverServicesRequest.SearchType.APPLICATION_SERVICES;
import static com.telenav.kivakit.service.registry.protocol.discover.DiscoverServicesRequest.SearchType.SERVICES_OF_TYPE;

/**
 * A JSON + REST + JERSEY proxy to {@link ServiceRegistry} processes that register services on the local host and
 * discover services by {@link Scope}: locally, on remote hosts, in clusters and on the network at large.
 * <p>
 * Service registration makes application services discoverable and maps them to physical ports on the local host,
 * ensuring that port conflicts don't occur. These port mappings and other information are automatically propagated to
 * the network-wide registry where it can be queried with this client.
 * <p>
 * <i>Note: at this time, only {@link Scope#localhost()} and {@link Scope#network()} are supported.</i>
 * </p>
 * <p>
 * <b>Registry Servers</b>
 * </p>
 * ServiceRegistryServer is a command-line application can be run in two modes: as a registry on the local host (on port
 * 23,573) or as a network registry located on a well-known host (on port 23575 by default) that can be specified by the
 * KIVAKIT_NETWORK_SERVICE_REGISTRY_PORT environment variable (the default is kivakit-network-service-registry.mypna.com:23575).
 * When local service registries add, update and expire registration entries, they propagate this information upwards to
 * the network registry, allowing service lookups directed to the network registry to be performed against all services
 * on the network. A web view of current service registrations is available on the ports mentioned above.
 * <p>
 * <b>Registry Clients</b>
 * </p>
 * <p>
 * Registration and lookup are implemented under-the-hood as JSON REST services and non-Java applications can use this
 * interface directly (see ServiceRegistryRestResource). For KivaKit applications, the {@link ServiceRegistryClient}
 * class provides easy-to-use access to the network and local registries. {@link ServiceRegistryClient} is simply a Java
 * proxy that implements this {@link ServiceRegistry} interface.
 * </p>
 * <p>
 * <b>Only {@link ServiceRegistryClient} is Public API</b>
 * </p>
 * <p>
 * The service registry class has three subclasses: {@link LocalServiceRegistry}, {@link NetworkServiceRegistry} and
 * {@link ServiceRegistryClient}. Only the client (which interacts with the other two subclasses over REST) is intended
 * for use by end-users of the KivaKit.
 * </p>
 * <p>
 * <b>Registration</b>
 * </p>
 * <p>
 * Services can register themselves by calling {@link #register(Service)}. The application identifier uniquely
 * identifies the registering application on the local host. The {@link Application} and {@link Server} classes produce
 * such identifiers with the {@link Application#identifier()} method. In {@link Application} and {@link Server} classes,
 * the convenience method {@link #register(Scope, ServiceType, ServiceMetadata)} can be used. If the KivaKit application
 * framework is not being utilized, any unique string can be used to identify the registrant. The service type specifies
 * an identifier for the kind of service being registered, typically a value supplied as a service interface constant
 * value. When successful, the register() method will return a {@link Service} object that is bound to a free port in
 * the ephemeral port range.
 * <p>
 * <b>Lookup</b>
 * <p>
 * The following methods can be used to look up registered services within a given {@link Scope} (local host, network,
 * cluster or on a specific host):
 * <ul>
 *     <li>{@link #discoverApplications(Scope)} - A list of applications that have registered service(s) within the given scope</li>
 *     <li>{@link #discoverPortService(Port)} - Find the service running on the given port (and host since {@link Port} specifies host)</li>
 *     <li>{@link #discoverServices(Scope)} - All services within the given scope</li>
 *     <li>{@link #discoverServices(Scope, ServiceType)} - All services of the given type within the given scope</li>
 *     <li>{@link #discoverServices(Scope, ApplicationIdentifier)} - All services belonging to the given application within the given scope</li>
 *     <li>{@link #discoverServices(Scope, ApplicationIdentifier, ServiceType)} - A specific application service within the given scope</li>
 * </ul>
 * <p>
 * <b>Example</b>
 * </p>
 * A service can be bound by application A by calling register() on a {@link ServiceRegistryClient}:
 * <pre>
 *     public static final Service.SearchType SERVICE = new Service.SearchType("example-server");
 *     public static final Identifier APPLICATION_A = new Identifier("application-a");
 *
 *         [...]
 *
 *     var service = client.register(APPLICATION_A, SERVICE);
 *     var port = service.port();
 * </pre>
 * <p>
 * and application B can do the same thing, registering its own server on a different port:
 * </p>
 * <pre>
 *     public static final Identifier APPLICATION_B = new Identifier("application-b");
 *
 *         [...]
 *
 *     var service = client.register(APPLICATION_B, SERVICE);
 *     var port = service.port();
 * </pre>
 * <p>
 * In this way each logical application will be assigned a different physical port for its server.
 * </p>
 * <p>
 * A third application, can then look up either application's web server without knowing the port to connect to in
 * advance:
 * </p>
 * <pre>
 *     var serverA = client.lookup(APPLICATION_A, SERVICE);
 *     var serverB = client.lookup(APPLICATION_B, SERVICE);
 * </pre>
 * <p>
 * <b>API Implementation Details</b>
 * </p>
 * <p>
 * For details on the implementation service registration, including service expiration and port leases,
 * see {@link LocalServiceRegistry}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see LocalServiceRegistry
 * @see Service
 * @see ServiceType
 * @see Scope
 * @see ApplicationIdentifier
 * @see Port
 * @see Result
 */
@SuppressWarnings("InfiniteLoopStatement")
@UmlClassDiagram(diagram = DiagramClient.class)
@UmlRelation(label = "returns", referent = Result.class)
@UmlRelation(label = "discovers applications", referent = ApplicationIdentifier.class)
@UmlRelation(label = "discovers services", referent = Provider.Service.class)
@UmlRelation(label = "searches within", referent = Scope.class)
public class ServiceRegistryClient extends BaseRepeater
{
    /** True if the client is connected */
    private boolean connected;

    /** Client settings */
    private final ServiceRegistryClientSettings settings;

    public ServiceRegistryClient()
    {
        settings = Settings.require(ServiceRegistryClientSettings.class);
    }

    /**
     * @return All applications that have registered a service within the given scope
     */
    public @NotNull
    Result<Set<ApplicationIdentifier>> discoverApplications(final Scope scope)
    {
        trace("Requesting $ applications from remote registry", scope);
        final var request = new DiscoverApplicationsRequest().scope(scope);
        final var result = request(scope, request, DiscoverApplicationsResponse.class).asResult();
        trace("Received from registry: $", result);
        return result;
    }

    /**
     * @return Any service running on the given port. Since a {@link Port} includes the host it is unique and only a
     * single service is returned since only one service can be running on a specific port on a specific host.
     */
    public @NotNull
    Result<Service> discoverPortService(final Port port)
    {
        trace("Looking up service on port $ with remote registry", port);
        final var request = new DiscoverPortServiceRequest().port(port);
        final var service = request(port.host().isLocal()
                ? Scope.localhost()
                : Scope.network(), request, DiscoverPortServiceResponse.class).asResult();
        trace("Service on port $: ", port, service);
        return service.failed() ? Result.failed(service.why()) : Result.succeeded(service.get());
    }

    /**
     * @return All services registered by the given application within the given scope
     */
    public @NotNull
    Result<Set<Service>> discoverServices(final Scope scope, final ApplicationIdentifier application)
    {
        trace("Discovering $ services of $", scope, application);
        final var request = new DiscoverServicesRequest()
                .scope(scope)
                .type(APPLICATION_SERVICES)
                .application(application);

        final var result = request(scope, request, DiscoverServicesResponse.class).asResult();
        trace("Discovered services: $", result);
        return result;
    }

    /**
     * @return All services registered with this registry within the given scope
     */
    public @NotNull
    Result<Set<Service>> discoverServices(final Scope scope)
    {
        trace("Discovering all $ services", scope);
        final var request = new DiscoverServicesRequest()
                .scope(scope)
                .type(ALL_SERVICES);

        final var result = request(scope, request, DiscoverServicesResponse.class).asResult();
        trace("Discovered services: $", result);
        return result;
    }

    /**
     * @return All application services of the given type registered within the given scope
     */
    public @NotNull
    Result<Set<Service>> discoverServices(
            final Scope scope, final ApplicationIdentifier application, final ServiceType type)
    {
        trace("Discovering $ $ services of $ in remote registry", scope, type, application);
        final var request = new DiscoverServicesRequest()
                .scope(scope)
                .application(application)
                .serviceType(type);

        final var result = request(scope, request, DiscoverServicesResponse.class).asResult();
        trace("Discovered services: $", result);
        return result;
    }

    /**
     * @return All services of the given type that have been registered within the given scope
     */
    public @NotNull
    Result<Set<Service>> discoverServices(final Scope scope, final ServiceType type)
    {
        trace("Discovering $ $ services in remote registry", scope, type);
        final var request = new DiscoverServicesRequest()
                .scope(scope)
                .type(SERVICES_OF_TYPE)
                .serviceType(type);

        final var result = request(scope, request, DiscoverServicesResponse.class).asResult();
        trace("Discovered services: $", result);
        return result;
    }

    /**
     * Registers a {@link Service}, returning an updated {@link Service} object that has been bound to a unique port on
     * the local host and which has a lease on that port for a few minutes. The resulting registration information will
     * be immediately propagated to the {@link NetworkServiceRegistry} allowing the given service to be located on the
     * network after calling this method.
     *
     * @param service The service to register
     * @return The registered service, bound to a port
     */
    public @NotNull
    Result<Service> register(final Service service)
    {
        // Register the service
        trace("Registering with local registry: $", service);
        service.health(JavaVirtualMachine.local().health());
        final var request = new RegisterServiceRequest(service);
        final var result = request(Scope.localhost(), request, RegisterServiceResponse.class).asResult();
        if (result.succeeded())
        {
            final var registered = result.get();
            if (registered != null)
            {
                // then update the registration (renewing the lease) regularly in the background.
                KivaKitThread.run(this, "ServiceRegistryLeaseUpdater", () ->
                {
                    while (true)
                    {
                        ServiceRegistry.settings().serviceLeaseRenewalFrequency().duration().sleep();
                        trace("Renewing lease on registered service: $", registered);
                        final var health = JavaVirtualMachine.local().health();
                        if (health != null)
                        {
                            registered.health(health.update());
                        }
                        final var renewal = new RenewServiceRequest(registered);
                        final var renewed = request(Scope.localhost(), renewal, RenewServiceResponse.class).asResult();
                        trace("Remote registry renewed service: $", renewed);
                    }
                });
                trace("Local registry bound service: $", result);
            }
            else
            {
                warning("Unable to register service: $", service);
            }
        }

        return result;
    }

    /**
     * Convenience method that registers a service of the given type using the current KivaKit {@link Application} or
     * {@link Server} to fill in the service version and to create an application identifier and service description. If
     * this method is called from an application that is not using the kivakit-core-application base classes, a best
     * effort is made to fill in service registration details.
     *
     * @param scope The scope within which the service should be visible
     * @param serviceType A unique identifier for the type of service used in search/discovery
     * @param metadata Metadata for the service. If a description is not provided, one will be generated. The KivaKit
     * version and service version will be automatically filled in from the {@link Application} or {@link Server} that
     * is using this client.
     * @return The registered service, or null if it was not possible to do so due to network or server error.
     */
    public Result<Service> register
    (
            final Scope scope,
            final ServiceType serviceType,
            final ServiceMetadata metadata
    )
    {
        // Get the current kivakit application, if any
        final var application = Application.get();

        // and this process' id
        final var pid = OperatingSystem.get().processIdentifier();

        // then compose an identifier for the service application
        final var applicationIdentifier = application == null
                ? new ApplicationIdentifier("Unknown (pid " + OperatingSystem.get().processIdentifier() + ")")
                : application.identifier();

        // and a name to use in the description
        final var name = application == null ? "unknown process with pid " + pid : application.name();

        // and finally, add to the metadata
        if (metadata.description() == null)
        {
            metadata.description("Service '" + serviceType + "' for " + name);
        }
        metadata.kivakitVersion(KivaKit.get().version());
        metadata.version(version());

        // and register the service
        return register(new Service()
                .application(applicationIdentifier)
                .scope(scope)
                .type(serviceType)
                .port(Service.UNBOUND)
                .metadata(metadata));
    }

    /**
     * <b>Not public API</b>
     * <p>
     * Updates the given service in a network-wide registry of KivaKit services. Local service registries running on
     * individual hosts push service registration information to the network service registry via this method when
     * initial service registration and lease renewals occur. Services are transparently looked up in the network
     * service registry client when a network {@link Scope} is searched.
     * </p>
     *
     * @return True if the service was added
     */
    @NotNull
    public Result<Boolean> sendNetworkRegistryUpdate(final Service service)
    {
        // The client is used by the local service registry to update the network service registry.
        // This method should not be called by any end-users.
        try
        {
            if (service.isBound())
            {
                trace("Updating network service registry for: $", service);
                final var request = new NetworkRegistryUpdateRequest().service(service);
                final var result = request(Scope.network(), request, NetworkRegistryUpdateResponse.class).asResult();
                trace("Updated network service registry: $", result);
                return result;
            }
            else
            {
                warning("Cannot send update on unbound service: $", service);
                return Result.succeeded(false);
            }
        }
        catch (final Exception | AssertionError e)
        {
            return Result.succeeded(false).problem("Unable to update service $ int network registry: $", service.descriptor(), e.getMessage());
        }
    }

    /**
     * @return The version of this service registry client
     */
    public Version version()
    {
        return Settings.require(ServiceRegistrySettings.class).version();
    }

    /**
     * @return The connected client
     */
    private synchronized ServiceRegistryClient connectToLocalRegistry()
    {
        // If the client is not yet connected to the local registry,
        if (!connected)
        {
            // get the local port and if it is available, the registry is not running,
            final var port = ServiceRegistry.local();
            if (port.isAvailable())
            {
                // so launch the service from remote storage
                trace("Connecting client to $", port);
                final var local = Folder.kivakitHome()
                        .folder("kivakit-service/server/target")
                        .file("kivakit-service-registry-" + KivaKit.get().version() + ".jar");
                final var jar = settings.serverJar();
                trace("Launching $", jar);
                listenTo(new JarLauncher())
                        .processType(DETACHED)
                        .source(local)
                        .source(jar)
                        .run();

                // and wait until the port is no longer available, which means that the server is ready
                trace("Waiting for registry to start up");
                while (port.isAvailable())
                {
                    Duration.seconds(1).sleep();
                }
                trace("Registry is started");
            }
            connected = true;
        }
        return this;
    }

    private Gson gson()
    {
        return new ServiceRegistryGsonFactory().newInstance();
    }

    /**
     * Connects to the appropriate server for the given request scope, sends a request object and returns the response.
     */
    private <T, Response extends BaseResponse<T>> Response request(
            final Scope scope, final BaseRequest request, final Class<Response> responseType)
    {
        // If we're searching locally,
        if (scope.isLocal())
        {
            // connect to the local registry, possibly launching it if it isn't running,
            connectToLocalRegistry();
        }

        // then create a new Jersey client,
        final var timeout = settings.accessTimeout();
        final Client client = JerseyClientBuilder.newBuilder()
                .connectTimeout((long) timeout.asSeconds(), TimeUnit.SECONDS)
                .build();

        // turn the request into JSON,
        final var requestJson = gson().toJson(request);
        trace("Sending JSON ${class} to $/$:\n$", request.getClass(), ServiceRegistry.settings().restApiPath(),
                request.path(), requestJson);

        // get the appropriate server to contact based on the scope,
        final Port server;
        switch (scope.type())
        {
            case LOCALHOST:
                server = ServiceRegistry.local();
                break;

            case NETWORK:
            case CLUSTER:
                server = ServiceRegistry.network();
                break;

            default:
                return unsupported("Scope type '$' is not supported", scope.type());
        }

        // If the server host can be resolved,
        if (server.host().isResolvable())
        {
            // compose and post a request to the server,
            try
            {
                final var entity = Entity.entity(requestJson, "application/json");
                final var path = server
                        .path(ServiceRegistry.settings().restApiPath())
                        .withChild(request.path());
                trace("Posting $ to $", request.getClass().getSimpleName(), path);
                final var jaxResponse = client
                        .target(path.toString())
                        .request("application/json")
                        .post(entity, javax.ws.rs.core.Response.class);

                // read the JSON response,
                final var responseJson = jaxResponse.readEntity(String.class);

                // and convert the response to an object.
                final var response = gson().fromJson(responseJson, responseType);
                trace("Received JSON ${class}:\n$", response.getClass(), responseJson);

                return response;
            }
            catch (final Exception e)
            {
                if (e instanceof ProcessingException && e.getCause() instanceof ConnectException)
                {
                    warning(e, "Unable to connect to host $", server);
                }
                else
                {
                    problem(e, "Failure trying to connect to $", server);
                }
            }
        }

        // If the host is not resolvable or an exception occurred, so fail
        final var response = (Response) Type.forClass(responseType).newInstance();
        response.problem("Unable to connect to $", server);
        problem("Could not resolve ${lower} registry on host $", scope, server.host().name());
        return response;
    }
}

