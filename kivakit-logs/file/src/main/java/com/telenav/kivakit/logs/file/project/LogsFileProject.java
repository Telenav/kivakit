package com.telenav.kivakit.logs.file.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class LogsFileProject extends Project
{
    private static final Lazy<LogsFileProject> singleton = Lazy.of(LogsFileProject::new);

    public static LogsFileProject get()
    {
        return singleton.get();
    }

    protected LogsFileProject()
    {
    }
}
