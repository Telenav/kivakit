package com.telenav.kivakit.core.network.http.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class CoreNetworkHttpProject extends Project
{
    private static final Lazy<CoreNetworkHttpProject> singleton = Lazy.of(CoreNetworkHttpProject::new);

    public static CoreNetworkHttpProject get()
    {
        return singleton.get();
    }

    protected CoreNetworkHttpProject()
    {
    }
}
