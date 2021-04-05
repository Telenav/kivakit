package com.telenav.kivakit.service.registry.protocol.discover;

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
 * The service running on the port requested by a {@link DiscoverPortServiceRequest}
 *
 * @author jonathanl (shibo)
 */
@Schema(description = "The service that was found running on the given port")
@UmlClassDiagram(diagram = DiagramRest.class)
public class DiscoverPortServiceResponse extends BaseResponse<Service>
{
    @JsonProperty
    @Schema(description = "The service running on the given port, or null if no service was found")
    private Service service;

    @KivaKitIncludeProperty
    @KivaKitFormatProperty(format = StringFormat.LOG_IDENTIFIER)
    public Service service()
    {
        return service;
    }

    public DiscoverPortServiceResponse service(final Service service)
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
        return service();
    }
}
