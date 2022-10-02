package com.telenav.kivakit.serialization.properties;

import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.project.ProjectTrait;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.serialization.ObjectSerializerRegistry;

/**
 * This class defines a KivaKit {@link Project}. It cannot be constructed with the new operator since it has a private
 * constructor. To access the singleton instance of this class, call {@link Project#resolveProject(Class)}, or use
 * {@link ProjectTrait#project(Class)}.
 *
 * @author jonathanl (shibo)
 */
public class PropertiesSerializationProject extends Project
{
    @Override
    public void onInitialize()
    {
        // Register .properties object serializer
        require(ObjectSerializerRegistry.class, ObjectSerializerRegistry::new)
                .add(Extension.PROPERTIES, listenTo(new PropertiesObjectSerializer()));
    }
}
