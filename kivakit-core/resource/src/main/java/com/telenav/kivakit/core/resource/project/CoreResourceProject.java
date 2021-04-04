package com.telenav.kivakit.core.resource.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class CoreResourceProject extends Project
{
    private static final Lazy<CoreResourceProject> singleton = Lazy.of(CoreResourceProject::new);

    public static CoreResourceProject get()
    {
        return singleton.get();
    }

    protected CoreResourceProject()
    {
    }
}
