package com.telenav.kivakit.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.resource.SerializableObject;
import com.telenav.kivakit.resource.serialization.ObjectMetadata;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;
import com.telenav.kivakit.serialization.kryo.types.KryoTypes;

import java.io.InputStream;
import java.io.OutputStream;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.TYPE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.VERSION;

/**
 * {@link Kryo} {@link ObjectSerializer} provider.
 *
 * @author jonathanl (shibo)
 */
public class KryoObjectSerializer implements
        ObjectSerializer,
        TryTrait
{
    private final ProgressReporter reporter;

    private final KryoTypes types;

    private final ThreadLocal<Kryo> kryo = ThreadLocal.withInitial(this::newKryo);

    public KryoObjectSerializer(KryoTypes types)
    {
        this(types, ProgressReporter.none());
    }

    public KryoObjectSerializer(KryoTypes types, ProgressReporter reporter)
    {
        this.types = types;
        this.reporter = reporter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> SerializableObject<T> read(InputStream inputStream,
                                          StringPath path,
                                          Class<T> type,
                                          ObjectMetadata... metadata)
    {
        try
        {
            // Wrap input stream in Kryo wrapper,
            var input = new Input(inputStream);

            // read any type from the input,
            if (type == null && TYPE.containedIn(metadata))
            {
                type = (Class<T>) kryo.get().readObject(input, Class.class);
            }

            ensureNotNull(type, "Must specify type explicitly or specify ObjectMetadata.TYPE to read it from input");

            // read any version,
            Version version = null;
            if (ObjectMetadata.VERSION.containedIn(metadata))
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

    @Override
    public ProgressReporter reporter()
    {
        return reporter;
    }

    @Override
    public <T> boolean write(OutputStream outputStream,
                             StringPath path,
                             SerializableObject<T> object,
                             ObjectMetadata... metadata)
    {
        return tryCatchDefault(() ->
        {
            // Wrap output stream in Kryo wrapper,
            var output = new Output(outputStream);

            // write any type,
            if (TYPE.containedIn(metadata))
            {
                kryo.get().writeObject(output, object.object().getClass());
            }

            // write any version,
            if (VERSION.containedIn(metadata))
            {
                kryo.get().writeObject(output, object.version());
            }

            // and write the object.
            kryo.get().writeObject(output, object.object());
            output.flush();
            return true;
        }, false);
    }

    private Kryo newKryo()
    {
        var kryo = new Kryo();
        types.registerWith(kryo);
        return kryo;
    }
}
