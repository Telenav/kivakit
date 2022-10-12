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

package com.telenav.kivakit.internal.tests.resource;

import com.telenav.kivakit.resource.packages.PackageResource;
import com.telenav.kivakit.resource.packages.PackageTrait;
import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Time.START_OF_UNIX_TIME;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;

/**
 * @author jonathanl (shibo)
 */
public class PackageResourceTest extends UnitTest implements PackageTrait
{
    @Test
    public void testLastModified()
    {
        ensure(a().lastModified().isAfter(START_OF_UNIX_TIME));
    }

    @Test
    public void testPath()
    {
        var fullPath = a().path();
        ensureEqual(fullPath.join("/"), "com/telenav/kivakit/internal/tests/resource/a.txt");
    }

    @Test
    public void testRead()
    {
        ensureEqual(a().reader().asString().trim(), "123");
        ensureEqual(b().reader().asString().trim(), "1234");
    }

    @Test
    public void testSize()
    {
        ensure(a().sizeInBytes().isGreaterThanOrEqualTo(bytes(4)));
        ensure(b().sizeInBytes().isGreaterThanOrEqualTo(bytes(5)));
    }

    private PackageResource a()
    {
        return packageResource("a.txt");
    }

    private PackageResource b()
    {
        return packageResource("b.txt");
    }
}
