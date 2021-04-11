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

package kernel.scalars;

import com.telenav.kivakit.core.kernel.data.validation.reporters.ValidationFailure;
import com.telenav.kivakit.core.kernel.language.values.level.Confidence;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureFalse;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

public class ConfidenceTest
{
    @Test
    public void test()
    {
        ensureEqual(Confidence.NO, Confidence.forByte((byte) 0x0));
        ensureEqual(Confidence.FULL, Confidence.forByte(Byte.MAX_VALUE));
        ensureEqual(Confidence.NO, Confidence.forUnsignedByte(0));
        ensureEqual(Confidence.FULL, Confidence.forUnsignedByte(255));
        ensure(Confidence.MEDIUM.isGreaterThan(Confidence.ZERO));
        ensure(Confidence.FULL.isGreaterThan(Confidence.MEDIUM));
        ensure(Confidence.LOW.isLessThan(Confidence.MEDIUM));
        ensure(Confidence.LOW.isLessThan(Confidence.FULL));
        ensure(Confidence.LOW.isGreaterThan(Confidence.ZERO));
        ensure(Confidence.ZERO.isZero());
        ensureFalse(Confidence.LOW.isZero());
        ensureEqual(0, Confidence.NO.asUnsignedByte());
        ensureEqual(255, Confidence.FULL.asUnsignedByte());
        try
        {
            Confidence.confidence(2);
            fail("Should throw");
        }
        catch (final ValidationFailure ignored)
        {
        }
        try
        {
            Confidence.confidence(-1);
            fail("Should throw");
        }
        catch (final ValidationFailure ignored)
        {
        }
        Confidence.confidence(1);
    }
}
