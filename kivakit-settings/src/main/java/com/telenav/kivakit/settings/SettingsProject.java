package com.telenav.kivakit.settings;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.serialization.gson.GsonSerializationProject;
import com.telenav.kivakit.serialization.properties.PropertiesSerializationProject;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.collections.set.ObjectSet.objectSet;

/**
 * Initializer for settings project
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class SettingsProject extends Project
{
    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectSet<Class<? extends Project>> dependencies()
    {
        // Settings stores depend on .properties and .json serialization
        return objectSet(PropertiesSerializationProject.class, GsonSerializationProject.class);
    }
}
