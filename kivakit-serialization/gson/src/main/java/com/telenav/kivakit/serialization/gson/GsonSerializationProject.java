package com.telenav.kivakit.serialization.gson;

import com.telenav.kivakit.core.BaseKivaKitProject;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.resource.serialization.ObjectSerializerRegistry;

import static com.telenav.kivakit.resource.Extension.JSON;

/**
 * This class defines a KivaKit {@link Project}. It cannot be constructed with the new operator since it has a private
 * constructor. To access the singleton instance of this class, call {@link Project#resolveProject(Class)}, or use
 * {@link com.telenav.kivakit.core.project.ProjectTrait#project(Class)}.
 *
 * <p>
 * Project initialization associates a {@link GsonObjectSerializer} with the <i>.json</i> extension in
 * {@link ObjectSerializerRegistry}, and registers a {@link BaseGsonFactory} in the global {@link Registry}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see GsonObjectSerializer
 * @see BaseGsonFactory
 * @see GsonFactory
 * @see Registry
 */
public class GsonSerializationProject extends BaseKivaKitProject
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitialize()
    {
        // Associate Gson object serializer with .json resources
        require(ObjectSerializerRegistry.class, ObjectSerializerRegistry::new)
            .add(JSON, listenTo(new GsonObjectSerializer()));

        // Register default GsonFactory
        register(new KivaKitCoreGsonFactory());
    }
}
