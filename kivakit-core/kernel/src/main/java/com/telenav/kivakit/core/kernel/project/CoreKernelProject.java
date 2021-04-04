package com.telenav.kivakit.core.kernel.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;

/**
 * @author jonathanl (shibo)
 */
public class CoreKernelProject extends Project
{
    private static final Lazy<CoreKernelProject> singleton = Lazy.of(CoreKernelProject::new);

    public static CoreKernelProject get()
    {
        return singleton.get();
    }

    protected CoreKernelProject()
    {
    }
}
