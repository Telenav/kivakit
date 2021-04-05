package com.telenav.kivakit.service.registry.protocol.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.service.registry.project.lexakai.annotations.DiagramRest;
import com.telenav.kivakit.service.registry.protocol.BaseResponse;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.oas.annotations.media.Schema;

import static com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;

/**
 * @author jonathanl (shibo)
 */
@Schema
@UmlClassDiagram(diagram = DiagramRest.class)
public class NetworkRegistryUpdateResponse extends BaseResponse<Boolean>
{
    @JsonProperty
    private boolean added;

    public NetworkRegistryUpdateResponse added(final boolean added)
    {
        this.added = added;
        return this;
    }

    @KivaKitIncludeProperty
    public boolean added()
    {
        return added;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString(MULTILINE);
    }

    @Override
    protected void value(final Boolean value)
    {
        added = value;
    }

    @Override
    protected Boolean value()
    {
        return added;
    }
}
