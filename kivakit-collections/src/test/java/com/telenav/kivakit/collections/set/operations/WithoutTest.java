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

package com.telenav.kivakit.collections.set.operations;

import com.telenav.kivakit.collections.set.SetOperationTest;
import org.junit.Test;

public class WithoutTest extends SetOperationTest
{
    @Test
    public void test()
    {
        var a = set(1, 2, 3);
        var b = set(2, 3, 4);
        var without = new Without<>(a, b);
        ensureEqual(1, without.size());
        ensureFalse(without.isEmpty());
        ensure(without.contains(1));
        ensureFalse(without.contains(4));
        ensure(set(1).equals(without));
    }
}
