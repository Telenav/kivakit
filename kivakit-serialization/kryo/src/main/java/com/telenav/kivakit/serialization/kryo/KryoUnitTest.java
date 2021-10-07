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

import com.telenav.kivakit.kernel.KivaKit;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.language.values.version.VersionedObject;
import com.telenav.kivakit.serialization.core.SerializationSession;
import com.telenav.kivakit.serialization.core.SerializationSessionFactory;
import com.telenav.kivakit.test.UnitTest;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Adds Kryo serialization testing to the {@link UnitTest} base class. Serialization of objects can be tested with
 * {@link #serializationTest(Object)} and serialization sessions can be specialized by {@link #kryoTypes()} and {@link
 * #sessionFactory()}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class KryoUnitTest extends UnitTest
{
    private SerializationSessionFactory factory;

    protected KryoTypes kryoTypes()
    {
        return new CoreKernelKryoTypes();
    }

    protected void serializationTest(final Object object)
    {
        if (!isQuickTest())
        {
            trace("before serialization = $", object);

            final var version = Version.parse("1.0");
            final var data = new ByteArrayOutputStream();

            {
                final var session = session();
                try (final var output = data)
                {
                    session.open(SerializationSession.Type.RESOURCE, KivaKit.get().projectVersion(), output);
                    for (var index = 0; index < 3; index++)
                    {
                        session.write(new VersionedObject<>(version, object));
                    }
                    IO.close(session);
                }
                catch (final IOException e)
                {
                    e.printStackTrace();
                }
            }

            {
                final var session = session();
                try (final var input = new ByteArrayInputStream(data.toByteArray()))
                {
                    Ensure.ensureEqual(session.open(SerializationSession.Type.RESOURCE, KivaKit.get().projectVersion(), input), KivaKit.get().projectVersion());
                    for (var index = 0; index < 3; index++)
                    {
                        final var deserialized = session.read();
                        trace("version $ after deserialization = $", deserialized.version(), deserialized.get());
                        ensureEqual(deserialized.version(), version);
                        ensureEqual(deserialized.get(), object);
                    }
                }
                catch (final IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    protected SerializationSession session()
    {
        return sessionFactory().session(this);
    }

    protected final SerializationSessionFactory sessionFactory()
    {
        if (factory == null)
        {
            factory = kryoTypes().sessionFactory();
        }

        return factory;
    }
}
