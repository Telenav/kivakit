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

package com.telenav.kivakit.internal.tests.core.language;

import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.language.Objects;
import com.telenav.kivakit.core.language.primitive.Primitives;import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

public class ObjectsTest extends CoreUnitTest
{
    @Test
    public void testEquals()
    {
        ensure(Objects.equal(1, 1));
        ensure(Objects.equal(1, 1));
        ensureFalse(Objects.equal(1, 1.0));
        ensure(Objects.equal(null, null));
        ensureFalse(Objects.equal(1, null));
        ensureFalse(Objects.equal(null, 1));
        ensureFalse(Objects.equal(1, ""));
    }

    @Test
    public void testHashCode()
    {
        ensureEqual(Hash.many(1, 2, 3), Hash.many(1, 2, 3));
        ensureNotEqual(Hash.many(1, 2, 3), Hash.many(3, 2, 1));
    }

    @Test
    public void testIsPrimitiveWrapper()
    {
        ensureFalse(Primitives.isPrimitiveWrapper(ClassesTest.class));
        ensure(Primitives.isPrimitiveWrapper(7));
    }
}
