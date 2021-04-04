package com.telenav.kivakit.core.test.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class CoreTestProject extends Project
{
    private static final Lazy<CoreTestProject> singleton = Lazy.of(CoreTestProject::new);

    public static CoreTestProject get()
    {
        return singleton.get();
    }

    protected CoreTestProject()
    {
    }
}
