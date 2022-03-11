package com.telenav.kivakit.serialization.properties;

import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.resource.serialization.ObjectSerializers;

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
