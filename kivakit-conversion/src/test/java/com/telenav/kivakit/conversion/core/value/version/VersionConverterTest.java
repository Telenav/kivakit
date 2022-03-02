package com.telenav.kivakit.conversion.core.value.version;

import com.telenav.kivakit.conversion.core.value.VersionConverter;
import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.core.version.Version;
import org.junit.Test;

public class VersionConverterTest extends UnitTest
{
    @Test
    public void testVersionConverter()
    {
        ensureEqual(new VersionConverter(this).convert("3.0"), Version.parse(this, "3.0"));
        ensureEqual(new VersionConverter(this).convert("3.0.1"), Version.parse(this, "3.0.1"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-alpha"), Version.parse(this, "3.0.1-alpha"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-alpha-SNAPSHOT"), Version.parse(this, "3.0.1-alpha-SNAPSHOT"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-beta"), Version.parse(this, "3.0.1-beta"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-beta-SNAPSHOT"), Version.parse(this, "3.0.1-beta-SNAPSHOT"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-rc"), Version.parse(this, "3.0.1-rc"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-rc-SNAPSHOT"), Version.parse(this, "3.0.1-rc-SNAPSHOT"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-final"), Version.parse(this, "3.0.1-final"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-final-SNAPSHOT"), Version.parse(this, "3.0.1-final-SNAPSHOT"));
        ensureEqual(new VersionConverter(this).convert("3.0.1-m1"), Version.parse(this, "3.0.1-m1"));
        ensureEqual(new VersionConverter(this).convert("3.0-SNAPSHOT"), Version.parse(this, "3.0-SNAPSHOT"));
    }
}
