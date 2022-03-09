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

package com.telenav.kivakit.serialization.kryo;

import com.telenav.kivakit.core.version.Version;
import org.junit.Test;

/**
 * @author jonathanl (shibo)
 */
public class VersionTest extends KryoUnitTest
{
    @Test
    public void testSerialization()
    {
        serializationTest(Version.parseVersion(this, "6.0.0-snapshot"));
        serializationTest(Version.parseVersion(this, "18.3.4-rc"));
        serializationTest(Version.parseVersion(this, "4.0.9-m1"));
        serializationTest(Version.parseVersion(this, "4.0"));
    }
}
