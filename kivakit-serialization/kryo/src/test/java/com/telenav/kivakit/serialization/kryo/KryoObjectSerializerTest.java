package com.telenav.kivakit.serialization.kryo;

import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.version.Version;
import org.junit.Test;

public class KryoObjectSerializerTest extends KryoUnitTest
{
    @Test
    public void test()
    {
        testSerialize(new Problem("testing: $", 1));
        testSerialize("test");
        testSerialize(9);
        testSerialize(true);
        testSerialize(Boolean.TRUE);
        testSerialize(4L);
        testSerialize(Version.parseVersion("4.1.0-RELEASE"));
        testSerialize(Bytes.bytes(1_004));
        testSerialize(Duration.days(17));
        testSerialize(Time.now());
    }
}
