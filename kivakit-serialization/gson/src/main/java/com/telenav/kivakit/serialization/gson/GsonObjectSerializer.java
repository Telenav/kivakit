package com.telenav.kivakit.serialization.gson;

import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.resource.resources.OutputResource;
import com.telenav.kivakit.resource.serialization.ObjectMetadata;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.io.IO.readString;
import static com.telenav.kivakit.core.language.Arrays.arrayContains;
import static com.telenav.kivakit.core.language.Classes.classForName;
import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;
import static com.telenav.kivakit.core.string.Brackets.bracket;
import static com.telenav.kivakit.core.version.Version.parseVersion;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.METADATA_OBJECT_INSTANCE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.METADATA_OBJECT_TYPE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.METADATA_OBJECT_VERSION;

/**
 * JSON {@link ObjectSerializer} implementation using Google Gson library.
 *
 * @author jonathanl (shibo)
 * @see ObjectSerializer
 */
public class GsonObjectSerializer implements
    ObjectSerializer,
    RegistryTrait,
    TryTrait
{
    private static final Pattern TYPE_PATTERN = Pattern.compile("\"(class|type)\"\\s*:\\s*\"(?<type>.+)\"");

    private static final Pattern INSTANCE_PATTERN = Pattern.compile("\"(instance)\"\\s*:\\s*\"(?<instance>.+)\"");

    private static final Pattern VERSION_PATTERN = Pattern.compile("\"(version)\"\\s*:\\s*\"(?<version>.+)\"");

    /** The progress reporter to notify as serialization proceeds */
    private final ProgressReporter progressReporter;

    /** The Gson object factory */
    private final GsonFactory factory = require(GsonFactory.class);

    /**
     * Create a Gson object serializer
     */
    public GsonObjectSerializer()
    {
        this(nullProgressReporter());
    }

    /**
     * Create a Gson object serializer
     *
     * @param progressReporter The progress reporter to update as serialization proceeds
     */
    public GsonObjectSerializer(ProgressReporter progressReporter)
    {
        this.progressReporter = progressReporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProgressReporter progressReporter()
    {
        return progressReporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> SerializableObject<T> readObject(@NotNull InputStream input,
                                                @NotNull StringPath path,
                                                Class<T> typeToRead,
                                                ObjectMetadata @NotNull ... metadata)
    {
        try
        {
            // Read JSON from input,
            var json = bracket(readString(this, input));

            // get the type to read,
            var type = arrayContains(metadata, METADATA_OBJECT_TYPE)
                ? ensureNotNull(type(json, metadata, typeToRead))
                : typeToRead;

            // and return the deserialized object.
            return new SerializableObject<>(factory.gson().fromJson(json, type),
                version(json), instance(json, metadata));
        }
        catch (Exception e)
        {
            throw problem(e, "Unable to read from " + path.join("/")).asException();
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
        tryCatchThrow(() ->
        {
            var json = factory.gson().toJson(object.object());

            if (METADATA_OBJECT_TYPE.containedIn(metadata))
            {
                json = json.replaceFirst("\\s*\\{", "{\n  \"type\": \"" + object.object().getClass().getName() + "\",");
            }

            if (METADATA_OBJECT_VERSION.containedIn(metadata))
            {
                json = json.replaceFirst("\\s*\\{", "{\n  \"version\": \"" + object.version() + "\", ");
            }

            if (METADATA_OBJECT_INSTANCE.containedIn(metadata) && object.instance() != null)
            {
                json = json.replaceFirst("\\s*\\{", "{\n  \"instance\": \"" + object.instance() + "\", ");
            }

            try (var out = new OutputResource(output).printWriter())
            {
                out.println(json);
            }
        }, "Unable to write to $: $", path, object.object().getClass().getSimpleName());
    }

    @NotNull
    private InstanceIdentifier instance(String json, ObjectMetadata[] metadata)
    {
        var instance = InstanceIdentifier.singleton();
        var instanceMatcher = INSTANCE_PATTERN.matcher(json);
        if (METADATA_OBJECT_INSTANCE.containedIn(metadata) && instanceMatcher.find())
        {
            instance = InstanceIdentifier.instanceIdentifierForEnumName(this, instanceMatcher.group("instance"));
        }
        return instance;
    }

    @Nullable
    private <T> Class<T> type(String json, ObjectMetadata[] metadata, Class<T> typeToRead)
    {
        Class<T> type = typeToRead;
        if (type == null && METADATA_OBJECT_TYPE.containedIn(metadata))
        {
            var typeMatcher = TYPE_PATTERN.matcher(json);
            if (typeMatcher.find())
            {
                type = classForName(typeMatcher.group("type"));
            }
        }

        return type;
    }

    @Nullable
    private Version version(String json)
    {
        Version version = null;
        var versionMatcher = VERSION_PATTERN.matcher(json);
        if (versionMatcher.find())
        {
            version = parseVersion(versionMatcher.group("version"));
        }
        return version;
    }
}
