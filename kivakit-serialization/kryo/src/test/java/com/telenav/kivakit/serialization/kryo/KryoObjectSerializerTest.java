package com.telenav.kivakit.serialization.kryo;

import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.version.Version;
import org.junit.Test;

import java.util.ArrayList;

public class KryoObjectSerializerTest extends KryoUnitTest
{
    @Test
    public void test()
    {
        testSerialization(new Problem("testing: $", 1));
        testSerialization("test");
        testSerialization(9);
        testSerialization(true);
        testSerialization(Boolean.TRUE);
        testSerialization(4L);
        testSerialization(Version.parseVersion("4.1.0-RELEASE"));
        testSerialization(Bytes.bytes(1_004));
        testSerialization(Duration.days(17));
        testSerialization(Time.now());
        
        var list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        testSerialization(list);
    }
}
