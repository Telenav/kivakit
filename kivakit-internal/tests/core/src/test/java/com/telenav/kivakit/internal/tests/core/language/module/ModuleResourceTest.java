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

package com.telenav.kivakit.internal.tests.core.language.module;
import com.telenav.kivakit.core.language.module.ModuleResource;
import com.telenav.kivakit.core.language.module.Modules;
import com.telenav.kivakit.core.language.module.PackageReference;
import com.telenav.kivakit.internal.test.support.CoreUnitTest;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author jonathanl (shibo)
 */
@SuppressWarnings("ConstantConditions")
public class ModuleResourceTest extends CoreUnitTest
{
    @Test
    public void testFileName()
    {
        ModuleResource resource = a();
        ensureEqual(resource.fileNameAsJavaPath(), Path.of("a.txt"));
    }

    @Test
    public void testLastModified()
    {
        try
        {
            ensureEqual(a().lastModified(), Time.milliseconds(Files.getLastModifiedTime(packagePath().asJavaPath()).toMillis()));
        }
        catch (IOException ignored)
        {
        }
    }

    @Test
    public void testPackagePath()
    {
        ensureEqual(a().packageReference(), PackageReference.parsePackageReference(this, getClass(), "resources/a"));
    }

    @Test
    public void testPath()
    {
        ensureEqual(a().packageReference(), packagePath().withChild("resources.a"));
    }

    @Test
    public void testSize()
    {
        try
        {
            ensureEqual(a().size(), Bytes.bytes(Files.size(packagePath().asJavaPath())));
        }
        catch (IOException ignored)
        {
        }
    }

    @Test
    public void testUri()
    {
        var uri = a().uri();
        ensureEqual(uri.getScheme(), "file");
        ensure(uri.getPath().endsWith("tests/core/language/module/resources/a/a.txt"));
    }

    @Nullable
    private ModuleResource a()
    {
        return Modules.moduleResource(this, packagePath().withChild("resources/a/a.txt"));
    }

    private PackageReference packagePath()
    {
        return PackageReference.packageReference(getClass());
    }
}
