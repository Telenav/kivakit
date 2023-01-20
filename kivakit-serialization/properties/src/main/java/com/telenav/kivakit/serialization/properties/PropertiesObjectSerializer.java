package com.telenav.kivakit.serialization.properties;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.core.language.object.ObjectConverter;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.serialization.ObjectMetadata;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.language.Arrays.arrayContains;
import static com.telenav.kivakit.core.language.Classes.classForName;
import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.instanceIdentifierForEnumName;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.singleton;
import static com.telenav.kivakit.core.version.Version.parseVersion;
import static com.telenav.kivakit.properties.PropertyMap.loadPropertyMap;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.METADATA_OBJECT_INSTANCE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.METADATA_OBJECT_TYPE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.METADATA_OBJECT_VERSION;

/**
 * Reads a {@link SerializableObject} from a <i>.properties</i> file. Writing is not supported at this time.
 *
 * @author jonathanl (shibo)
 * @see ObjectSerializer
 * @see Resource
 * @see Version
 * @see InstanceIdentifier
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class PropertiesObjectSerializer implements ObjectSerializer
{
    /** The progress reporter to call while serializing */
    private final ProgressReporter reporter;

    /**
     * Creates a properties file object serializer
     */
    public PropertiesObjectSerializer()
    {
        this(nullProgressReporter());
    }

    /**
     * Creates a properties file object serializer
     *
     * @param reporter The progress reporter to update as lines are read
     */
    public PropertiesObjectSerializer(ProgressReporter reporter)
    {
        this.reporter = reporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProgressReporter progressReporter()
    {
        return reporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> SerializableObject<T> readObject(@NotNull InputStream input,
                                                @NotNull StringPath path,
                                                Class<T> type,
                                                ObjectMetadata @NotNull ... metadata)
    {
        // Load properties from the given resource,
        var properties = loadPropertyMap(this, input);
        try
        {
            // and if a type wasn't explicitly specified,
            if (type == null)
            {
                // then load it using the class name specified by the 'class' property.
                ensure(arrayContains(metadata, METADATA_OBJECT_TYPE), "Must specify either an explicit type or ObjectMetadata.TYPE to read the type from the input");
                var typeName = properties.get("class");
                if (typeName == null)
                {
                    typeName = properties.get("type");
                }
                ensureNotNull(typeName, "Cannot find 'class' or 'type' property in: $", path);
                type = ensureNotNull(classForName(typeName), "Unable to load class $, specified in $", typeName, path);
            }

            // Next, read any version
            Version version = null;
            if (arrayContains(metadata, METADATA_OBJECT_VERSION))
            {
                version = parseVersion(this, properties.get("version"));
            }

            // get any instance identifier,
            var instance = singleton();
            if (arrayContains(metadata, METADATA_OBJECT_INSTANCE))
            {
                var enumName = properties.getOrDefault("instance", singleton().name());
                instance = instanceIdentifierForEnumName(this, enumName);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void writeObject(@NotNull OutputStream output,
                                @NotNull StringPath path,
                                @NotNull SerializableObject<T> object,
                                ObjectMetadata @NotNull ... metadata)
    {
        unsupported();
    }
}
