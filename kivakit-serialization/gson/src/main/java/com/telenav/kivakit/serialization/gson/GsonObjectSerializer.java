package com.telenav.kivakit.serialization.gson;

import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.language.Arrays;
import com.telenav.kivakit.core.language.Classes;
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
import com.telenav.kivakit.serialization.gson.factory.GsonFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.INSTANCE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.TYPE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.VERSION;

/**
 * JSON {@link ObjectSerializer} using Google Gson library.
 *
 * @author jonathanl (shibo)
 */
public class GsonObjectSerializer implements
        ObjectSerializer,
        RegistryTrait,
        TryTrait
{
    private static final Pattern TYPE_PATTERN = Pattern.compile("\"(class|type)\"\\s*:\\s*\"(?<type>.+)\"");

    private static final Pattern INSTANCE_PATTERN = Pattern.compile("\"(instance)\"\\s*:\\s*\"(?<instance>.+)\"");

    private static final Pattern VERSION_PATTERN = Pattern.compile("\"(version)\"\\s*:\\s*\"(?<version>.+)\"");

    private final ProgressReporter reporter;

    private final GsonFactory factory = require(GsonFactory.class);

    public GsonObjectSerializer()
    {
        this(ProgressReporter.none());
    }

    public GsonObjectSerializer(ProgressReporter reporter)
    {
        this.reporter = reporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> SerializableObject<T> read(InputStream input,
                                          StringPath path,
                                          Class<T> typeToRead,
                                          ObjectMetadata... metadata)
    {
        return tryCatchThrow(() ->
        {
            // Read JSON from input,
            var json = IO.string(this, input);

            // get the type to read,
            var type = Arrays.contains(metadata, TYPE)
                    ? ensureNotNull(type(json, metadata, typeToRead))
                    : typeToRead;

            // and return the deserialized object.
            return new SerializableObject<>(factory.gson().fromJson(json, type),
                    version(json), instance(json, metadata));
        }, "Unable to read from $", path);
    }

    @Override
    public ProgressReporter reporter()
    {
        return reporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void write(OutputStream output,
                          StringPath path,
                          SerializableObject<T> object,
                          ObjectMetadata... metadata)
    {
        tryCatchThrow(() ->
        {
            var json = factory.gson().toJson(object);

            if (TYPE.containedIn(metadata))
            {
                json = json.replaceAll("\\s*\\{", "{\n\"type\": \"" + object.object().getClass().getName() + "\"");
            }

            if (VERSION.containedIn(metadata))
            {
                json = json.replaceAll("\\s*\\{", "{\n\"version\": \"" + object.version() + "\"");
            }

            if (INSTANCE.containedIn(metadata) && object.instance() != null)
            {
                json = json.replaceAll("\\s*\\{", "{\n\"instance\": \"" + object.instance() + "\"");
            }

            try (var out = new OutputResource(output).printWriter())
            {
                out.println(json);
            }
        }, "Unable to write to: $", path);
    }

    @NotNull
    private InstanceIdentifier instance(String json, ObjectMetadata[] metadata)
    {
        var instance = InstanceIdentifier.singletonInstance();
        var instanceMatcher = INSTANCE_PATTERN.matcher(json);
        if (INSTANCE.containedIn(metadata) && instanceMatcher.find())
        {
            instance = InstanceIdentifier.instanceIdentifierForEnumName(this, instanceMatcher.group("instance"));
        }
        return instance;
    }

    @Nullable
    private <T> Class<T> type(String json, ObjectMetadata[] metadata, Class<T> typeToRead)
    {
        Class<T> type = typeToRead;
        if (type == null && TYPE.containedIn(metadata))
        {
            var typeMatcher = TYPE_PATTERN.matcher(json);
            if (typeMatcher.find())
            {
                type = Classes.classForName(typeMatcher.group("type"));
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
            version = Version.parseVersion(versionMatcher.group("version"));
        }
        return version;
    }
}
