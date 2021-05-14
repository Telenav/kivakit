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

package kernel.language.pattern;

import com.telenav.kivakit.kernel.language.patterns.Pattern;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureFalse;

public class PatternTest
{
    @Test
    public void testFloatingPoint()
    {
        ensure(Pattern.FLOATING_POINT_NUMBER.matches("9"));
        ensure(Pattern.FLOATING_POINT_NUMBER.matches("99"));
        ensure(Pattern.FLOATING_POINT_NUMBER.matches("99.0"));
        ensure(Pattern.FLOATING_POINT_NUMBER.matches("0.9"));
        ensure(Pattern.FLOATING_POINT_NUMBER.matches("-.9"));
        ensure(Pattern.FLOATING_POINT_NUMBER.matches("-9"));
        ensure(Pattern.FLOATING_POINT_NUMBER.matches("-45.0"));
        ensureFalse(Pattern.FLOATING_POINT_NUMBER.matches("-9.45.0"));
        ensureFalse(Pattern.FLOATING_POINT_NUMBER.matches("+9.0"));
    }
}
