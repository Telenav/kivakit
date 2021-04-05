package com.telenav.kivakit.service.registry.client.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class ServiceClientProject extends Project
{
    private static final Lazy<ServiceClientProject> singleton = Lazy.of(ServiceClientProject::new);

    public static ServiceClientProject get()
    {
        return singleton.get();
    }

    protected ServiceClientProject()
    {
    }
}
