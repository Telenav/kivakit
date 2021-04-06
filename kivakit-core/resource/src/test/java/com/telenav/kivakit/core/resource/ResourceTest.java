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

package com.telenav.kivakit.core.resource;

import com.telenav.kivakit.core.resource.resources.other.PropertyMap;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class ResourceTest extends UnitTest
{
    @Test
    public void testResolution()
    {
        final var properties = Resource.resolve("classpath:com/telenav/kivakit/core/resource/ResourceTest.properties");
        ensureEqual("b", PropertyMap.load(properties).get("a"));
    }
}
