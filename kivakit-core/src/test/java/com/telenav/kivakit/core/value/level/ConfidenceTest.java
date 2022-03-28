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

package com.telenav.kivakit.core.value.level;
import com.telenav.kivakit.core.test.CoreUnitTest;
import org.junit.Test;

public class ConfidenceTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        ensureEqual(Confidence.NO, Confidence.confidenceForByte((byte) 0x0));
        ensureEqual(Confidence.FULL, Confidence.confidenceForByte(Byte.MAX_VALUE));
        ensureEqual(Confidence.NO, Confidence.confidenceForInt(0));
        ensureEqual(Confidence.FULL, Confidence.confidenceForInt(255));
        ensure(Confidence.MEDIUM.isGreaterThan(Confidence.ZERO));
        ensure(Confidence.FULL.isGreaterThan(Confidence.MEDIUM));
        ensure(Confidence.LOW.isLessThan(Confidence.MEDIUM));
        ensure(Confidence.LOW.isLessThan(Confidence.FULL));
        ensure(Confidence.LOW.isGreaterThan(Confidence.ZERO));
        ensure(Confidence.ZERO.isZero());
        ensureFalse(Confidence.LOW.isZero());
        ensureEqual(0, Confidence.NO.asUnsignedByte());
        ensureEqual(255, Confidence.FULL.asUnsignedByte());
        ensureThrows(() -> Confidence.confidence(2));
        ensureThrows(() -> Confidence.confidence(-1));
        Confidence.confidence(1);
    }
}
