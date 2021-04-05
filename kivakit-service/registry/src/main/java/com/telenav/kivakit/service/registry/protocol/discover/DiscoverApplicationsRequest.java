package com.telenav.kivakit.service.registry.protocol.discover;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.service.registry.Scope;
import com.telenav.kivakit.service.registry.project.lexakai.annotations.DiagramRest;
import com.telenav.kivakit.service.registry.protocol.BaseRequest;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.oas.annotations.media.Schema;

import static com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;
import static com.telenav.kivakit.service.registry.protocol.ServiceRegistryProtocol.DISCOVER_APPLICATIONS;

/**
 * Requests the applications within a given {@link Scope}.
 *
 * @author jonathanl (shibo)
 */
@Schema(description = "A request to locate all KivaKit applications within the given scope")
@UmlClassDiagram(diagram = DiagramRest.class)
public class DiscoverApplicationsRequest extends BaseRequest
{
    @JsonProperty
    @Schema(description = "The scope of the search",
            required = true)
    private Scope scope;

    @Override
    public String path()
    {
        return DISCOVER_APPLICATIONS;
    }

    public Scope scope()
    {
        return scope;
    }

    public DiscoverApplicationsRequest scope(final Scope scope)
    {
        this.scope = scope;
        return this;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString(MULTILINE);
    }
}
