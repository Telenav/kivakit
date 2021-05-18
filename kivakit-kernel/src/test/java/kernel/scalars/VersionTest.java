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

package kernel.scalars;

import com.telenav.kivakit.kernel.language.values.version.Version;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

@SuppressWarnings("ConstantConditions")
public class VersionTest
{
    @Test
    public void test()
    {
        ensure(Version.parse("2.0").isNewerThan(Version.parse("1.9.1-rc")));
        ensure(Version.parse("1.9").isOlderThan(Version.parse("1.9.1-rc")));
        ensure(Version.parse("1.9.3").isNewerThan(Version.parse("1.9.1-rc")));
        ensure(Version.parse("1.9.2-alpha").isOlderThan(Version.parse("1.9.3")));
        ensure(Version.parse("4.9.1-beta").isNewerThan(Version.parse("1.9.3")));
        ensure(Version.of(1, 0).isNewerThan(Version.of(0, 9)));
        ensure(Version.parse("1.9").isNewerThan(Version.parse("0.9")));
        ensureEqual(Version.of(1, 0, 0), Version.parse("1.0.0"));
        ensureEqual("1.0.0", Version.parse("1.0.0").toString());
        ensureEqual(Version.parse("1.0.5-SNAPSHOT"), Version.parse("1.0.5-SNAPSHOT"));
        ensure(!Version.parse("1.0.5-SNAPSHOT").isNewerThan(Version.parse("1.0.5-SNAPSHOT")));
        ensure(!Version.parse("1.0.5-SNAPSHOT").isOlderThan(Version.parse("1.0.5-SNAPSHOT")));
        ensure(Version.parse("1.0.6-SNAPSHOT").isNewerThan(Version.parse("1.0.5-SNAPSHOT")));
        ensure(Version.parse("1.0.4-SNAPSHOT").isOlderThan(Version.parse("1.0.5-SNAPSHOT")));
    }
}
