package com.telenav.kivakit.service.registry.protocol.discover;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.core.kernel.language.collections.set.Sets;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.project.lexakai.annotations.DiagramRest;
import com.telenav.kivakit.service.registry.protocol.BaseResponse;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;

/**
 * The set of services that were found for a {@link DiscoverServicesRequest}.
 *
 * @author jonathanl (shibo)
 */
@Schema
@UmlClassDiagram(diagram = DiagramRest.class)
public class DiscoverServicesResponse extends BaseResponse<Set<Service>>
{
    @JsonProperty
    private Set<Service> services = new HashSet<>();

    public Service service()
    {
        return Sets.first(services);
    }

    public DiscoverServicesResponse services(final Set<Service> services)
    {
        this.services = services;
        return this;
    }

    @KivaKitIncludeProperty
    public Set<Service> services()
    {
        return services;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString(MULTILINE);
    }

    @Override
    protected void value(final Set<Service> value)
    {
        services = value;
    }

    @Override
    protected Set<Service> value()
    {
        return services();
    }
}
