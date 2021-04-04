package com.telenav.kivakit.data.formats.library.xml.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class DataFormatsXmlProject extends Project
{
    private static final Lazy<DataFormatsXmlProject> singleton = Lazy.of(DataFormatsXmlProject::new);

    public static DataFormatsXmlProject get()
    {
        return singleton.get();
    }

    protected DataFormatsXmlProject()
    {
    }
}
