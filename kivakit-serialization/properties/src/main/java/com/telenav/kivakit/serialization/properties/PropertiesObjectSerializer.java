package com.telenav.kivakit.serialization.properties;

import com.telenav.kivakit.conversion.core.language.object.ObjectConverter;
import com.telenav.kivakit.core.language.Arrays;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.properties.PropertyMap;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.serialization.ObjectMetadata;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;
import com.telenav.kivakit.resource.serialization.SerializableObject;

import java.io.InputStream;
import java.io.OutputStream;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.INSTANCE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.TYPE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.VERSION;

/**
 * Reads a {@link SerializableObject} from a <i>.properties</i> file.
 *
 * @author jonathanl (shibo)
 * @see ObjectSerializer
 * @see Resource
 * @see Version
 * @see InstanceIdentifier
 */
public class PropertiesObjectSerializer implements ObjectSerializer
{
    private final ProgressReporter reporter;

    public PropertiesObjectSerializer()
    {
        this(ProgressReporter.nullProgressReporter());
    }

    public PropertiesObjectSerializer(ProgressReporter reporter)
    {
        this.reporter = reporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> SerializableObject<T> read(InputStream input,
                                          StringPath path,
                                          Class<T> type,
                                          ObjectMetadata... metadata)
    {
        // Load properties from the given resource,
        var properties = PropertyMap.loadPropertyMap(this, input);
        try
        {
            // and if a type wasn't explicitly specified,
            if (type == null)
            {
                // then load it using the class name specified by the 'class' property.
                ensure(Arrays.contains(metadata, TYPE), "Must specify either an explicit type or ObjectMetadata.TYPE to read the type from the input");
                var typeName = properties.get("class");
                if (typeName == null)
                {
                    typeName = properties.get("type");
                }
                ensureNotNull(typeName, "Cannot find 'class' or 'type' property in: $", path);
                type = ensureNotNull(Classes.classForName(typeName), "Unable to load class $, specified in $", typeName, path);
            }

            // Next, read any version
            Version version = null;
            if (Arrays.contains(metadata, VERSION))
            {
                version = Version.parseVersion(this, properties.get("version"));
            }

            // get any instance identifier,
            var instance = InstanceIdentifier.singletonInstance();
            if (Arrays.contains(metadata, INSTANCE))
            {
                var enumName = properties.getOrDefault("instance",
                        InstanceIdentifier.singletonInstance().identifier().name());
                instance = InstanceIdentifier.instanceIdentifierForEnumName(this, enumName);
            }

            // convert the property map to an object,
            var object = new ObjectConverter<>(this, type).convert(properties);
            if (object != null)
            {
                // and return the deserialized object.
                return new SerializableObject<>(object, version, instance);
            }
            else
            {
                return fail("Unable to convert properties to an object of type $: $", type, path);
            }
        }
        catch (Exception e)
        {
            return fail(e, "Unable to load properties for type $: $", type, path);
        }
    }

    @Override
    public ProgressReporter reporter()
    {
        return reporter;
    }

    @Override
    public <T> void write(OutputStream output,
                          StringPath path,
                          SerializableObject<T> object,
                          ObjectMetadata... metadata)
    {
        unsupported();
    }
}
