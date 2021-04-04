package com.telenav.kivakit.data.compression.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class DataCompressionProject extends Project
{
    private static final Lazy<DataCompressionProject> singleton = Lazy.of(DataCompressionProject::new);

    public static DataCompressionProject get()
    {
        return singleton.get();
    }

    protected DataCompressionProject()
    {
    }
}
