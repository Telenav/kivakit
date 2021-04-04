package com.telenav.kivakit.core.commandline.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class CoreCommandLineProject extends Project
{
    private static final Lazy<CoreCommandLineProject> singleton = Lazy.of(CoreCommandLineProject::new);

    public static CoreCommandLineProject get()
    {
        return singleton.get();
    }

    protected CoreCommandLineProject()
    {
    }
}
