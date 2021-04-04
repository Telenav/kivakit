package com.telenav.kivakit.data.formats.library.text.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class DataFormatsTextProject extends Project
{
    private static final Lazy<DataFormatsTextProject> singleton = Lazy.of(DataFormatsTextProject::new);

    public static DataFormatsTextProject get()
    {
        return singleton.get();
    }

    protected DataFormatsTextProject()
    {
    }
}
