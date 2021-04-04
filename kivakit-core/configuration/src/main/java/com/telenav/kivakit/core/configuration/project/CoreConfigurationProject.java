package com.telenav.kivakit.core.configuration.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class CoreConfigurationProject extends Project
{
    private static final Lazy<CoreConfigurationProject> singleton = Lazy.of(CoreConfigurationProject::new);

    public static CoreConfigurationProject get()
    {
        return singleton.get();
    }

    protected CoreConfigurationProject()
    {
    }
}
