package com.telenav.kivakit.core.serialization.kryo;

import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.kivakit.core.kernel.language.values.version.Version;
import com.telenav.kivakit.core.kernel.language.values.version.VersionedObject;
import com.telenav.kivakit.core.serialization.core.SerializationSession;
import com.telenav.kivakit.core.serialization.core.SerializationSessionFactory;
import com.telenav.kivakit.core.test.UnitTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.telenav.kivakit.core.serialization.core.SerializationSession.Type.RESOURCE;

/**
 * Adds Kryo serialization testing to the {@link UnitTest} base class.
 *
 * @author jonathanl (shibo)
 */
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
                    session.open(RESOURCE, KivaKit.get().version(), output);
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
                    Ensure.ensureEqual(session.open(RESOURCE, KivaKit.get().version(), input), KivaKit.get().version());
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
