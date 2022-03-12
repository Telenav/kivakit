package com.telenav.kivakit.serialization.properties;

import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.project.ProjectTrait;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.resource.serialization.ObjectSerializers;

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
        require(ObjectSerializers.class, ObjectSerializers::new)
                .add(Extension.PROPERTIES, listenTo(new PropertiesObjectSerializer()));
    }
}
