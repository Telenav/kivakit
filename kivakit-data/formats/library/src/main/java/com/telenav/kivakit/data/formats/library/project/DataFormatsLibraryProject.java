package com.telenav.kivakit.data.formats.library.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class DataFormatsLibraryProject extends Project
{
    private static final Lazy<DataFormatsLibraryProject> singleton = Lazy.of(DataFormatsLibraryProject::new);

    public static DataFormatsLibraryProject get()
    {
        return singleton.get();
    }

    protected DataFormatsLibraryProject()
    {
    }
}
