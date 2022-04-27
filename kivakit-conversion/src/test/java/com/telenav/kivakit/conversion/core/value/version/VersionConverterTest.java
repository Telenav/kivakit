package com.telenav.kivakit.conversion.core.value.version;

import com.telenav.kivakit.conversion.core.value.VersionConverter;
import com.telenav.kivakit.core.test.support.CoreUnitTest;
import com.telenav.kivakit.core.version.Version;
import org.junit.Test;

public class VersionConverterTest extends CoreUnitTest
{
    @Test
    public void testVersionConverter()
    {
        ensureEqual(new VersionConverter(this).convert("3.0"), Version.parseVersion(this, "3.0"));
        ensureEqual(new VersionConverter(this).convert("3.0.1"), Version.parseVersion(this, "3.0.1"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-alpha"), Version.parseVersion(this, "3.0.1-alpha"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-alpha-SNAPSHOT"), Version.parseVersion(this, "3.0.1-alpha-SNAPSHOT"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-beta"), Version.parseVersion(this, "3.0.1-beta"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-beta-SNAPSHOT"), Version.parseVersion(this, "3.0.1-beta-SNAPSHOT"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-rc"), Version.parseVersion(this, "3.0.1-rc"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-rc-SNAPSHOT"), Version.parseVersion(this, "3.0.1-rc-SNAPSHOT"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-final"), Version.parseVersion(this, "3.0.1-final"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-final-SNAPSHOT"), Version.parseVersion(this, "3.0.1-final-SNAPSHOT"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-m1"), Version.parseVersion(this, "3.0.1-m1"));
        ensureEqual(new VersionConverter(this).convert("3.0-SNAPSHOT"), Version.parseVersion(this, "3.0-SNAPSHOT"));
    }
}
