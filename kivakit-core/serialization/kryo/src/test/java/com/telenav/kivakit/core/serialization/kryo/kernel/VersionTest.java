package com.telenav.kivakit.core.serialization.kryo.kernel;

import com.telenav.kivakit.core.kernel.language.values.version.Version;
import com.telenav.kivakit.core.serialization.kryo.KryoUnitTest;
import org.junit.Test;

/**
 * @author jonathanl (shibo)
 */
public class VersionTest extends KryoUnitTest
{
    @Test
    public void testSerialization()
    {
        serializationTest(Version.parse("6.0.0-snapshot"));
        serializationTest(Version.parse("18.3.4-rc"));
        serializationTest(Version.parse("0.0.9-alpha"));
        serializationTest(Version.parse("4.0"));
    }
}
