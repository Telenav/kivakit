package com.telenav.kivakit.serialization.gson;

import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.resource.SerializedObject;
import com.telenav.kivakit.resource.resources.OutputResource;
import com.telenav.kivakit.resource.serialization.ObjectMetadata;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.ensure.Ensure.fail;
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
    public <T> SerializedObject<T> read(InputStream input,
                                        StringPath path,
                                        Class<T> typeToRead,
                                        ObjectMetadata... metadata)
    {
        return tryCatch(() ->
        {
            var json = IO.string(input);

            // Get any instance identifier
            var instance = InstanceIdentifier.SINGLETON;
            var instanceMatcher = INSTANCE_PATTERN.matcher(json);
            if (INSTANCE.containedIn(metadata) && instanceMatcher.find())
            {
                instance = InstanceIdentifier.of(instanceMatcher.group("instance"));
            }

            // Get the type to instantiate
            Class<T> type = typeToRead;
            if (type == null && TYPE.containedIn(metadata))
            {
                var typeMatcher = TYPE_PATTERN.matcher(json);
                if (typeMatcher.find())
                {
                    type = Classes.forName(typeMatcher.group("type"));
                }
            }

            // Get version
            Version version = null;
            var versionMatcher = VERSION_PATTERN.matcher(json);
            if (versionMatcher.find())
            {
                version = Version.parseVersion(versionMatcher.group("version"));
            }

            if (type != null)
            {
                return new SerializedObject<>(factory.gson().fromJson(json, type), version, instance);
            }

            return fail("Unable to find type field in: $", path);
        });
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
    public <T> boolean write(OutputStream output,
                             StringPath path,
                             SerializedObject<T> object,
                             ObjectMetadata... metadata)
    {
        return tryCatchDefault(() ->
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

            new OutputResource(output).printWriter().println(json);
            return true;
        }, false);
    }
}
