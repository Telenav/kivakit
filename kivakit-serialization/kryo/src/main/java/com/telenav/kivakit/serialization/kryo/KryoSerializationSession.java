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
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.core.SerializationSession;
import com.telenav.kivakit.serialization.kryo.lexakai.DiagramKryo;
import com.telenav.kivakit.serialization.kryo.types.KryoTypes;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * An implementation of {@link SerializationSession} using {@link Kryo}.
 *
 * @author jonathanl (shibo)
 * @see Kryo
 * @see SerializationSession
 */
@UmlClassDiagram(diagram = DiagramKryo.class)
@UmlRelation(diagram = DiagramKryo.class,
             label = "registers",
             referent = Serializer.class,
             referentCardinality = "*")
@LexakaiJavadoc(complete = true)
public final class KryoSerializationSession extends BaseRepeater implements
        Named,
        SerializationSession
{
    /**
     * Map from Kryo object back to serialization session
     */
    private static final Map<Kryo, KryoSerializationSession> kryoToSession = new HashMap<>();

    /**
     * @return The {@link KryoSerializationSession} for the given kryo object
     */
    public static KryoSerializationSession session(Kryo kryo)
    {
        return kryoToSession.get(kryo);
    }

    /** Kryo input when reading */
    private Input input;

    /** Serialization object */
    private final Kryo kryo = new DebugKryo(this);

    /** Kryo output when writing */
    private Output output;

    /** The Kryo types that have been registered for this session */
    private final KryoTypes types;

    /** The version of data being read or written */
    private Version version;

    /**
     * @param types The kryo types to register for this session
     */
    public KryoSerializationSession(KryoTypes types)
    {
        this.types = types;

        // Associate this session with the given kryo object so that BaseSerializer can look it up,
        kryoToSession.put(kryo, this);

        // turn on reference tracking by default to avoid hard-to-diagnose errors,
        trackReferences(true);

        // and register the given types with kryo.
        types.registerWith(kryo);
    }

    @Override
    public void flush(LengthOfTime maximumWaitTime)
    {
        if (isWriting())
        {
            trace("Flushing");
            IO.flush(output);
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
    public Version open(InputStream input,
                        OutputStream output,
                        SessionType type,
                        Version version)
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
                    var clientVersion = startReading(input);

                    // send our version to the client
                    startWriting(output, version);

                    // and return the client version.
                    return clientVersion;
                }

                default:
                    return fail("Unsupported session type: $", type);
            }
        }
        catch (Exception e)
        {
            return fail(e, "Unable to open $ serialization session for $ $",
                    type, input != null ? "READ" : "", output != null ? "WRITE" : "");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> SerializableObject<T> read()
    {
        try
        {
            var hasVersion = kryo.readObject(input, Boolean.class);
            Version version = null;
            if (hasVersion)
            {
                version = read(Version.class);
                trace("Read version $", version);
            }
            @SuppressWarnings("unchecked")
            var object = (T) kryo.readClassAndObject(input);
            trace("Read $", object);
            return new SerializableObject<>(object, version);
        }
        catch (Exception e)
        {
            fatal(e, "Unable to read object");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> Serializer<? extends T> serializer(Class<T> type)
    {
        return kryo.getRegistration(type).getSerializer();
    }

    /**
     * Turns on Kryo reference tracking
     */
    public KryoSerializationSession trackReferences(boolean track)
    {
        kryo.setReferences(track);
        return this;
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
    public <T> void write(SerializableObject<T> object)
    {
        try
        {
            kryo.writeObject(output, object.hasVersion());

            if (object.hasVersion())
            {
                write(object.version());
            }

            kryo.writeClassAndObject(output, object.object());
        }
        catch (Exception e)
        {
            fatal(e, "Unable to write $", object);
        }
    }

    private Version startReading(InputStream input)
    {
        assert input != null;

        this.input = new Input(input);
        trace("Starting to read at $", this.input.total());
        var version = read(Version.class);
        version(version);
        var kryoTypesName = read(String.class);
        if (!kryoTypesName.equals(kryoTypes().name()))
        {
            fail("Input stream kryo types $ != session kryo types $", kryoTypesName, kryoTypes().name());
        }
        return version;
    }

    private void startWriting(OutputStream output, Version version)
    {
        assert output != null;

        this.output = new Output(output);
        trace("Starting to write at $", this.output.total());
        version(version);
        write(version);
        write(kryoTypes().name());
        flush();
    }

    private void version(Version version)
    {
        this.version = version;
    }
}
