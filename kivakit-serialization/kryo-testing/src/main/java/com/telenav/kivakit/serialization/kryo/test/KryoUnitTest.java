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

package com.telenav.kivakit.serialization.kryo.test;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.core.SerializationSession;
import com.telenav.kivakit.serialization.core.SerializationSessionFactory;
import com.telenav.kivakit.serialization.kryo.KryoObjectSerializer;
import com.telenav.kivakit.serialization.kryo.KryoSerializationSession;
import com.telenav.kivakit.serialization.kryo.KryoSerializationSessionFactory;
import com.telenav.kivakit.serialization.kryo.types.KivaKitCoreKryoTypes;
import com.telenav.kivakit.serialization.kryo.types.KivaKitResourceKryoTypes;
import com.telenav.kivakit.serialization.kryo.types.KryoTypes;
import com.telenav.kivakit.testing.UnitTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.path.StringPath.stringPath;
import static com.telenav.kivakit.core.version.Version.version;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.METADATA_OBJECT_VERSION;
import static com.telenav.kivakit.serialization.core.SerializationSession.SessionType.RESOURCE_SERIALIZATION_SESSION;

/**
 * Adds Kryo serialization testing to the {@link UnitTest} base class. Serialization of objects can be tested with:
 *
 * <ul>
 *     <li>{@link #testSerialization(Object)}</li>
 *     <li>{@link #testSerialization(Object, Version)}</li>
 *     <li>{@link #testSessionSerialization(Object)}</li>
 * </ul>
 *
 * <p>
 * The registered types used by these methods can be specified by overriding {@link #kryoTypes()}.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class KryoUnitTest extends UnitTest
{
    private SerializationSessionFactory factory;

    /**
     * Returns the kryo types for this unit test. By default this is the kivakit-core types and the kivakit-resource
     * types.
     */
    protected KryoTypes kryoTypes()
    {
        return new KivaKitCoreKryoTypes().mergedWith(new KivaKitResourceKryoTypes());
    }

    /**
     * Returns a new serialization session
     */
    protected SerializationSession session()
    {
        return sessionFactory().newSession(this);
    }

    /**
     * Returns the serialization session factory
     */
    protected final SerializationSessionFactory sessionFactory()
    {
        if (factory == null)
        {
            factory = new KryoSerializationSessionFactory(kryoTypes());
        }

        return factory;
    }

    protected <T> void testSerialization(T object, Version version)
    {
        var output = new ByteArrayOutputStream();
        var serializer = new KryoObjectSerializer(kryoTypes());
        var path = stringPath("/a/b/c");

        var write = new SerializableObject<>(object, version);
        serializer.writeObject(output, path, write, METADATA_OBJECT_VERSION);

        var input = new ByteArrayInputStream(output.toByteArray());

        var read = serializer.readObject(input, path, object.getClass(), METADATA_OBJECT_VERSION);
        ensureEqual(write, read);
    }

    protected void testSerialization(Object object)
    {
        testSerialization(object, version("1.0"));
    }

    protected void testSessionSerialization(Object object)
    {
        testSessionSerialization(object, version("1.0"));
    }

    protected void testSessionSerialization(Object object, Version version)
    {
        trace("before serialization = $", object);

        var output = new ByteArrayOutputStream();

        var n = Count.count(3);

        // Write the object n times to the session
        {
            var session = new KryoSerializationSession(kryoTypes());
            session.open(output, RESOURCE_SERIALIZATION_SESSION, version);
            n.loop(() -> session.write(new SerializableObject<>(object, version)));
            session.close();
        }

        // Read the object n times from the written data
        {
            var session = new KryoSerializationSession(kryoTypes());
            var input = new ByteArrayInputStream(output.toByteArray());
            var streamVersion = session.open(input, RESOURCE_SERIALIZATION_SESSION);
            ensureEqual(version, streamVersion);
            n.loop(() ->
            {
                var deserialized = session.read();
                assert deserialized != null;
                trace("version $ after deserialization = $", deserialized.version(), deserialized.object());
                ensureEqual(deserialized.object(), object);
                ensureEqual(deserialized.version(), version);
            });
            session.close();
        }
    }
}
