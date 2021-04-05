package com.telenav.kivakit.service.registry;

import com.telenav.kivakit.core.kernel.language.values.identifier.StringIdentifier;
import com.telenav.kivakit.service.registry.project.lexakai.annotations.DiagramRegistry;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * An identifier for a particular kind of service
 *
 * @author jonathanl (shibo)
 */
@Schema(description = "A value that uniquely identifies the type of service. "
        + "The use of domain-name qualified names is encouraged to avoid conflicts.",
        example = "com-telenav-kivakit-server-log")
@UmlClassDiagram(diagram = DiagramRegistry.class)
@UmlExcludeSuperTypes
public class ServiceType extends StringIdentifier
{
    public ServiceType(final String identifier)
    {
        super(identifier);
    }

    protected ServiceType()
    {
    }
}
