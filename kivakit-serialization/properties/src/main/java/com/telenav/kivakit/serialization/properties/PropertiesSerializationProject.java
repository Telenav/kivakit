package com.telenav.kivakit.serialization.properties;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.project.ProjectTrait;
import com.telenav.kivakit.resource.serialization.ObjectSerializerRegistry;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.resource.Extension.PROPERTIES;

/**
 * This class defines a KivaKit {@link Project}. It cannot be constructed with the new operator since it has a private
 * constructor. To access the singleton instance of this class, call {@link Project#resolveProject(Class)}, or use
 * {@link ProjectTrait#project(Class)}.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class PropertiesSerializationProject extends Project
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitialize()
    {
        // Register .properties object serializer
        require(ObjectSerializerRegistry.class, ObjectSerializerRegistry::new)
                .add(PROPERTIES, listenTo(new PropertiesObjectSerializer()));
    }
}
