package com.telenav.kivakit.service.registry.protocol.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.protocol.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;

import static com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;
import static com.telenav.kivakit.service.registry.protocol.ServiceRegistryProtocol.NETWORK_REGISTRY_UPDATE;

/**
 * A request from a local service registry to the network service registry propagating initial registration information
 * for a new service or renewing the lease of an existing service.
 *
 * @author jonathanl (shibo)
 */
@Schema
public class NetworkRegistryUpdateRequest extends BaseRequest
{
    @JsonProperty
    private Service service;

    @Override
    public String path()
    {
        return NETWORK_REGISTRY_UPDATE;
    }

    public NetworkRegistryUpdateRequest service(final Service service)
    {
        this.service = service;
        return this;
    }

    @KivaKitIncludeProperty
    public Service service()
    {
        return service;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString(MULTILINE);
    }
}
