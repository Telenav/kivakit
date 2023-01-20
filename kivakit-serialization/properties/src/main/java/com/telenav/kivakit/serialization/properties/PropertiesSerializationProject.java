package com.telenav.kivakit.serialization.properties;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.BaseKivaKitProject;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.project.ProjectTrait;
import com.telenav.kivakit.resource.serialization.ObjectSerializerRegistry;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.resource.Extension.PROPERTIES;

/**
 * This class defines a KivaKit {@link Project}. It cannot be constructed with the new operator since it has a private
 * constructor. To access the singleton instance of this class, call {@link Project#resolveProject(Class)}, or use
 * {@link ProjectTrait#project(Class)}.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class PropertiesSerializationProject extends BaseKivaKitProject
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
