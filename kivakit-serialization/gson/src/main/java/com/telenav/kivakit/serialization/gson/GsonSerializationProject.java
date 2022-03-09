package com.telenav.kivakit.serialization.gson;

import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.resource.serialization.ObjectSerializers;

public class GsonSerializationProject extends Project
{
    @Override
    public void onInitialize()
    {
        // Register .json object serializer
        require(ObjectSerializers.class).add(Extension.JSON, new GsonObjectSerializer());
    }
}
