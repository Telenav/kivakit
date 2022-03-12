package com.telenav.kivakit.serialization.gson;

import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.resource.serialization.ObjectSerializers;
import com.telenav.kivakit.serialization.gson.factory.BaseGsonFactory;
import com.telenav.kivakit.serialization.gson.factory.CoreGsonFactory;
import com.telenav.kivakit.serialization.gson.factory.GsonFactory;

/**
 * This class defines a KivaKit {@link Project}. It cannot be constructed with the new operator since it has a private
 * constructor. To access the singleton instance of this class, call {@link Project#resolveProject(Class)}, or use
 * {@link com.telenav.kivakit.core.project.ProjectTrait#project(Class)}.
 *
 * <p>
 * Project initialization associates a {@link GsonObjectSerializer} with the <i>.json</i> extension in {@link
 * ObjectSerializers}, and registers a {@link BaseGsonFactory} in the global {@link Registry}.
 * </p>
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
