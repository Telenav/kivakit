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

package com.telenav.kivakit.internal.tests.core.version;

import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.version.Version.parseVersion;
import static com.telenav.kivakit.core.version.Version.version;

@SuppressWarnings("ConstantConditions")
public class VersionTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        ensure(parseVersion(this, "2.0").isNewerThan(parseVersion(this, "1.9.1-rc")));
        ensure(parseVersion(this, "1.9").isOlderThan(parseVersion(this, "1.9.1-rc")));
        ensure(parseVersion(this, "1.9.3").isNewerThan(parseVersion(this, "1.9.1-rc")));
        ensure(parseVersion(this, "1.9.2-m3").isOlderThan(parseVersion(this, "1.9.3")));
        ensure(parseVersion(this, "4.9.1-beta").isNewerThan(parseVersion(this, "1.9.3")));
        ensure(version(1, 0).isNewerThan(version(0, 9)));
        ensure(parseVersion(this, "1.9").isNewerThan(parseVersion(this, "0.9")));
        ensureEqual(version(1, 0, 0), parseVersion(this, "1.0.0"));
        ensureEqual(parseVersion(this, "1.0.5-SNAPSHOT"), parseVersion(this, "1.0.5-SNAPSHOT"));
        ensure(!parseVersion(this, "1.0.5-SNAPSHOT").isNewerThan(parseVersion(this, "1.0.5-SNAPSHOT")));
        ensure(!parseVersion(this, "1.0.5-SNAPSHOT").isOlderThan(parseVersion(this, "1.0.5-SNAPSHOT")));
        ensure(parseVersion(this, "1.0.6-SNAPSHOT").isNewerThan(parseVersion(this, "1.0.5-SNAPSHOT")));
        ensure(parseVersion(this, "1.0.4-SNAPSHOT").isOlderThan(parseVersion(this, "1.0.5-SNAPSHOT")));
    }

    @Test
    public void testToString()
    {
        ensureEqual(version("1").toString(), "1");
        ensureEqual(version("1.5").toString(), "1.5");
        ensureEqual(version("1.0.0").toString(), "1.0.0");
        ensureEqual(version("99.14.17").toString(), "99.14.17");
    }
}
