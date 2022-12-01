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

package com.telenav.kivakit.internal.tests.core.value.level;

import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.value.level.Confidence.FULL;
import static com.telenav.kivakit.core.value.level.Confidence.LOW;
import static com.telenav.kivakit.core.value.level.Confidence.MEDIUM;
import static com.telenav.kivakit.core.value.level.Confidence.NO;
import static com.telenav.kivakit.core.value.level.Confidence.ZERO;
import static com.telenav.kivakit.core.value.level.Confidence.confidence;
import static com.telenav.kivakit.core.value.level.Confidence.confidenceForByte;
import static com.telenav.kivakit.core.value.level.Confidence.confidenceForInt;

public class ConfidenceTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        ensureEqual(NO, confidenceForByte((byte) 0x0));
        ensureEqual(FULL, confidenceForByte(Byte.MAX_VALUE));
        ensureEqual(NO, confidenceForInt(0));
        ensureEqual(FULL, confidenceForInt(255));
        ensure(MEDIUM.isGreaterThan(ZERO));
        ensure(FULL.isGreaterThan(MEDIUM));
        ensure(LOW.isLessThan(MEDIUM));
        ensure(LOW.isLessThan(FULL));
        ensure(LOW.isGreaterThan(ZERO));
        ensure(ZERO.isZero());
        ensureFalse(LOW.isZero());
        ensureEqual(0, NO.asUnsignedByte());
        ensureEqual(255, FULL.asUnsignedByte());
        ensureThrows(() -> confidence(2));
        ensureThrows(() -> confidence(-1));
        confidence(1);
    }
}
