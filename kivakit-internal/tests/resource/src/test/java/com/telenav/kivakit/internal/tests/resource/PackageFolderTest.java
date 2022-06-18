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

import com.telenav.kivakit.testing.UnitTest;
import com.telenav.kivakit.resource.packages.PackageTrait;
import org.junit.Test;

import static com.telenav.kivakit.resource.Extension.TXT;

/**
 * @author jonathanl (shibo)
 */
public class PackageFolderTest extends UnitTest implements PackageTrait
{
    @Test
    public void testResources()
    {
        if (!isQuickTest())
        {
            var folder =  thisPackage();
            int textFiles = 0;
            for (var ignored : folder.resources(TXT::ends))
            {
                textFiles++;
            }
            ensureEqual(textFiles, 2);
        }
    }
}
