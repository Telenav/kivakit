package com.telenav.kivakit.service.registry.protocol.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.protocol.BaseRequest;
import com.telenav.kivakit.service.registry.protocol.ServiceRegistryProtocol;
import io.swagger.v3.oas.annotations.media.Schema;

import static com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;

/**
 * A request from an application (usually a server) to register a service and begin leasing a port for it.
 *
 * @author jonathanl (shibo)
 */
@Schema
public class RegisterServiceRequest extends BaseRequest
{
    /** The potentially unbound service to register or renew */
    @JsonProperty
    @Schema(description = "The service that should be registered and allocated a port",
            required = true)
    private Service service;

    public RegisterServiceRequest(final Service service)
    {
        this.service = service;
    }

    protected RegisterServiceRequest()
    {
    }

    @Override
    public String path()
    {
        return ServiceRegistryProtocol.REGISTER_SERVICE;
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
