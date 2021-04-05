package com.telenav.kivakit.service.registry.protocol.renew;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.protocol.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;

import static com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;
import static com.telenav.kivakit.service.registry.protocol.ServiceRegistryProtocol.RENEW_SERVICE;

/**
 * A request from an application to renew the lease for a service
 *
 * @author jonathanl (shibo)
 */
@Schema
public class RenewServiceRequest extends BaseRequest
{
    /** The service to renew */
    @JsonProperty
    private Service service;

    public RenewServiceRequest(final Service service)
    {
        this.service = service;
    }

    protected RenewServiceRequest()
    {
    }

    @Override
    public String path()
    {
        return RENEW_SERVICE;
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
