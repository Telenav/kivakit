package com.telenav.kivakit.conversion.core.value.version;

import com.telenav.kivakit.conversion.core.value.VersionConverter;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.version.Version.parseVersion;

public class VersionConverterTest extends CoreUnitTest
{
    @Test
    public void testVersionConverter()
    {
        ensureEqual(new VersionConverter(this).convert("3.0"), parseVersion(this, "3.0"));
        ensureEqual(new VersionConverter(this).convert("3.0.1"), parseVersion(this, "3.0.1"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-alpha"), parseVersion(this, "3.0.1-alpha"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-alpha-SNAPSHOT"), parseVersion(this, "3.0.1-alpha-SNAPSHOT"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-beta"), parseVersion(this, "3.0.1-beta"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-beta-SNAPSHOT"), parseVersion(this, "3.0.1-beta-SNAPSHOT"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-rc"), parseVersion(this, "3.0.1-rc"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-rc-SNAPSHOT"), parseVersion(this, "3.0.1-rc-SNAPSHOT"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-final"), parseVersion(this, "3.0.1-final"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-final-SNAPSHOT"), parseVersion(this, "3.0.1-final-SNAPSHOT"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-m1"), parseVersion(this, "3.0.1-m1"));
        ensureEqual(new VersionConverter(this).convert("3.0-SNAPSHOT"), parseVersion(this, "3.0-SNAPSHOT"));
    }
}
