package com.telenav.kivakit.logs.email.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class LogsEmailProject extends Project
{
    private static final Lazy<LogsEmailProject> singleton = Lazy.of(LogsEmailProject::new);

    public static LogsEmailProject get()
    {
        return singleton.get();
    }

    protected LogsEmailProject()
    {
    }
}
