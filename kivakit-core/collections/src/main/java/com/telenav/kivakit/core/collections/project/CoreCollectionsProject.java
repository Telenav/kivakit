package com.telenav.kivakit.core.collections.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class CoreCollectionsProject extends Project
{
    private static final Lazy<CoreCollectionsProject> singleton = Lazy.of(CoreCollectionsProject::new);

    public static CoreCollectionsProject get()
    {
        return singleton.get();
    }

    protected CoreCollectionsProject()
    {
    }
}
