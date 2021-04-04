package com.telenav.kivakit.core.network.email.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class CoreNetworkEmailProject extends Project
{
    private static final Lazy<CoreNetworkEmailProject> singleton = Lazy.of(CoreNetworkEmailProject::new);

    public static CoreNetworkEmailProject get()
    {
        return singleton.get();
    }

    protected CoreNetworkEmailProject()
    {
    }
}
