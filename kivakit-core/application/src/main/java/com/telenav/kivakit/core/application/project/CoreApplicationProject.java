package com.telenav.kivakit.core.application.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class CoreApplicationProject extends Project
{
    private static final Lazy<CoreApplicationProject> singleton = Lazy.of(CoreApplicationProject::new);

    public static CoreApplicationProject get()
    {
        return singleton.get();
    }

    protected CoreApplicationProject()
    {
    }
}
