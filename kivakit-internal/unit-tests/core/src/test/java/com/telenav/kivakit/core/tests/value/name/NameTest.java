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

package com.telenav.kivakit.core.tests.value.name;
import com.telenav.kivakit.core.test.support.CoreUnitTest;
import com.telenav.kivakit.core.value.name.Name;
import org.junit.Test;

public class NameTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        final Name bob = new Name("Bob");
        ensureEqual(new Name("Bob"), bob);
        ensureEqual("Bob", bob.name());
        ensureEqual(new Name("bob"), bob.lowerCase());
        ensureEqual("Bob", bob.toString());
    }
}
