package com.telenav.kivakit.settings;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.serialization.gson.GsonSerializationProject;
import com.telenav.kivakit.serialization.properties.PropertiesSerializationProject;

import static com.telenav.kivakit.core.collections.set.ObjectSet.objectSet;

public class SettingsProject extends Project
{
    @Override
    public ObjectSet<Class<? extends Project>> dependencies()
    {
        return objectSet(PropertiesSerializationProject.class,
                GsonSerializationProject.class);
    }
}
