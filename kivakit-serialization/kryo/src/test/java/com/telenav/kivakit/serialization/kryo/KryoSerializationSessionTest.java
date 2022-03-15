package com.telenav.kivakit.serialization.kryo;

import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.serialization.kryo.types.CoreKryoTypes;
import org.junit.Test;

import static com.telenav.kivakit.core.version.Version.parseVersion;

public class KryoSerializationSessionTest extends KryoUnitTest
{
    @Test
    public void test()
    {
        testSessionSerialization(9);
        testSessionSerialization(parseVersion("6.0.0-snapshot"));
        testSessionSerialization(parseVersion("18.3.4-rc"));
        testSessionSerialization(parseVersion("4.0.9-m1"));
        testSessionSerialization(parseVersion("4.0"));
        testSessionSerialization(Bytes.bytes(9), parseVersion("1.0"));

        testSessionSerialization(new Problem("testing: $", 1));
        testSessionSerialization("test");
        testSessionSerialization(9);
        testSessionSerialization(true);
        testSessionSerialization(Boolean.TRUE);
        testSessionSerialization(4L);
        testSessionSerialization(Version.parseVersion("4.1.0-RELEASE"));
        testSessionSerialization(Bytes.bytes(1_004));
        testSessionSerialization(Duration.days(17));
        testSessionSerialization(Time.now());
    }

    @Test
    public void testResource()
    {
        var file = File.temporary(Extension.KRYO);
        var version = Version.parseVersion("1.0");
        var session = new KryoSerializationSession(new CoreKryoTypes());
        session.writeResource(file, version, () -> session.write("Hello, World!"));
        session.readResource(file, () ->
        {
            var hello = session.read(String.class);
            ensureEqual(hello, "Hello, World!");
        });
    }
}
