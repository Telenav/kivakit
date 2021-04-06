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

package com.telenav.kivakit.core.collections.set.logical.operations;

import com.telenav.kivakit.core.collections.set.SetOperationTest;
import org.junit.Test;

public class IntersectionTest extends SetOperationTest
{
    @Test
    public void test()
    {
        final var a = set(1, 2, 3);
        final var b = set(2, 3, 4);
        final var intersection = new Intersection<>(a, b);
        ensureEqual(2, intersection.size());
        ensureFalse(intersection.isEmpty());
        ensure(intersection.contains(3));
        ensureFalse(intersection.contains(1));
        ensure(set(2, 3).equals(intersection));
    }
}
