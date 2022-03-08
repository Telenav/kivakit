package com.telenav.kivakit.resource.resources.properties;

import com.telenav.kivakit.conversion.core.language.object.ObjectConverter;
import com.telenav.kivakit.core.language.Arrays;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.SerializedObject;
import com.telenav.kivakit.resource.WritableResource;
import com.telenav.kivakit.resource.serialization.ObjectMetadata;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.SINGLETON;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.INSTANCE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.TYPE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.VERSION;

/**
 * Reads a {@link SerializedObject} from a <i>.properties</i> file.
 *
 * @author jonathanl (shibo)
 * @see ObjectSerializer
 * @see Resource
 * @see Version
 * @see InstanceIdentifier
 */
public class PropertyMapSerializer implements ObjectSerializer
{
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> SerializedObject<T> read(Resource input, Class<T> type, ObjectMetadata... metadata)
    {
        // Load properties from the given resource,
        var properties = PropertyMap.load(this, input);
        try
        {
            // and if a type wasn't explicitly specified,
            if (type == null)
            {
                // then load it using the class name specified by the 'class' property.
                ensure(Arrays.contains(metadata, TYPE), "Must specify either a type or Metadata.TYPE");
                var typeName = ensureNotNull(properties.get("class"), "Missing class property in: $", input);
                type = ensureNotNull(Classes.forName(typeName), "Unable to load class: $", typeName);
            }

            // Next, read any version
            Version version = null;
            if (Arrays.contains(metadata, VERSION))
            {
                version = Version.parseVersion(this, properties.get("version"));
            }

            // get any instance identifier,
            var instance = SINGLETON;
            if (Arrays.contains(metadata, INSTANCE))
            {
                instance = InstanceIdentifier.of(properties.get("instance"));
            }

            // convert the property map to an object,
            var object = new ObjectConverter<>(this, type).convert(properties);
            if (object != null)
            {
                // and return the deserialized object.
                return new SerializedObject<>(object, version, instance);
            }
            else
            {
                return fail("Unable to convert properties to an object: $", input);
            }
        }
        catch (Exception e)
        {
            return fail(e, "Unable to load properties from: $", input);
        }
    }

    @Override
    public <T> boolean write(WritableResource out, SerializedObject<T> object,
                             com.telenav.kivakit.resource.serialization.ObjectMetadata... metadata)
    {
        return unsupported();
    }
}
