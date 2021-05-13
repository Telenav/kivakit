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

package com.telenav.kivakit.resource.reading;

import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.resource.resources.packaged.PackageResource;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

public class LineReaderTest extends UnitTest
{
    @Test
    public void test()
    {
        var i = 1;
        final var resource = PackageResource.of(PackagePath.packagePath(getClass()), "test.txt");
        for (final String line : resource.reader().linesAsStringList())
        {
            ensureEqual(i++, Integer.parseInt(line));
        }
    }
}
