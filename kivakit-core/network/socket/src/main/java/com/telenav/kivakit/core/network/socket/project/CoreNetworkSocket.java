package com.telenav.kivakit.core.network.socket.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class CoreNetworkSocket extends Project
{
    private static final Lazy<CoreNetworkSocket> singleton = Lazy.of(CoreNetworkSocket::new);

    public static CoreNetworkSocket get()
    {
        return singleton.get();
    }

    protected CoreNetworkSocket()
    {
    }
}
