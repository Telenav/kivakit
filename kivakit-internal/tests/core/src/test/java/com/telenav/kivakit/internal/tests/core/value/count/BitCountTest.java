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

package com.telenav.kivakit.internal.tests.core.value.count;

import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.value.count.BitCount.bits;

public class BitCountTest extends CoreUnitTest
{
    @Test
    public void testMaximumSigned()
    {
        ensureEqual((long) Byte.MAX_VALUE, bits(8).maximumSigned());
        ensureEqual((long) Short.MAX_VALUE, bits(16).maximumSigned());
        ensureEqual((long) Integer.MAX_VALUE, bits(32).maximumSigned());
        ensureEqual(Long.MAX_VALUE, bits(64).maximumSigned());
    }

    @Test
    public void testMaximumUnsigned()
    {
        ensureEqual(0xffL, bits(8).maximumUnsigned());
        ensureEqual(0xffffL, bits(16).maximumUnsigned());
        ensureEqual(0xffff_ffffL, bits(32).maximumUnsigned());
        ensureEqual(Long.MAX_VALUE, bits(64).maximumUnsigned());
    }

    @Test
    public void testMinimum()
    {
        ensureEqual((long) Byte.MIN_VALUE, bits(8).minimumSigned());
        ensureEqual((long) Short.MIN_VALUE, bits(16).minimumSigned());
        ensureEqual((long) Integer.MIN_VALUE, bits(32).minimumSigned());
        ensureEqual(Long.MIN_VALUE, bits(64).minimumSigned());
    }
}
