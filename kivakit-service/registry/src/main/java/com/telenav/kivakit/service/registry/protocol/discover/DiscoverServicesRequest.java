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

package com.telenav.kivakit.service.registry.protocol.discover;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.core.application.ApplicationIdentifier;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.service.registry.Scope;
import com.telenav.kivakit.service.registry.ServiceType;
import com.telenav.kivakit.service.registry.project.lexakai.annotations.DiagramRest;
import com.telenav.kivakit.service.registry.protocol.BaseRequest;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.oas.annotations.media.Schema;

import static com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;
import static com.telenav.kivakit.service.registry.protocol.ServiceRegistryProtocol.DISCOVER_SERVICES;

/**
 * Requests information about registered services within a given scope. If the request type is {@link
 * SearchType#ALL_SERVICES}, the all services within the scope are returned. For {@link
 * SearchType#APPLICATION_SERVICES}, the services of a particular application are returned. And if the request type is
 * {@link SearchType#SERVICES_OF_TYPE}, then all the services of a given type are returned. The individual values that
 * must be provided as properties depend on the request type.
 *
 * @author jonathanl (shibo)
 */
@Schema(description = "A request to locate all services matching the given criteria")
@UmlClassDiagram(diagram = DiagramRest.class)
@LexakaiJavadoc(complete = true)
public class DiscoverServicesRequest extends BaseRequest
{
    /**
     * The type of search to perform
     */
    @Schema(description = "The type of search",
            required = true)
    @LexakaiJavadoc(complete = true)
    public enum SearchType
    {
        /** All registered services */
        ALL_SERVICES,

        /** The services provided by an application */
        APPLICATION_SERVICES,

        /** All services of a given type */
        SERVICES_OF_TYPE
    }

    @JsonProperty
    @Schema(description = "The scope of the search",
            required = true)
    private Scope scope;

    @JsonProperty
    @Schema(description = "The type of search to conduct: ALL_SERVICES, APPLICATION_SERVICES or SERVICES_OF_TYPE",
            required = true)
    private SearchType type;

    @JsonProperty
    @Schema(description = "The type of services to search for, if the search type is SERVICES_OF_TYPE")
    private ServiceType serviceType;

    @JsonProperty
    @Schema(description = "The application to search, if the search type is APPLICATION_SERVICES")
    private ApplicationIdentifier application;

    public DiscoverServicesRequest application(final ApplicationIdentifier application)
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
    public String path()
    {
        return DISCOVER_SERVICES;
    }

    public Scope scope()
    {
        return scope;
    }

    public DiscoverServicesRequest scope(final Scope scope)
    {
        this.scope = scope;
        return this;
    }

    public DiscoverServicesRequest serviceType(final ServiceType serviceType)
    {
        this.serviceType = serviceType;
        return this;
    }

    @KivaKitIncludeProperty
    public ServiceType serviceType()
    {
        return serviceType;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString(MULTILINE);
    }

    @KivaKitIncludeProperty
    public SearchType type()
    {
        return type;
    }

    public DiscoverServicesRequest type(final SearchType type)
    {
        this.type = type;
        return this;
    }
}
