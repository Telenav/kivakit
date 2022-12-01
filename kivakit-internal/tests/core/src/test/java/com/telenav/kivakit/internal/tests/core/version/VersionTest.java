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
import com.telenav.kivakit.core.version.Version;
import org.junit.Test;

@SuppressWarnings("ConstantConditions")
public class VersionTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        ensure(Version.parseVersion(this, "2.0").isNewerThan(Version.parseVersion(this, "1.9.1-rc")));
        ensure(Version.parseVersion(this, "1.9").isOlderThan(Version.parseVersion(this, "1.9.1-rc")));
        ensure(Version.parseVersion(this, "1.9.3").isNewerThan(Version.parseVersion(this, "1.9.1-rc")));
        ensure(Version.parseVersion(this, "1.9.2-m3").isOlderThan(Version.parseVersion(this, "1.9.3")));
        ensure(Version.parseVersion(this, "4.9.1-beta").isNewerThan(Version.parseVersion(this, "1.9.3")));
        ensure(Version.version(1, 0).isNewerThan(Version.version(0, 9)));
        ensure(Version.parseVersion(this, "1.9").isNewerThan(Version.parseVersion(this, "0.9")));
        ensureEqual(Version.version(1, 0, 0), Version.parseVersion(this, "1.0.0"));
        ensureEqual("1.0.0", Version.parseVersion(this, "1.0.0").toString());
        ensureEqual(Version.parseVersion(this, "1.0.5-SNAPSHOT"), Version.parseVersion(this, "1.0.5-SNAPSHOT"));
        ensure(!Version.parseVersion(this, "1.0.5-SNAPSHOT").isNewerThan(Version.parseVersion(this, "1.0.5-SNAPSHOT")));
        ensure(!Version.parseVersion(this, "1.0.5-SNAPSHOT").isOlderThan(Version.parseVersion(this, "1.0.5-SNAPSHOT")));
        ensure(Version.parseVersion(this, "1.0.6-SNAPSHOT").isNewerThan(Version.parseVersion(this, "1.0.5-SNAPSHOT")));
        ensure(Version.parseVersion(this, "1.0.4-SNAPSHOT").isOlderThan(Version.parseVersion(this, "1.0.5-SNAPSHOT")));
    }
}
