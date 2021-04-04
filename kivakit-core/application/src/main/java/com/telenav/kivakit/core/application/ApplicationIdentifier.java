package com.telenav.kivakit.core.application;

import com.telenav.kivakit.core.application.project.lexakai.diagrams.DiagramApplication;
import com.telenav.kivakit.core.kernel.language.values.identifier.StringIdentifier;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A unique string identifier for a KivaKit {@link Application}.
 *
 * @author jonathanl (shibo)
 */
@Schema(description = "A unique identifier for a KivaKit application, provided by the Application base class",
        example = "ServiceRegistryServer")
@UmlClassDiagram(diagram = DiagramApplication.class)
@UmlExcludeSuperTypes
public class ApplicationIdentifier extends StringIdentifier
{
    public ApplicationIdentifier(final String identifier)
    {
        super(identifier);
    }

    protected ApplicationIdentifier()
    {
    }
}
