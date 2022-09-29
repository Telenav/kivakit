package com.telenav.kivakit.core.project;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.KivaKit;
import com.telenav.kivakit.core.version.Version;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.project.Project.resolveProject;

/**
 * Trait for {@link Project} access.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = STABLE_DEFAULT_EXTENSIBLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public interface ProjectTrait
{
    /**
     * Returns the {@link KivaKit} project
     *
     * @return The {@link KivaKit} project
     */
    default Project kivakit()
    {
        return project(KivaKit.class);
    }

    /**
     * Returns the version of KivaKit in use
     *
     * @return The version of KivaKit
     */
    default Version kivakitVersion()
    {
        return kivakit().kivakitVersion();
    }

    /**
     * Returns the {@link Project} object of the given type
     *
     * @param type The {@link Project} class
     * @param <T> The type of project
     * @return The project
     */
    default <T extends Project> T project(Class<T> type)
    {
        return resolveProject(type);
    }
}
