package com.telenav.kivakit.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.resource.serialization.ObjectMetadata;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.kryo.types.KryoTypes;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.METADATA_OBJECT_TYPE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.METADATA_OBJECT_VERSION;

/**
 * {@link Kryo} {@link ObjectSerializer} provider.
 *
 * @author jonathanl (shibo)
 * @see ObjectSerializer
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class KryoObjectSerializer implements
        ObjectSerializer,
        TryTrait
{
    private final ProgressReporter reporter;

    private final KryoTypes types;

    private final ThreadLocal<Kryo> kryo = ThreadLocal.withInitial(this::newKryo);

    /**
     * Creates a Kryo object serializer for the given Kryo-registered types
     *
     * @param types The types to register with Kryo
     */
    public KryoObjectSerializer(KryoTypes types)
    {
        this(types, ProgressReporter.nullProgressReporter());
    }

    /**
     * Creates a Kryo object serializer for the given Kryo-registered types
     *
     * @param types The types to register with Kryo
     * @param reporter The reporter to call as data is serialized
     */
    public KryoObjectSerializer(KryoTypes types, ProgressReporter reporter)
    {
        this.types = types;
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
    @SuppressWarnings("unchecked")
    @Override
    public <T> SerializableObject<T> readObject(@NotNull InputStream inputStream,
                                                @NotNull StringPath path,
                                                Class<T> type,
                                                ObjectMetadata @NotNull ... metadata)
    {
        try
        {
            // Wrap input stream in Kryo wrapper,
            var input = new Input(inputStream);

            // read any type from the input,
            if (type == null && METADATA_OBJECT_TYPE.containedIn(metadata))
            {
                type = (Class<T>) kryo.get().readObject(input, Class.class);
            }

            ensureNotNull(type, "Must specify type explicitly or specify ObjectMetadata.TYPE to read it from input");

            // read any version,
            Version version = null;
            if (METADATA_OBJECT_VERSION.containedIn(metadata))
            {
                version = kryo.get().readObject(input, Version.class);
            }

            // then read the object,
            var object = kryo.get().readObject(input, type);

            // and return it.
            return new SerializableObject<>(object, version);
        }
        catch (Exception e)
        {
            return fail(e, "Unable to read $ from: $", type, path);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void writeObject(@NotNull OutputStream outputStream,
                                @NotNull StringPath path,
                                @NotNull SerializableObject<T> object,
                                ObjectMetadata @NotNull ... metadata)
    {
        tryCatchThrow(() ->
        {
            // Wrap output stream in Kryo wrapper,
            var output = new Output(outputStream);

            // write any type,
            if (METADATA_OBJECT_TYPE.containedIn(metadata))
            {
                kryo.get().writeObject(output, object.object().getClass());
            }

            // write any version,
            if (METADATA_OBJECT_VERSION.containedIn(metadata))
            {
                kryo.get().writeObject(output, object.version());
            }

            // and write the object.
            kryo.get().writeObject(output, object.object());
            output.flush();
        }, "Unable to write object to $", path);
    }

    private Kryo newKryo()
    {
        var kryo = new Kryo();
        types.registerWith(kryo);
        kryo.setReferences(true);
        return kryo;
    }
}
