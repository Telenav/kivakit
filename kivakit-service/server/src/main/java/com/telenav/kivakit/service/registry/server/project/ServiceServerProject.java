package com.telenav.kivakit.service.registry.server.project;

import com.telenav.kivakit.core.configuration.settings.Settings;
import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.language.values.version.Version;
import com.telenav.kivakit.core.kernel.project.Project;
import com.telenav.kivakit.service.registry.ServiceRegistrySettings;

/**
 * @author jonathanl (shibo)
 */
public class ServiceServerProject extends Project
{
    private static final Lazy<ServiceServerProject> singleton = Lazy.of(ServiceServerProject::new);

    public static ServiceServerProject get()
    {
        return singleton.get();
    }

    protected ServiceServerProject()
    {
    }

    @Override
    public Version version()
    {
        return Settings.require(ServiceRegistrySettings.class).version();
    }
}
