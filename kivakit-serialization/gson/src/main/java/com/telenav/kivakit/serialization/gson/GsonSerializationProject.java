package com.telenav.kivakit.serialization.gson;

import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.resource.serialization.ObjectSerializers;
import com.telenav.kivakit.serialization.gson.factory.BaseGsonFactory;
import com.telenav.kivakit.serialization.gson.factory.CoreGsonFactory;
import com.telenav.kivakit.serialization.gson.factory.GsonFactory;

/**
 * Project initializer that associates a {@link GsonObjectSerializer} with the <i>.json</i> extension in {@link
 * ObjectSerializers}, and registers a {@link BaseGsonFactory} in the global {@link Registry}.
 *
 * @author jonathanl (shibo)
 * @see GsonObjectSerializer
 * @see BaseGsonFactory
 * @see GsonFactory
 * @see Registry
 */
public class GsonSerializationProject extends Project
{
    @Override
    public void onInitialize()
    {
        // Associate Gson object serializer with .json resources
        require(ObjectSerializers.class, ObjectSerializers::new)
                .add(Extension.JSON, listenTo(new GsonObjectSerializer()));

        // Register default GsonFactory
        register(new CoreGsonFactory(this));
    }
}
