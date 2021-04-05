package com.telenav.kivakit.service.registry.protocol;

import com.telenav.kivakit.core.configuration.settings.Settings;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.values.version.Version;
import com.telenav.kivakit.service.registry.ServiceRegistrySettings;
import com.telenav.kivakit.service.registry.project.lexakai.annotations.DiagramRest;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramRest.class)
public abstract class BaseRequest
{
    public abstract String path();

    @KivaKitIncludeProperty
    public Version version()
    {
        return Settings.require(ServiceRegistrySettings.class).version();
    }
}
