package com.telenav.kivakit.core.serialization.jersey.json.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class SerializationJerseyJsonProject extends Project
{
    private static final Lazy<SerializationJerseyJsonProject> singleton = Lazy.of(SerializationJerseyJsonProject::new);

    public static SerializationJerseyJsonProject get()
    {
        return singleton.get();
    }

    protected SerializationJerseyJsonProject()
    {
    }
}
