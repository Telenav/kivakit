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

package com.telenav.kivakit.resource.reading;

import com.telenav.kivakit.core.path.PackagePath;
import com.telenav.kivakit.resource.resources.PackageResource;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class LineReaderTest extends UnitTest
{
    @Test
    public void test()
    {
        var i = 1;
        var resource = PackageResource.packageResource(this, PackagePath.packagePath(getClass()), "test.txt");
        for (String line : resource.reader().linesAsStringList())
        {
            ensureEqual(i++, Integer.parseInt(line));
        }
    }
}
