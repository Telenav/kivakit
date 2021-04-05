package com.telenav.kivakit.service.registry.protocol.renew;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.core.kernel.language.strings.formatting.KivaKitFormatProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.project.lexakai.annotations.DiagramRest;
import com.telenav.kivakit.service.registry.protocol.BaseResponse;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.oas.annotations.media.Schema;

import static com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;

/**
 * The service with its renewedAt time updated.
 *
 * @author jonathanl (shibo)
 */
@Schema
@UmlClassDiagram(diagram = DiagramRest.class)
public class RenewServiceResponse extends BaseResponse<Service>
{
    @JsonProperty
    private Service service;

    @KivaKitIncludeProperty
    @KivaKitFormatProperty(format = StringFormat.LOG_IDENTIFIER)
    public Service service()
    {
        return service;
    }

    public RenewServiceResponse service(final Service service)
    {
        this.service = service;
        return this;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString(MULTILINE);
    }

    @Override
    protected void value(final Service value)
    {
        service = value;
    }

    @Override
    protected Service value()
    {
        return service;
    }
}
