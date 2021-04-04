package com.telenav.kivakit.core.network.ftp.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class CoreNetworkFtpProject extends Project
{
    private static final Lazy<CoreNetworkFtpProject> singleton = Lazy.of(CoreNetworkFtpProject::new);

    public static CoreNetworkFtpProject get()
    {
        return singleton.get();
    }

    protected CoreNetworkFtpProject()
    {
    }
}
