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
import com.telenav.kivakit.resource.path.Extension;
import org.junit.Test;

/**
 * @author jonathanl (shibo)
 */
public class PackageFolderTest extends UnitTest
{
    @Test
    public void testResources()
    {
        if (!isQuickTest())
        {
            var folder = new Package(PackagePath.packagePath(getClass()));
            int textFiles = 0;
            for (var ignored : folder.resources(Extension.TXT::ends))
            {
                textFiles++;
            }
            ensureEqual(textFiles, 2);
        }
    }
}
