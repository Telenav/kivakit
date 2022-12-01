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

package com.telenav.kivakit.internal.tests.core.collections.map;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

public class VariableMapTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        var map = new VariableMap<String>();
        map.add("a", "2");
        map.add("b", "two");
        ensureEqual("2 = two", map.expand("${a} = ${b}"));
        ensureEqual("a 2 = b two", map.expand("a ${a} = b ${b}"));
    }
}
