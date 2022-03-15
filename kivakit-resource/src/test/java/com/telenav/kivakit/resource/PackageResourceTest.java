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

package com.telenav.kivakit.resource;

import com.telenav.kivakit.core.path.PackagePath;
import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.resource.resources.PackageResource;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

/**
 * @author jonathanl (shibo)
 */
public class PackageResourceTest extends UnitTest
{
    @Test
    public void testLastModified()
    {
        ensure(a().lastModified().isAfter(Time.START_OF_UNIX_TIME));
    }

    @Test
    public void testPath()
    {
        var fullPath = a().path();
        ensureEqual(fullPath.join("/"), "com/telenav/kivakit/resource/a.txt");
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
        ensure(a().sizeInBytes().isGreaterThanOrEqualTo(Bytes.bytes(4)));
        ensure(b().sizeInBytes().isGreaterThanOrEqualTo(Bytes.bytes(5)));
    }

    @NotNull
    private PackagePath _package()
    {
        return PackagePath.packagePath(getClass());
    }

    private PackageResource a()
    {
        return PackageResource.packageResource(this, _package(), "a.txt");
    }

    private PackageResource b()
    {
        return PackageResource.packageResource(this, _package(), "b.txt");
    }
}
