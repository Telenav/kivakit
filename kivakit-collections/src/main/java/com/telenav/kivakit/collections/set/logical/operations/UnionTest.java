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

package com.telenav.kivakit.collections.set.logical.operations;

import com.telenav.kivakit.collections.set.SetOperationTest;
import org.junit.Test;

import java.util.Set;

public class UnionTest extends SetOperationTest
{
    @Test
    public void test()
    {
        final Set<Integer> a = set(1, 2, 3);
        final Set<Integer> b = set(2, 3, 4);
        final Union<Integer> union = new Union<>(a, b);
        ensureEqual(4, union.size());
        ensureFalse(union.isEmpty());
        ensure(union.contains(4));
        ensureFalse(union.contains(5));
        ensure(set(1, 2, 3, 4).equals(union));
    }
}
