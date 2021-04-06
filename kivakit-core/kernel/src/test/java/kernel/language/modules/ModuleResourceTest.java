////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.modules;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.language.modules.ModuleResource;
import com.telenav.kivakit.core.kernel.language.modules.Modules;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author jonathanl (shibo)
 */
@SuppressWarnings("ConstantConditions")
public class ModuleResourceTest
{
    @Test
    public void testFileName()
    {
        final ModuleResource resource = a();
        Ensure.ensureEqual(resource.fileNameAsJavaPath(), Path.of("a.txt"));
    }

    @Test
    public void testLastModified()
    {
        try
        {
            Ensure.ensureEqual(a().lastModified(), Time.milliseconds(Files.getLastModifiedTime(packagePath().asJavaPath()).toMillis()));
        }
        catch (final IOException ignored)
        {
        }
    }

    @Test
    public void testPackagePath()
    {
        Ensure.ensureEqual(a().packagePath(), PackagePath.parsePackagePath(getClass(), "resources/a"));
    }

    @Test
    public void testPath()
    {
        Ensure.ensureEqual(a().packagePath(), packagePath().withChild("resources.a"));
    }

    @Test
    public void testSize()
    {
        try
        {
            Ensure.ensureEqual(a().size(), Bytes.bytes(Files.size(packagePath().asJavaPath())));
        }
        catch (final IOException ignored)
        {
        }
    }

    @Test
    public void testUri()
    {
        final var uri = a().uri();
        Ensure.ensureEqual(uri.getScheme(), "file");
        Ensure.ensure(uri.getPath().endsWith("kernel/language/modules/resources/a/a.txt"));
    }

    @Nullable
    private ModuleResource a()
    {
        return Modules.resource(packagePath().withChild("resources/a/a.txt"));
    }

    private PackagePath packagePath()
    {
        return PackagePath.packagePath(getClass());
    }
}
