package com.telenav.kivakit.serialization.kryo;

import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.version.Version;
import org.junit.Test;

import static com.telenav.kivakit.core.version.Version.parseVersion;

public class KryoSerializationSessionTest extends KryoUnitTest
{
    @Test
    public void test()
    {
        testSessionSerialize(9);
        testSessionSerialize(parseVersion("6.0.0-snapshot"));
        testSessionSerialize(parseVersion("18.3.4-rc"));
        testSessionSerialize(parseVersion("4.0.9-m1"));
        testSessionSerialize(parseVersion("4.0"));
        testSessionSerialize(Bytes.bytes(9), parseVersion("1.0"));

        testSessionSerialize(new Problem("testing: $", 1));
        testSessionSerialize("test");
        testSessionSerialize(9);
        testSessionSerialize(true);
        testSessionSerialize(Boolean.TRUE);
        testSessionSerialize(4L);
        testSessionSerialize(Version.parseVersion("4.1.0-RELEASE"));
        testSessionSerialize(Bytes.bytes(1_004));
        testSessionSerialize(Duration.days(17));
        testSessionSerialize(Time.now());
    }
}
