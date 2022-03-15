package com.telenav.kivakit.core.project;

import com.telenav.kivakit.core.KivaKit;
import com.telenav.kivakit.core.version.Version;

import static com.telenav.kivakit.core.project.Project.resolveProject;

public interface ProjectTrait
{
    default Project kivakit()
    {
        return project(KivaKit.class);
    }

    default Version kivakitVersion()
    {
        return kivakit().kivakitVersion();
    }

    default <T extends Project> T project(Class<T> type)
    {
        return resolveProject(type);
    }

    default Build projectBuild()
    {
        return Build.build(getClass());
    }

    default Version projectVersion()
    {
        return kivakit().projectVersion();
    }
}
