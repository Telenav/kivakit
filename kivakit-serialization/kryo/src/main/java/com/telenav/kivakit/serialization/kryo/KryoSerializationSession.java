////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.map.ObjectMap;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.language.values.version.VersionedObject;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.serialization.core.SerializationSession;
import com.telenav.kivakit.serialization.kryo.project.lexakai.diagrams.DiagramSerializationKryo;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * An extension of the {@link Kryo} object which adds enhanced reading and writing capabilities and enforces versioning
 * of all data that is read or written, according to the {@link SerializationSession} contract.
 *
 * <p>
 * To begin serializing data, you must call {@link SerializationSession#open(Type, Version, InputStream, OutputStream)}.
 * Once as session has started, the current version is made available to subclasses through a thread-local so there is
 * no need to pass version information around during serialization.
 * </p>
 *
 * <p>
 * In the case of {@link SerializationSession.Type#RESOURCE} sessions, the input and output stream are opened and the
 * version is read and written. For {@link SerializationSession.Type#CLIENT} and {@link
 * SerializationSession.Type#SERVER} sessions, version handshaking occurs with the client writing its version first and
 * the server reading it before the server writes its version and the client reads that.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Kryo
 * @see SerializationSession
 * @see KryoSerializer
 */
@UmlClassDiagram(diagram = DiagramSerializationKryo.class)
@UmlRelation(diagram = DiagramSerializationKryo.class,
             label = "registers",
             referent = Serializer.class,
             referentCardinality = "*")
@LexakaiJavadoc(complete = true)
public final class KryoSerializationSession extends BaseRepeater implements Named, SerializationSession
{
    /**
     * Map from Kryo object back to serialization session
     */
    private static final Map<Kryo, KryoSerializationSession> kryoToSession = new IdentityHashMap<>();

    /**
     * @return The session for the given kryo object, used by {@link KryoSerializer}.
     */
    static KryoSerializationSession session(final Kryo kryo)
    {
        return kryoToSession.get(kryo);
    }

    /** Serialization object */
    private final Kryo kryo = new DebugKryo(this);

    /** The version of data being read or written */
    private Version version;

    /** Kryo input when reading */
    private Input input;

    /** Kryo output when writing */
    private Output output;

    /** The Kryo types that have been registered for this session */
    private final KryoTypes types;

    /**
     * @param types The kryo types to register for this session
     */
    public KryoSerializationSession(final KryoTypes types)
    {
        this.types = types;

        // Associate this session with the given kryo object so that KryoSerializer can look it up,
        kryoToSession.put(kryo, this);

        // turn on reference tracking,
        trackReferences();

        // and register the given types with kryo.
        types.registerWith(kryo);
    }

    @Override
    public void flush(final Duration maximumWaitTime)
    {
        if (isWriting())
        {
            trace("Flushing");

            if (!IO.flush(output))
            {
                close();
            }
        }
    }

    @Override
    public boolean isReading()
    {
        return input != null;
    }

    @Override
    public boolean isWriting()
    {
        return output != null;
    }

    public KryoTypes kryoTypes()
    {
        return types;
    }

    @Override
    public void onClose()
    {
        if (isReading())
        {
            IO.close(input);
            input = null;
        }

        if (isWriting())
        {
            flush();
            IO.close(output);
            output = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Version open(final Type type,
                        final Version version,
                        final InputStream input,
                        final OutputStream output)
    {
        trace("Opening serialization session: type = $, version = $, kryoTypes = $", type, version, kryoTypes().name());

        try
        {
            // If the session type is,
            switch (type)
            {
                case CLIENT:
                {
                    // write our version to the server,
                    startWriting(output, version);

                    // and return the server version.
                    return startReading(input);
                }

                case RESOURCE:
                {
                    // If we are reading
                    if (input != null)
                    {
                        // then we can't be writing,
                        assert output == null;

                        // and return the resource version.
                        return startReading(input);
                    }

                    // If we are writing,
                    if (output != null)
                    {
                        // write our version to the resource,
                        startWriting(output, version);

                        // and return it.
                        return version;
                    }

                    // If we get here, then we aren't reading or writing.
                    fail("Input and output streams both null");
                }

                case SERVER:
                {
                    // read the client's version,
                    final var clientVersion = startReading(input);

                    // send our version to the client
                    startWriting(output, version);

                    // and return the client version.
                    return clientVersion;
                }

                default:
                    return fail("Unsupported session type: $", type);
            }
        }
        catch (final Exception e)
        {
            return fail(e, "Unable to open $ serialization session for $ $",
                    type, input != null ? "READ" : "", output != null ? "WRITE" : "");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> VersionedObject<T> read()
    {
        try
        {
            final var version = readVersion();
            trace("Read version $", version);
            final T object = readClassAndObject();
            trace("Read $", object);
            return new VersionedObject<>(version, object);
        }
        catch (final Exception e)
        {
            throwProblem(e, "Unable to read object");
        }
        return null;
    }

    @Override
    public boolean readBoolean()
    {
        return kryo.readObject(input, boolean.class);
    }

    @Override
    public byte readByte()
    {
        return kryo.readObject(input, byte.class);
    }

    @Override
    public char readChar()
    {
        return kryo.readObject(input, char.class);
    }

    public Registration readClass()
    {
        return kryo.readClass(input);
    }

    @SuppressWarnings("unchecked")
    public <T> T readClassAndObject()
    {
        return (T) kryo.readClassAndObject(input);
    }

    @Override
    public double readDouble()
    {
        return kryo.readObject(input, double.class);
    }

    @Override
    public float readFloat()
    {
        return kryo.readObject(input, float.class);
    }

    @Override
    public int readInt()
    {
        return kryo.readObject(input, int.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Element> ObjectList<Element> readList()
    {
        final int size = readInt();
        final var list = new ObjectList<Element>();
        for (var i = 0; i < size; i++)
        {
            list.add((Element) kryo.readClassAndObject(input));
        }
        return list;
    }

    @Override
    public <Element> ObjectList<Element> readList(final Class<Element> type)
    {
        final var size = readInt();
        final var list = new ObjectList<Element>();
        for (var i = 0; i < size; i++)
        {
            list.add(kryo.readObject(input, type));
        }
        return list;
    }

    @Override
    public long readLong()
    {
        return kryo.readObject(input, long.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Key, Value> ObjectMap<Key, Value> readMap()
    {
        final var map = new ObjectMap<Key, Value>();
        for (var i = 0; i < readInt(); i++)
        {
            final var key = (Key) kryo.readClassAndObject(input);
            final var value = (Value) kryo.readClassAndObject(input);
            map.put(key, value);
        }
        return map;
    }

    @Override
    public <T> T readObject(final Class<T> type)
    {
        return kryo.readObject(input, type);
    }

    @Override
    public short readShort()
    {
        return kryo.readObject(input, short.class);
    }

    @Override
    public String readString()
    {
        return kryo.readObject(input, String.class);
    }

    @Override
    public Version readVersion()
    {
        return kryo.readObject(input, Version.class);
    }

    @SuppressWarnings("unchecked")
    public <T> Serializer<? extends T> serializer(final Class<T> type)
    {
        return kryo.getRegistration(type).getSerializer();
    }

    public void trackReferences()
    {
        kryo.setReferences(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Version version()
    {
        return version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void write(final VersionedObject<T> object)
    {
        try
        {
            writeVersion(object.version());
            kryo.writeClassAndObject(output, object.get());
        }
        catch (final Exception e)
        {
            throwProblem(e, "Unable to write $", object);
        }
    }

    @Override
    public void writeBoolean(final boolean value)
    {
        kryo.writeObject(output, value);
    }

    @Override
    public void writeByte(final byte value)
    {
        kryo.writeObject(output, value);
    }

    @Override
    public void writeChar(final char value)
    {
        kryo.writeObject(output, value);
    }

    @Override
    public void writeDouble(final double value)
    {
        kryo.writeObject(output, value);
    }

    @Override
    public void writeFloat(final float value)
    {
        kryo.writeObject(output, value);
    }

    @Override
    public void writeInt(final int value)
    {
        kryo.writeObject(output, value);
    }

    @Override
    public <Element> void writeList(final List<Element> list)
    {
        writeInt(list.size());
        for (final var element : list)
        {
            kryo.writeClassAndObject(output, element);
        }
    }

    @Override
    public <Element> void writeList(final List<Element> list, final Class<Element> type)
    {
        writeInt(list.size());
        for (final var element : list)
        {
            kryo.writeObject(output, element);
        }
    }

    @Override
    public void writeLong(final long value)
    {
        kryo.writeObject(output, value);
    }

    @Override
    public <Key, Value> void writeMap(final Map<Key, Value> map)
    {
        writeInt(map.size());
        for (final var key : map.keySet())
        {
            kryo.writeClassAndObject(output, key);
            kryo.writeClassAndObject(output, map.get(key));
        }
    }

    @Override
    public void writeObject(final Object object)
    {
        kryo.writeObject(output, object);
    }

    @Override
    public void writeShort(final short value)
    {
        kryo.writeObject(output, value);
    }

    @Override
    public void writeString(final String value)
    {
        kryo.writeObject(output, value);
    }

    @Override
    public void writeVersion(final Version version)
    {
        writeObject(version);
    }

    private Version startReading(final InputStream input)
    {
        assert input != null;

        this.input = new Input(input);
        trace("Starting to read at $", this.input.total());
        final var version = readVersion();
        version(version);
        final var name = readString();
        if (!name.equals(kryoTypes().name()))
        {
            fail("Input stream kryo types $ != session kryo types $", name, kryoTypes().name());
        }
        return version;
    }

    private void startWriting(final OutputStream output, final Version version)
    {
        assert output != null;

        this.output = new Output(output);
        trace("Starting to write at $", this.output.total());
        version(version);
        writeVersion(version);
        writeString(kryoTypes().name());
        flush();
    }

    private void version(final Version version)
    {
        this.version = version;
        KryoSerializer.version(version);
    }
}
