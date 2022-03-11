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

import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.resource.SerializableObject;
import com.telenav.kivakit.serialization.core.SerializationSession;
import com.telenav.kivakit.serialization.core.SerializationSessionFactory;
import com.telenav.kivakit.serialization.kryo.types.CoreKryoTypes;
import com.telenav.kivakit.serialization.kryo.types.KryoTypes;
import com.telenav.kivakit.serialization.kryo.types.ResourceKryoTypes;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static com.telenav.kivakit.serialization.core.SerializationSession.SessionType.RESOURCE;

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
@LexakaiJavadoc(complete = true)
public class KryoUnitTest extends UnitTest
{
    private SerializationSessionFactory factory;

    protected KryoTypes kryoTypes()
    {
        return new CoreKryoTypes().mergedWith(new ResourceKryoTypes());
    }

    protected SerializationSession session()
    {
        return sessionFactory().newSession(this);
    }

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
        var types = new CoreKryoTypes();
        var serializer = new KryoObjectSerializer(types);
        var path = StringPath.stringPath("/a/b/c");

        var write = new SerializableObject<>(object, version);
        serializer.write(output, path, write);

        var input = new ByteArrayInputStream(output.toByteArray());

        var read = serializer.read(input, path, object.getClass());
        ensureEqual(write, read);
    }

    protected void testSerialization(Object object)
    {
        testSerialization(object, null);
    }

    protected void testSessionSerialization(Object object)
    {
        testSessionSerialization(object, null);
    }

    protected void testSessionSerialization(Object object, Version version)
    {
        if (!isQuickTest())
        {
            trace("before serialization = $", object);

            var output = new ByteArrayOutputStream();

            // Write the object n times to the session
            var n = Count.count(3);
            {
                var session = new KryoSerializationSession(kryoTypes()).trackReferences(false); // session();
                session.open(RESOURCE, projectVersion(), output);
                n.loop(() -> session.write(new SerializableObject<>(object, version)));
                session.close();
            }

            // Read the object n times from the written data
            {
                var session = new KryoSerializationSession(kryoTypes()).trackReferences(false);
                var input = new ByteArrayInputStream(output.toByteArray());
                var streamVersion = session.open(RESOURCE, projectVersion(), input);
                ensureEqual(projectVersion(), streamVersion);
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
}
