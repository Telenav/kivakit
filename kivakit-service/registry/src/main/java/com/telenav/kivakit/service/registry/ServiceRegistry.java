package com.telenav.kivakit.service.registry;

import com.telenav.kivakit.core.application.ApplicationIdentifier;
import com.telenav.kivakit.core.configuration.settings.Settings;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.core.kernel.messaging.Repeater;
import com.telenav.kivakit.core.kernel.messaging.messages.Result;
import com.telenav.kivakit.core.network.core.Host;
import com.telenav.kivakit.core.network.core.Port;
import com.telenav.kivakit.service.registry.project.lexakai.annotations.DiagramRegistry;
import com.telenav.kivakit.service.registry.registries.BaseServiceRegistry;
import com.telenav.kivakit.service.registry.registries.LocalServiceRegistry;
import com.telenav.kivakit.service.registry.registries.NetworkServiceRegistry;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlNote;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * <b>Not public API</b>
 * <p>
 * For details on what service registries are and how they work, refer to ServiceRegistryClient. The client is a REST +
 * JSON proxy to {@link LocalServiceRegistry} and {@link NetworkServiceRegistry} processes which provides a public API
 * to the key methods in this class. Further details on service registry implementation are also available in {@link
 * BaseServiceRegistry}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see BaseServiceRegistry
 * @see LocalServiceRegistry
 * @see Service
 * @see ServiceType
 * @see ApplicationIdentifier
 * @see Port
 * @see Result
 */
@UmlClassDiagram(diagram = DiagramRegistry.class)
@UmlNote(text = "Use ServiceRegistryClient to register and discover services")
@UmlNotPublicApi
public interface ServiceRegistry extends Repeater
{
    /**
     * <b>Not public API</b>
     */
    static Port local()
    {
        return port(Host.loopback());
    }

    /**
     * <b>Not public API</b>
     *
     * @return The service registry for the network (normally some kind of intranet)
     */
    static Port network()
    {
        final var host = JavaVirtualMachine.property
                (
                        "KIVAKIT_NETWORK_SERVICE_REGISTRY_PORT",
                        "kivakit-network-service-registry.mypna.com:23575"
                );

        return settings().networkServiceRegistryPort();
    }

    /**
     * <b>Not public API</b>
     */
    static Port port(final Host host)
    {
        return host.http(settings().localServiceRegistryPort());
    }

    static ServiceRegistrySettings settings()
    {
        return Settings.require(ServiceRegistrySettings.class);
    }

    /**
     * <b>Not public API</b>
     * <p>
     * Adds or updates registration information and renews the lease for the given service.
     */
    @NotNull Result<Boolean> addOrUpdate(final Service service);

    /**
     * @return All applications that have registered a service
     */
    @NotNull Result<Set<ApplicationIdentifier>> discoverApplications(Scope scope);

    /**
     * @return All of the hosts that have registered services
     */
    default Result<Set<Host>> discoverHosts()
    {
        final var result = new Result<Set<Host>>();
        final var services = discoverServices();
        if (services.succeeded())
        {
            final var hosts = new HashSet<Host>();
            services.get().forEach(service -> hosts.add(service.port().host()));
            result.set(hosts);
            return result;
        }
        return result.problem("Unable to find hosts");
    }

    /**
     * @return Any service running on the given port. Since a {@link Port} includes the host it is unique and only a
     * single service is returned since only one service can be running on a specific port on a specific host.
     */
    @NotNull Result<Service> discoverPortService(Port port);

    /**
     * Any application services of the given type
     */
    @NotNull Result<Set<Service>> discoverServices(ApplicationIdentifier application, ServiceType type);

    /**
     * All services registered by the given application
     */
    @NotNull Result<Set<Service>> discoverServices(ApplicationIdentifier application);

    /**
     * @return All services registered with this registry
     */
    @NotNull Result<Set<Service>> discoverServices();

    /**
     * All services of the given type that have been registered with this registry
     */
    @NotNull Result<Set<Service>> discoverServices(ServiceType type);

    /**
     * @return True if this is a {@link LocalServiceRegistry}
     */
    default boolean isLocal()
    {
        return this instanceof LocalServiceRegistry;
    }

    /**
     * @return True if this is a {@link NetworkServiceRegistry}
     */
    default boolean isNetwork()
    {
        return this instanceof NetworkServiceRegistry;
    }

    /**
     * Registers a {@link Service}, returning a {@link Service} object that has been bound to a unique physical port (on
     * a specific host) and which has a lease on that port for a few minutes. The resulting new registration information
     * will be immediately passed on to the {@link NetworkServiceRegistry}.
     * <p>
     * This method is only implemented by {@link LocalServiceRegistry} since the {@link NetworkServiceRegistry} is
     * updated through information propagated from local registries.
     * </p>
     *
     * @param service The service to register
     * @return The registered service, bound to a port
     */
    default @NotNull Result<Service> register(final Service service)
    {
        return unsupported();
    }

    /**
     * Renews the lease on a service that has been registered with {@link #register(Service)} on the local host. The
     * resulting renewal information will be passed on to the {@link NetworkServiceRegistry}.
     * <p>
     * This method is only implemented by {@link LocalServiceRegistry} since the {@link NetworkServiceRegistry} is
     * updated through information propagated from local registries.
     * </p>
     */
    default @NotNull Result<Service> renew(final Service service)
    {
        return unsupported();
    }
}
