package com.telenav.kivakit.core.project;

import com.telenav.kivakit.core.KivaKit;
import com.telenav.kivakit.core.version.Version;

import static com.telenav.kivakit.core.project.Project.resolveProject;

@SuppressWarnings("unused")
public interface ProjectTrait
{
    /**
     * Returns the {@link KivaKit} project
     */
    default Project kivakit()
    {
        return project(KivaKit.class);
    }

    /**
     * Returns the version of KivaKit in use
     */
    default Version kivakitVersion()
    {
        return kivakit().kivakitVersion();
    }

    /**
     * Returns the {@link Project} object of the given type
     *
     * @param type The {@link Project} class
     * @return The project
     */
    default <T extends Project> T project(Class<T> type)
    {
        return resolveProject(type);
    }
}
