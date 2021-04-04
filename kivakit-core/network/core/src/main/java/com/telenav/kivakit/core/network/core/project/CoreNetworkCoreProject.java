package com.telenav.kivakit.core.network.core.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class CoreNetworkCoreProject extends Project
{
    private static final Lazy<CoreNetworkCoreProject> singleton = Lazy.of(CoreNetworkCoreProject::new);

    public static CoreNetworkCoreProject get()
    {
        return singleton.get();
    }

    protected CoreNetworkCoreProject()
    {
    }
}
