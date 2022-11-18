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

import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import java.nio.file.Path;
import java.util.HashSet;

import static com.telenav.kivakit.core.language.module.Modules.allNestedModuleResources;
import static com.telenav.kivakit.core.language.module.Modules.moduleResource;
import static com.telenav.kivakit.core.language.module.Modules.moduleResources;
import static com.telenav.kivakit.core.language.module.Modules.nestedModuleResources;
import static com.telenav.kivakit.core.language.packaging.PackageReference.packageReference;
import static com.telenav.kivakit.core.language.packaging.PackageReference.parsePackageReference;

/**
 * @author jonathanl (shibo)
 */
@SuppressWarnings("ConstantConditions")
public class ModulesTest extends CoreUnitTest
{
    @Test
    public void testAllNestedResources()
    {
        var a = parsePackageReference(this, getClass(), "resources.a");
        var resources = allNestedModuleResources(this, a);
        ensureEqual(resources.size(), 2);
        var filenames = new HashSet<Path>();
        filenames.add(resources.get(0).fileNameAsJavaPath());
        filenames.add(resources.get(1).fileNameAsJavaPath());
        ensure(filenames.contains(Path.of("a.txt")));
        ensure(filenames.contains(Path.of("b.txt")));
        ensure(resources.get(0).size().isGreaterThan(Bytes._0));
        ensure(resources.get(1).size().isGreaterThan(Bytes._0));
    }

    @Test
    public void testNestedResources()
    {
        var a = parsePackageReference(this, getClass(), "resources.a");
        var resources = nestedModuleResources(this, a,
                resource -> resource.fileNameAsJavaPath().equals(Path.of("b.txt")));
        ensureEqual(resources.size(), 1);
        ensureEqual(resources.get(0).fileNameAsJavaPath(), Path.of("b.txt"));
        ensure(resources.get(0).size().isGreaterThan(Bytes._0));
    }

    @Test
    public void testResource()
    {
        var _package = packageReference(getClass());
        var path = _package.withChild("resources/a/a.txt");
        var resource = moduleResource(this, path);
        ensureNotNull(resource);
        ensureEqual(resource.fileNameAsJavaPath(), Path.of("a.txt"));
        ensureEqual(resource.packageReference(), _package.withChild("resources").withChild("a"));
        ensure(resource.size().isGreaterThan(Bytes._0));
    }

    @Test
    public void testResources()
    {
        var a = parsePackageReference(this, getClass(), "resources.a");
        var resources = moduleResources(this, a);
        ensureEqual(resources.size(), 1);
        ensureEqual(resources.get(0).fileNameAsJavaPath(), Path.of("a.txt"));
        ensure(resources.get(0).size().isGreaterThan(Bytes._0));
    }
}
