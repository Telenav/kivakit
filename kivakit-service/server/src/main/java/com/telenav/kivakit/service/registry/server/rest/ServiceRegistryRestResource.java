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

package com.telenav.kivakit.service.registry.server.rest;

import com.telenav.kivakit.core.configuration.settings.Settings;
import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.messages.Result;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.ServiceRegistry;
import com.telenav.kivakit.service.registry.ServiceRegistrySettings;
import com.telenav.kivakit.service.registry.protocol.ServiceRegistryProtocol;
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
import com.telenav.kivakit.service.registry.server.ServiceRegistryServer;
import com.telenav.kivakit.web.jersey.BaseRestResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author jonathanl (shibo)
 */
@OpenAPIDefinition(

        info = @Info(

                title = "KivaKit Service Registry",
                description = "Registry of KivaKit services. See KivaKit module kivakit.service for details.",
                version = "8.0.7",

                contact = @Contact(
                        name = "Jonathan Locke",
                        url = "https://www.linkedin.com/in/jonathan-locke-3892ba/",
                        email = "jonathanl@telenav.com"
                ),

                license = @License(
                        name = "Copyright 2020 Telenav - All rights reserved.",
                        url = "http://www.telenav.com"
                )

        )

)
@Path("api/v8")
public class ServiceRegistryRestResource extends BaseRestResource
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /** The service registry to query and update */
    private final ServiceRegistry registry = ServiceRegistryServer.get().registry();

    //----------------------------------------------------------------------------------------------
    // Discover Applications
    //----------------------------------------------------------------------------------------------

    /**
     * Locates the set of applications within the given scope.
     *
     * @return The set of applications that were discovered
     */
    @POST
    @Path(ServiceRegistryProtocol.DISCOVER_APPLICATIONS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    @Operation(operationId = ServiceRegistryProtocol.DISCOVER_APPLICATIONS,
               method = "POST",
               description = "Locates all applications within the given search scope")

    @ApiResponses(

            @ApiResponse(responseCode = "200",
                         description = "Success",
                         content = @Content(schema = @Schema(implementation = DiscoverApplicationsResponse.class))
            )
    )

    public DiscoverApplicationsResponse onDiscoverApplications
    (
            @Parameter(name = "request",
                       description = "The scope within which to locate applications",
                       required = true,
                       schema = @Schema(implementation = DiscoverApplicationsRequest.class))

            final DiscoverApplicationsRequest request
    )
    {
        narrate("Received discover applications request: $", request);
        final var scope = request.scope();
        final var response = new DiscoverApplicationsResponse();
        response.result(registry.discoverApplications(scope));
        narrate("Returning discover applications response: $", response);
        return response;
    }

    //----------------------------------------------------------------------------------------------
    // Discover Port Service
    //----------------------------------------------------------------------------------------------

    /**
     * Locates any service that may be running on the provided port. There is no scope for this request because a port
     * is globally unique since it contains a host and port number and only one service can run on a port.
     *
     * @return The service running on the given port or null if the port is not bound to a service
     */
    @POST
    @Path(ServiceRegistryProtocol.DISCOVER_PORT_SERVICE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    @Operation(operationId = ServiceRegistryProtocol.DISCOVER_PORT_SERVICE,
               method = "POST",
               description = "Locates any service that may be running on a particular port on a host")

    @ApiResponses(

            @ApiResponse(responseCode = "200",
                         description = "Success",
                         content = @Content(schema = @Schema(implementation = DiscoverPortServiceResponse.class))
            )

    )

    public DiscoverPortServiceResponse onDiscoverPortService
    (
            @Parameter(name = "request",
                       description = "The specific host and port to examine",
                       required = true,
                       schema = @Schema(implementation = DiscoverPortServiceRequest.class))

            final DiscoverPortServiceRequest request
    )
    {
        narrate("Received discover port service request $:", request);
        final var response = new DiscoverPortServiceResponse();
        response.result(registry.discoverPortService(request.port()));
        narrate("Returning discover port service response $:", response);
        return response;
    }

    //----------------------------------------------------------------------------------------------
    // Discover Services
    //----------------------------------------------------------------------------------------------

    /**
     * Locates all the services within the given scope using criteria in the {@link DiscoverServicesRequest} object.
     *
     * @return The set of services that were discovered
     */
    @POST
    @Path(ServiceRegistryProtocol.DISCOVER_SERVICES)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    @Operation(operationId = ServiceRegistryProtocol.DISCOVER_SERVICES,
               method = "POST",
               description = "Locates services matching the given criteria")

    @ApiResponses(

            @ApiResponse(responseCode = "200",
                         description = "Success",
                         content = @Content(schema = @Schema(implementation = DiscoverServicesResponse.class))
            )

    )

    public DiscoverServicesResponse onDiscoverServices
    (
            @Parameter(name = "request",
                       description = "The criteria for services to locate",
                       required = true,
                       schema = @Schema(implementation = DiscoverServicesRequest.class))

            final DiscoverServicesRequest request
    )
    {
        narrate("Received discover services request: $", request);
        final var response = new DiscoverServicesResponse();
        switch (request.type())
        {
            case ALL_SERVICES:
                response.result(registry.discoverServices());
                break;

            case APPLICATION_SERVICES:
                response.result(registry.discoverServices(request.application()));
                break;

            case SERVICES_OF_TYPE:
                response.result(registry.discoverServices(request.serviceType()));
                break;

            default:
                return null;
        }
        narrate("Returning discover services response: $", response);
        return response;
    }

    //----------------------------------------------------------------------------------------------
    // Network Registry Update
    //----------------------------------------------------------------------------------------------

    /**
     * <b>Not public API</b>
     * <p>
     * On a {@link NetworkServiceRegistry} (only), this method is called by {@link LocalServiceRegistry} instances on
     * different hosts on the network to update service registration information.
     * </p>
     */
    @POST
    @Path(ServiceRegistryProtocol.NETWORK_REGISTRY_UPDATE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation
            (
                    method = "GET",
                    description = "Updates the network registry with changes from a local registry",
                    hidden = true
            )
    public NetworkRegistryUpdateResponse onNetworkRegistryUpdate(final NetworkRegistryUpdateRequest request)
    {
        narrate("Received network registry update: $", request);
        final var service = request.service();
        final boolean succeeded;
        if (service.isBound())
        {
            succeeded = registry.addOrUpdate(service).succeeded();
        }
        else
        {
            LOGGER.warning("Client tried to update an unbound service: $", service);
            succeeded = false;
        }

        final var response = new NetworkRegistryUpdateResponse();
        response.result(Result.succeeded(succeeded));
        narrate("Returning network registry response: $", response);
        return response;
    }

    //----------------------------------------------------------------------------------------------
    // Register Service
    //----------------------------------------------------------------------------------------------

    /**
     * Called to register a service on the local host. The resulting registration information will be propagated to any
     * network registry by issuing a {@link NetworkRegistryUpdateRequest}.
     *
     * @param request The service to register
     * @return The new service entry in the registry, specifying the port that can be used for the service
     */
    @POST
    @Path(ServiceRegistryProtocol.REGISTER_SERVICE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    @Operation(operationId = ServiceRegistryProtocol.REGISTER_SERVICE,
               method = "POST",
               description = "Registers the KivaKit service described by the request")

    @ApiResponses(

            @ApiResponse(responseCode = "200",
                         description = "Success",
                         content = @Content(schema = @Schema(implementation = RegisterServiceResponse.class))
            )

    )
    public RegisterServiceResponse onRegisterService
    (
            @Parameter(name = "request",
                       description = "The service to register",
                       required = true,
                       schema = @Schema(implementation = RegisterServiceRequest.class))

            final RegisterServiceRequest request
    )
    {
        narrate("Received register service request: $", request);
        final var service = registry.register(request.service());
        final var response = new RegisterServiceResponse();
        response.result(service);
        narrate("Returning register service response: $", response);
        return response;
    }

    //----------------------------------------------------------------------------------------------
    // Renew Service
    //----------------------------------------------------------------------------------------------

    /**
     * <b>Not public API</b>
     * <p>
     * Called to renew the lease on a service on the local machine. The resulting renewal information will be propagated
     * to any network registry by issuing a {@link NetworkRegistryUpdateRequest}.
     *
     * @param request The service to renew
     * @return The renewed service with an updated {@link Service#renewedAt()} time.
     */
    @POST
    @Path(ServiceRegistryProtocol.RENEW_SERVICE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation
            (
                    method = "POST",
                    description = "Renews the service's registry entry and port lease",
                    hidden = true
            )
    public RenewServiceResponse onRenewService(final RenewServiceRequest request)
    {
        narrate("Received renew service request: $", request);
        final var service = registry.renew(request.service());
        final var response = new RenewServiceResponse();
        response.result(service);
        narrate("Returning renew service response: $", response);
        return response;
    }

    //----------------------------------------------------------------------------------------------
    // Version
    //----------------------------------------------------------------------------------------------

    /**
     * @return The version of this KivaKit service registry
     */
    @GET
    @Path(ServiceRegistryProtocol.SHOW_VERSION)
    @Produces(MediaType.TEXT_PLAIN)
    @Operation
            (
                    method = "GET",
                    description = "Gets the current version of this service registry server"
            )
    public Response onVersion()
    {
        final String output = "KivaKit Service Registry "
                + Settings.require(ServiceRegistrySettings.class).version()
                + "\n"
                + "KivaKit "
                + KivaKit.get().version()
                + " (" + KivaKit.get().build() + ")";

        return Response.status(200)
                .entity(output)
                .build();
    }

    private void narrate(final String message, final Object... arguments)
    {
        LOGGER.narrate(message, arguments);
    }
}
