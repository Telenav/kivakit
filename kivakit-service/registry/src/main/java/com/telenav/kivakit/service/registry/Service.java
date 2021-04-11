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

package com.telenav.kivakit.service.registry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.core.application.Application;
import com.telenav.kivakit.core.application.ApplicationIdentifier;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.core.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachineHealth;
import com.telenav.kivakit.core.network.core.Host;
import com.telenav.kivakit.core.network.core.Port;
import com.telenav.kivakit.service.registry.project.lexakai.annotations.DiagramRegistry;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter.Format.SINGLE_LINE;

/**
 * A logical service of a particular type that can be registered by an application with a local or network service
 * registry, and subsequently discovered by client registry searches.
 *
 * <p><b>Service Registration and Expiration</b></p>
 *
 * <p>
 * A service of some {@link ServiceType}, with {@link ServiceMetadata} describing its function, may be registered by an
 * {@link Application} in order to bind it to an unused port on the local host and make it discoverable by clients. When
 * registered and bound, a service is visible within a particular {@link Scope} so long as the service renews its lease
 * with the registry. If the lease expires, the service is removed from the registry. Lease renewal is handled
 * automatically for applications that register a service using the service registration client.
 * </p>
 *
 * <p><b>Service Discovery</b></p>
 *
 * <p>
 * Registered services can be discovered using a service registration client, which communicates using a REST protocol
 * with the registry. The criteria that are used for service discovery is detailed in ServiceRegistryClient.
 * </p>
 *
 * <p><b>Service Properties</b></p>
 *
 * <p>
 * A service has the following properties:
 * </p>
 * <ul>
 *     <li>{@link #application()} - Identifies the application registering the service</li>
 *     <li>{@link #scope()} - The scope where the service is visible</li>
 *     <li>{@link #metadata()} - Metadata describing the service</li>
 *     <li>{@link #type()} - Service type identifier for use in searching for services</li>
 *     <li>{@link #port()} - The port that the service is bound to or {@link #UNBOUND}</li>
 *     <li>{@link #renewedAt()} - The most recent service lease renewal time</li>
 *     <li>{@link #health()} - Information about the health of the server running the service</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@Schema(description = "A particular type of service belonging to an application and running on a TCP/IP port on a host. "
        + "There can be multiple services running on a network, on a cluster, on a host and within an application. "
        + "Each service has metadata describing itself and declares a scope within which it should be visible. ")
@UmlClassDiagram(diagram = DiagramRegistry.class)
@UmlExcludeSuperTypes
@LexakaiJavadoc(complete = true)
public class Service implements Comparable<Service>, AsString
{
    public static final Port UNBOUND = Host.local().port(0);

    @JsonProperty
    @Schema(description = "The application that is running the service",
            required = true)
    @UmlAggregation
    private ApplicationIdentifier application;

    @JsonProperty
    @Schema(description = "The scope that the service is visible to",
            required = true)
    @UmlAggregation(label = "visibility")
    private Scope scope;

    @JsonProperty
    @Schema(description = "Metadata describing the service",
            required = true)
    @UmlAggregation
    private ServiceMetadata metadata;

    @JsonProperty
    @Schema(description = "The type of service",
            required = true)
    @UmlAggregation(label = "provided service")
    private ServiceType type;

    @JsonProperty
    @Schema(description = "The port that has been allocated for use by the service on the local host by the local KivaKit registry")
    @UmlAggregation(label = "allocated port")
    private Port port;

    @UmlAggregation(label = "health status")
    private JavaVirtualMachineHealth health;

    private long renewedAt;

    public Service()
    {
    }

    public Service application(final ApplicationIdentifier application)
    {
        this.application = application;
        return this;
    }

    @KivaKitIncludeProperty
    public ApplicationIdentifier application()
    {
        return application;
    }

    @Override
    public String asString(final StringFormat format)
    {
        switch (format.identifier())
        {
            case StringFormat.LOG_IDENTIFIER:
                return new ObjectFormatter(this).toString(SINGLE_LINE);

            default:
                return toString();
        }
    }

    @Override
    public int compareTo(@NotNull final Service that)
    {
        return Long.compare(port.number(), that.port.number());
    }

    public String descriptor()
    {
        return port + "-" + type;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Service)
        {
            final Service that = (Service) object;
            return application.equals(that.application) && type.equals(that.type) && port.equals(that.port());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(application, type, port);
    }

    @KivaKitIncludeProperty
    public JavaVirtualMachineHealth health()
    {
        return health;
    }

    public Service health(final JavaVirtualMachineHealth health)
    {
        this.health = health;
        return this;
    }

    public String hostAndApplication()
    {
        final var host = port().host();
        final var hostName = host.isLocal()
                ? ""
                : host.name() + ":";

        return hostName + application();
    }

    @NotNull
    public String hostApplicationAndPort()
    {
        return hostAndApplication() + ":" + port.number();
    }

    @JsonIgnore
    public boolean isBound()
    {
        return !isUnbound();
    }

    public boolean isSame(final Service that)
    {
        return application.equals(that.application) && type.equals(that.type);
    }

    /**
     * @return True if it has been too long since this service was renewed and it is in danger of being expired "soon"
     */
    @JsonIgnore
    public boolean isStale()
    {
        return renewedAt().elapsedSince().isGreaterThan(ServiceRegistry.settings()
                .serviceLeaseRenewalFrequency().duration().times(1.5));
    }

    @JsonIgnore
    public boolean isUnbound()
    {
        return UNBOUND.equals(port);
    }

    public ServiceMetadata metadata()
    {
        if (metadata == null)
        {
            metadata = new ServiceMetadata();
        }
        return metadata;
    }

    public Service metadata(final ServiceMetadata metadata)
    {
        this.metadata = metadata;
        return this;
    }

    public Service port(final Port port)
    {
        this.port = port;
        return this;
    }

    /**
     * @return The port that this service is bound to
     */
    @KivaKitIncludeProperty
    public Port port()
    {
        return port;
    }

    public Service renewedAt(final Time renewedAt)
    {
        this.renewedAt = renewedAt.asMilliseconds();
        return this;
    }

    @KivaKitIncludeProperty
    public Time renewedAt()
    {
        return Time.milliseconds(renewedAt);
    }

    public Scope scope()
    {
        return scope;
    }

    public Service scope(final Scope scope)
    {
        this.scope = scope;
        return this;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString(SINGLE_LINE);
    }

    public Service type(final ServiceType type)
    {
        this.type = type;
        return this;
    }

    /**
     * @return The type of service
     */
    @KivaKitIncludeProperty
    public ServiceType type()
    {
        return type;
    }
}
