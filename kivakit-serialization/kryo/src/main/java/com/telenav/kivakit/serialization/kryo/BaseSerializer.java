package com.telenav.kivakit.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.serialization.kryo.internal.lexakai.DiagramKryo;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.serialization.kryo.KryoSerializationSession.kryoSerializationSession;
import static java.util.Objects.requireNonNull;

/**
 * Kryo serializer extension class for serializing a particular type of object. Provides a thread-local version to
 * subclasses that is set with {@link #version(Version)} and retrieved in subclasses with {@link #version()}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramKryo.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public abstract class BaseSerializer<Value> extends Serializer<Value>
{
    private static final ThreadLocal<Version> threadLocalVersion = new ThreadLocal<>();

    /**
     * Sets the version being serialized for access through a thread-local. This reduces parameter passing
     */
    public static void version(Version version)
    {
        threadLocalVersion.set(requireNonNull(version));
    }

    /** The type for this serializer */
    private final Class<Value> type;

    /**
     * @param type The type of object being serialized
     */
    protected BaseSerializer(Class<Value> type)
    {
        this.type = requireNonNull(type);
    }

    /**
     * Called by Kryo when a value of this type needs to be read
     *
     * @return The value
     */
    @Override
    public Value read(Kryo kryo, Input input, Class<? extends Value> type)
    {
        return onRead(kryoSerializationSession(kryo));
    }

    /**
     * Returns the type to serialize
     */
    public final Class<?> type()
    {
        return type;
    }

    /**
     * Called by Kryo when a value of this type needs to be written
     *
     * @param value The value
     */
    @Override
    public final void write(Kryo kryo, Output output, Value value)
    {
        onWrite(kryoSerializationSession(kryo), value);
    }

    /**
     * Returns the value as read by the subclass of this serializer using the given session
     */
    protected abstract Value onRead(KryoSerializationSession session);

    /**
     * Writes the given value using the given session
     */
    protected abstract void onWrite(KryoSerializationSession session, Value value);

    /**
     * Flags an unsupported serialization version
     */
    protected void unsupportedVersion()
    {
        unsupported("Cannot serialize version $", version());
    }

    /**
     * Returns the version of the serialization session in progress
     */
    protected Version version()
    {
        return ensureNotNull(threadLocalVersion.get());
    }
}
