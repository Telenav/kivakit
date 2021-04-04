package com.telenav.kivakit.core.security.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class CoreSecurityProject extends Project
{
    private static final Lazy<CoreSecurityProject> singleton = Lazy.of(CoreSecurityProject::new);

    public static CoreSecurityProject get()
    {
        return singleton.get();
    }

    protected CoreSecurityProject()
    {
    }
}
