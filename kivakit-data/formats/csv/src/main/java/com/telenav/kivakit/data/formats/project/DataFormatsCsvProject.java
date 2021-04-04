package com.telenav.kivakit.data.formats.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class DataFormatsCsvProject extends Project
{
    private static final Lazy<DataFormatsCsvProject> singleton = Lazy.of(DataFormatsCsvProject::new);

    public static DataFormatsCsvProject get()
    {
        return singleton.get();
    }

    protected DataFormatsCsvProject()
    {
    }
}
